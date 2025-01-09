package com.doubleowner.revibe.global.config;

import at.favre.lib.crypto.bcrypt.BCrypt;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PasswordEncoder {
    public String encode(String rawPassword) {
        return BCrypt.withDefaults().hashToString(BCrypt.MIN_COST, rawPassword.toCharArray());
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        BCrypt.Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), encodedPassword);
        return result.verified;
    }
}