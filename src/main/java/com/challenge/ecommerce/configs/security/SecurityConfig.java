package com.challenge.ecommerce.configs.security;

import com.challenge.ecommerce.utils.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class SecurityConfig {
  CustomJwtDecoder customJwtDecoder;
  JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
  RestAccessDeniedHandler restAccessDeniedHandler;
  static final String[] PUBLIC_POST_ENDPOINT = {
    "/api/auth/signup", "/api/auth/login", "/api/auth/refresh"
  };
  static final String[] PRIVATE_PUT_ENDPOINT = {"/api/users/me", "/api/address/{id}"};
  static final String[] PRIVATE_GET_ENDPOINT = {
    "/api/users/me", "/api/location/**", "/api/address/{id}", "/api/address"
  };
  static final String[] PRIVATE_ADMIN_POST_ENDPOINT = {"/api/users/register"};
  static final String[] PRIVATE_ADMIN_PUT_ENDPOINT = {"/api/users/*"};
  static final String[] PRIVATE_ADMIN_GET_ENDPOINT = {"/api/users/*", "/api/users/{id}/address"};
  static final String[] PRIVATE_ADMIN_DELETE_ENDPOINT = {"/api/users", "/api/address/{id}"};
  static final String[] PRIVATE_DELETE_ENDPOINT = {"/api/address/users/{id}"};
  static final String[] PRIVATE_POST_ENDPOINT = {"/api/address"};
  static final String[] SWAGGER_WHITELIST = {
    "/swagger-ui/**", "/v3/api-docs/**", "/swagger-resources/**", "/swagger-resources"
  };

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        request ->
            request
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.POST, PRIVATE_POST_ENDPOINT)
                .hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers(HttpMethod.DELETE, PRIVATE_DELETE_ENDPOINT)
                .hasAnyAuthority(Role.ADMIN.name(), Role.USER.name())
                .requestMatchers(HttpMethod.PUT, PRIVATE_PUT_ENDPOINT)
                .hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .requestMatchers(HttpMethod.GET, PRIVATE_GET_ENDPOINT)
                .hasAnyAuthority(Role.USER.toString(), Role.ADMIN.toString())
                .requestMatchers(HttpMethod.POST, PRIVATE_ADMIN_POST_ENDPOINT)
                .hasAuthority(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.PUT, PRIVATE_ADMIN_PUT_ENDPOINT)
                .hasAuthority(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.DELETE, PRIVATE_ADMIN_DELETE_ENDPOINT)
                .hasAuthority(Role.ADMIN.toString())
                .requestMatchers(HttpMethod.GET, PRIVATE_ADMIN_GET_ENDPOINT)
                .hasAuthority(Role.ADMIN.toString())
                .requestMatchers(SWAGGER_WHITELIST)
                .permitAll()
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(
                        jwtConfigurer ->
                            jwtConfigurer
                                .decoder(customJwtDecoder)
                                .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                    .authenticationEntryPoint(jwtAuthenticationEntryPoint))
        .exceptionHandling(handler -> handler.accessDeniedHandler(restAccessDeniedHandler));
    http.csrf(AbstractHttpConfigurer::disable);
    return http.build();
  }

  @Bean
  UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.addAllowedOrigin("*");
    configuration.addAllowedMethod("*");
    configuration.addAllowedHeader("*");
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }

  @Bean
  JwtAuthenticationConverter jwtAuthenticationConverter() {
    JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter =
        new JwtGrantedAuthoritiesConverter();
    jwtGrantedAuthoritiesConverter.setAuthorityPrefix("");
    JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
    jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
    return jwtAuthenticationConverter;
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
