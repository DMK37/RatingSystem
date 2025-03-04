package org.example.ratingsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.exceptions.InvalidTokenException;
import org.example.ratingsystem.services.interfaces.ValidationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisValidationService implements ValidationService {

    final private StringRedisTemplate redisTemplate;

    @Override
    public void setEmailValidationToken(String token, String id) {
        redisTemplate.opsForValue().set("verify:" + token, id, 24, TimeUnit.HOURS);
    }

    @Override
    public String getUserIdFromValidationToken(String token) {
        String id = redisTemplate.opsForValue().get("verify:" + token);
        if (id == null) {
            throw new InvalidTokenException("Invalid token");
        }
        redisTemplate.delete(token);
        return id;
    }

    @Override
    public void setForgotPasswordToken(String token, String id) {
        redisTemplate.opsForValue().set("forget:" + token, id, 2, TimeUnit.HOURS);
    }

    @Override
    public String getUserIdFromForgotPasswordToken(String token) {
        String id = redisTemplate.opsForValue().get("forget:" + token);
        if (id == null) {
            throw new InvalidTokenException("Invalid token");
        }
        redisTemplate.delete(token);
        return id;
    }
}
