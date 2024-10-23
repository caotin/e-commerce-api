package com.challenge.ecommerce.configs;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {

        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "${CLOUD_NAME}",
                "api_key", "${API_KEY}",
                "api_secret", "${API_SECRET}",
                "secure", true
        ));
    }
}
