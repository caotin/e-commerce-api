package com.challenge.ecommerce.authentication.services;

import com.challenge.ecommerce.authentication.controllers.dtos.*;
import com.challenge.ecommerce.utils.ApiResponse;

public interface IAuthenticationService {
    ApiResponse<AuthenticationResponse> authenticate(AuthenticationRequest authenticationRequest);
    ApiResponse<Void> logout();
    IntrospectResponse introspect(IntrospectRequest introspectRequest) ;
    ApiResponse<AuthenticationResponse>  refreshToken(RefreshRequest request) ;
}
