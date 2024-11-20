package com.challenge.ecommerce.configs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import java.net.URI;
import java.net.URISyntaxException;
import lombok.extern.slf4j.Slf4j;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class RedisConfig {

  @Value("${spring.data.redis.url}")
  private String redisUrl;

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    try {
      URI redisUri = new URI(redisUrl);
      RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
      configuration.setHostName(redisUri.getHost());
      configuration.setPort(redisUri.getPort());

      if (redisUri.getUserInfo() != null) {
        configuration.setPassword(redisUri.getUserInfo().split(":", 2)[1]);
      }

      return new LettuceConnectionFactory(configuration);
    } catch (URISyntaxException e) {
      throw new RuntimeException("Redis URL is invalid", e);
    }
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> template = new RedisTemplate<>();
    template.setConnectionFactory(lettuceConnectionFactory());
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.setHashKeySerializer(new StringRedisSerializer());
    template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    template.afterPropertiesSet();
     log.info("Redis Template created");
    return template;
  }
}
