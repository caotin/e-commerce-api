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
  String[] PUBLIC_POST_ENDPOINT = {"/api/auth/signup", "/api/auth/login"};
  String[] PRIVATE_PUT_ENDPOINT = {"/api/users/me"};
  String[] PRIVATE_GET_ENDPOINT = {"/api/users/me"};

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests(
        request ->
            request
                .requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINT)
                .permitAll()
                .requestMatchers(HttpMethod.PUT, PRIVATE_PUT_ENDPOINT)
                .hasAnyAuthority(Role.USER.name())
                .requestMatchers(HttpMethod.GET, PRIVATE_GET_ENDPOINT)
                .hasAnyAuthority(Role.USER.name())
                .anyRequest()
                .authenticated());
    http.oauth2ResourceServer(
            oauth2 ->
                oauth2
                    .jwt(jwtConfigurer -> jwtConfigurer.decoder(customJwtDecoder))
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
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(10);
  }
}
