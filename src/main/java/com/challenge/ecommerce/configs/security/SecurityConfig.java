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
  static final String[] PRIVATE_PUT_ENDPOINT = {"/api/users/me"};
  static final String[] PRIVATE_GET_ENDPOINT = {"/api/users/me", "/api/location/**"};
  static final String[] PRIVATE_ADMIN_POST_ENDPOINT = {"/api/users/register"};
  static final String[] PRIVATE_ADMIN_PUT_ENDPOINT = {"/api/users/*"};
  static final String[] PRIVATE_ADMIN_GET_ENDPOINT = {"/api/users/*"};
  static final String[] PRIVATE_ADMIN_DELETE_ENDPOINT = {"/api/users"};

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
  public CorsFilter corsFilter() {
    CorsConfiguration config = new CorsConfiguration();
    config.addAllowedOrigin("*");
    config.addAllowedMethod("*");
    config.addAllowedHeader("*");
    UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource =
        new UrlBasedCorsConfigurationSource();
    urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", config);
    return new CorsFilter(urlBasedCorsConfigurationSource);
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
