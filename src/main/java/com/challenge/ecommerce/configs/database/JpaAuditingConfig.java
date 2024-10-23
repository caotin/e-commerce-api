package com.challenge.ecommerce.configs.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing()
public class JpaAuditingConfig {
}
