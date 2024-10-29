package com.challenge.ecommerce.configs.security;

import com.challenge.ecommerce.authentication.controllers.dtos.IntrospectRequest;
import com.challenge.ecommerce.authentication.services.IAuthenticationService;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.stereotype.Component;
import javax.crypto.spec.SecretKeySpec;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {
  @Value("${jwt.signerKeyAccess}")
  @NonFinal
  String SIGNER_KEY_ACCESS;

  private final IAuthenticationService authenticationService;

  private NimbusJwtDecoder nimbusJwtDecoder = null;

  public CustomJwtDecoder(IAuthenticationService authenticationService) {
    this.authenticationService = authenticationService;
  }

  @Override
  public Jwt decode(String token) {
    var response =
        authenticationService.introspect(IntrospectRequest.builder().accessToken(token).build());
    if (!response.isValid()) throw new BadJwtException("Invalid token");
    if (Objects.isNull(nimbusJwtDecoder)) {
      SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY_ACCESS.getBytes(), "HS512");
      nimbusJwtDecoder =
          NimbusJwtDecoder.withSecretKey(secretKeySpec).macAlgorithm(MacAlgorithm.HS512).build();
    }
    return nimbusJwtDecoder.decode(token);
  }
}
