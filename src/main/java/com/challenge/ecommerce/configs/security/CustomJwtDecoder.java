package com.challenge.ecommerce.configs.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;


public class CustomJwtDecoder implements JwtDecoder {
    @Value("${jwt.signerKeyAccess}")
    private  String SIGNER_KEY_ACCESS;


    @Override
    public Jwt decode(String token) throws JwtException {

        return null;
    }


}
