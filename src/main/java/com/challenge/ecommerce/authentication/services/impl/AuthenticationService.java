package com.challenge.ecommerce.authentication.services.impl;

import com.challenge.ecommerce.authentication.controllers.dtos.*;
import com.challenge.ecommerce.authentication.services.IAuthenticationService;
import com.challenge.ecommerce.exceptionHandlers.CustomRuntimeException;
import com.challenge.ecommerce.exceptionHandlers.ErrorCode;
import com.challenge.ecommerce.users.models.UserEntity;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.utils.ApiResponse;
import com.challenge.ecommerce.utils.AuthUtils;
import com.challenge.ecommerce.utils.enums.ResponseStatus;
import com.challenge.ecommerce.utils.enums.TokenType;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService implements IAuthenticationService {

  UserRepository userRepository;

  PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

  @NonFinal
  @Value("${jwt.signerKeyAccess}")
  protected String SIGNER_KEY_ACCESS_KEY;

  @NonFinal
  @Value("${jwt.signerKeyRefresh}")
  protected String SIGNER_KEY_REFRESH_KEY;

  @NonFinal
  @Value("${jwt.valid-duration}")
  protected String VALID_DURATION;

  @NonFinal
  @Value("${jwt.refreshable-duration}")
  protected String REFRESHABLE_DURATION;

  @Override
  public ApiResponse<AuthenticationResponse> authenticate(
      AuthenticationRequest authenticationRequest) {
    var user =
        userRepository
            .findByEmailAndNotDeleted(authenticationRequest.getEmail())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    if (!passwordEncoder.matches(authenticationRequest.getPassword(), user.getPassword())) {
      throw new CustomRuntimeException(ErrorCode.PASSWORD_INCORRECT);
    }
    var refreshToken = generateToken(user, TokenType.refresh_token);
    user.setRefresh_token(passwordEncoder.encode(refreshToken));
    userRepository.save(user);
    var authenticationResponse =
        AuthenticationResponse.builder()
            .name(user.getName())
            .role(user.getRole().toString())
            .accessToken(generateToken(user, TokenType.access_token))
            .refreshToken(refreshToken)
            .build();
    return ApiResponse.<AuthenticationResponse>builder()
        .message(ResponseStatus.SUCCESS_LOGIN.getMessage())
        .result(authenticationResponse)
        .build();
  }

  @Override
  public ApiResponse<Void> logout() {
    var user =
        userRepository
            .findByEmailAndNotDeleted(AuthUtils.getUserCurrent())
            .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
    user.setRefresh_token(null);
    userRepository.save(user);
    return ApiResponse.<Void>builder().message(ResponseStatus.SUCCESS.getMessage()).build();
  }

  @Override
  public IntrospectResponse introspect(IntrospectRequest introspectRequest) {
    var token = introspectRequest.getAccessToken();
    boolean isValid = true;
    try {
      verifyToken(token, TokenType.access_token);
    } catch (Exception e) {
      isValid = false;
    }
    return IntrospectResponse.builder().valid(isValid).build();
  }

  @Override
  public ApiResponse<AuthenticationResponse> refreshToken(RefreshRequest request) {
    SignedJWT signedJWT = verifyToken(request.getRefreshToken(), TokenType.refresh_token);
    try {
      var email = signedJWT.getJWTClaimsSet().getSubject();
      var user =
          userRepository
              .findByEmailAndNotDeleted(email)
              .orElseThrow(() -> new CustomRuntimeException(ErrorCode.USER_NOT_FOUND));
      if (!passwordEncoder.matches(request.getRefreshToken(), user.getRefresh_token())) {
        throw new CustomRuntimeException(ErrorCode.REFRESH_TOKEN_FAILED);
      }
      var refreshToken = generateToken(user, TokenType.refresh_token);
      user.setRefresh_token(passwordEncoder.encode(refreshToken));
      userRepository.save(user);
      var authenticationResponse =
          AuthenticationResponse.builder()
              .name(user.getName())
              .role(user.getRole().toString())
              .accessToken(generateToken(user, TokenType.access_token))
              .refreshToken(refreshToken)
              .build();
      return ApiResponse.<AuthenticationResponse>builder().result(authenticationResponse).build();
    } catch (ParseException e) {
      throw new CustomRuntimeException(ErrorCode.REFRESH_TOKEN_FAILED);
    }
  }

  private String generateToken(UserEntity userEntity, TokenType type) {
    var time = VALID_DURATION;
    var key = SIGNER_KEY_ACCESS_KEY;
    if (type.equals(TokenType.refresh_token)) {
      time = REFRESHABLE_DURATION;
      key = SIGNER_KEY_REFRESH_KEY;
    }
    JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);
    JWTClaimsSet jwtClaimsSet =
        new JWTClaimsSet.Builder()
            .subject(userEntity.getEmail())
            .issueTime(new Date())
            .expirationTime(
                new Date(
                    Instant.now().plus(Long.parseLong(time), ChronoUnit.SECONDS).toEpochMilli()))
            .jwtID(UUID.randomUUID().toString())
            .claim("scope", buildScope(userEntity))
            .build();
    Payload payload = new Payload(jwtClaimsSet.toJSONObject());
    JWSObject jwsObject = new JWSObject(header, payload);

    try {
      jwsObject.sign(new MACSigner(key.getBytes()));
      return jwsObject.serialize();
    } catch (JOSEException e) {
      throw new RuntimeException(e);
    }
  }

  private String buildScope(UserEntity userEntity) {
    StringJoiner scopeJoiner = new StringJoiner(" ");
    scopeJoiner.add(userEntity.getRole().toString());
    return scopeJoiner.toString();
  }

  private SignedJWT verifyToken(String token, TokenType type) {
    try {
      JWSVerifier verifier =
          type.equals(TokenType.access_token)
              ? new MACVerifier(SIGNER_KEY_ACCESS_KEY)
              : new MACVerifier(SIGNER_KEY_REFRESH_KEY);
      SignedJWT signedJWT = SignedJWT.parse(token);
      Date expirationDate = signedJWT.getJWTClaimsSet().getExpirationTime();
      var verify = signedJWT.verify(verifier);
      if (!expirationDate.after(new Date()) || !verify) {
        throw new CustomRuntimeException(ErrorCode.REFRESH_TOKEN_INVALID);
      }
      return signedJWT;
    } catch (JOSEException | ParseException e) {
      throw new CustomRuntimeException(ErrorCode.REFRESH_TOKEN_FAILED);
    }
  }
}
