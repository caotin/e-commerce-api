package com.challenge.ecommerce.configs;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@EnableRedisRepositories
public class RedisConfig {

  @Value("${spring.data.redis.host}")
  String redisHost;

  @Value("${spring.data.redis.port}")
  int redisPort;

  @Value("${spring.data.redis.password}")
  String redisPassword;

  @Bean
  public LettuceConnectionFactory lettuceConnectionFactory() {
    RedisStandaloneConfiguration redisStandaloneConfiguration =
        new RedisStandaloneConfiguration(redisHost, redisPort);
    redisStandaloneConfiguration.setPassword(redisPassword);
    return new LettuceConnectionFactory(redisStandaloneConfiguration);
  }

  @Bean
  public RedisTemplate<String, Object> redisTemplate() {
    RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
    redisTemplate.setConnectionFactory(lettuceConnectionFactory());
    redisTemplate.setKeySerializer(new StringRedisSerializer());
    redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
    redisTemplate.setHashKeySerializer(new StringRedisSerializer());
    redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
    redisTemplate.afterPropertiesSet();
    return redisTemplate;
  }
}
