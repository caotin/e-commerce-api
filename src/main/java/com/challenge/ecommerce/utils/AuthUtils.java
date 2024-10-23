package com.challenge.ecommerce.utils;

import com.challenge.ecommerce.users.repositories.UserRepository;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthUtils {
    UserRepository userRepository;
    private Authentication getAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }
    private String getUserEmail(){
        return getAuthentication().getName();
    }

    public static  String getUserCurrent(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null){
            return authentication.getName();
        }
        return null;
    }

}
