package com.challenge.ecommerce.configs.database;

import com.challenge.ecommerce.users.models.UserEntity;
import com.challenge.ecommerce.users.repositories.UserRepository;
import com.challenge.ecommerce.utils.enums.Role;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class UserAdminAccountInit {
  final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

  @Value("${app.admin.username}")
  String ADMIN_USER_NAME;

  @Value("${app.admin.password}")
  String ADMIN_PASSWORD;

  @Value("${app.admin.email}")
  String ADMIN_EMAIL;

  @Bean
  ApplicationRunner applicationRunner(UserRepository userRepository) {
    log.info("Initializing application.....");
    return args -> {
      if (!userRepository.findActiveUserEmails(ADMIN_EMAIL)) {
        UserEntity user =
            UserEntity.builder()
                .name(ADMIN_USER_NAME)
                .email(ADMIN_EMAIL)
                .password(passwordEncoder.encode(ADMIN_PASSWORD))
                .role(Role.ADMIN)
                .avatar_link(
                    "https://stcv4.hnammobile.com/downloads/a/cach-chup-anh-selfie-dep-an-tuong-ban-nhat-dinh-phai-biet-81675319567.jpg")
                .build();

        user.setCreatedAt(LocalDateTime.now());

        userRepository.save(user);
        log.warn("admin user has been created with default password: admin, please change it");
      }
      log.info("Application initialization completed .....");
    };
  }
}
