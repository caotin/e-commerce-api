package com.challenge.ecommerce.configs.security;

import com.challenge.ecommerce.authentication.controllers.dtos.IntrospectRequest;
import com.challenge.ecommerce.authentication.services.IAuthenticationService;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
@AllArgsConstructor
@RequiredArgsConstructor
public class CustomJwtDecoder implements JwtDecoder {
  @Value("${jwt.signerKeyAccess}")
  @NonFinal
  String SIGNER_KEY_ACCESS;

  IAuthenticationService authenticationService;

  NimbusJwtDecoder jwtDecoder = null;

  @Override
  public Jwt decode(String token) throws JwtException {
    var response =
        authenticationService.introspect(IntrospectRequest.builder().accessToken(token).build());
    if (!response.isValid()) throw new BadJwtException("Invalid token");
    if (Objects.isNull(jwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY_ACCESS.getBytes(), "HS512");
      jwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
    return jwtDecoder.decode(token);
  }
}
