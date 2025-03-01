package org.example.ratingsystem.services;

import lombok.RequiredArgsConstructor;
import org.example.ratingsystem.exceptions.InvalidTokenException;
import org.example.ratingsystem.services.interfaces.EmailValidationService;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class RedisEmailValidationService implements EmailValidationService {

    final private StringRedisTemplate redisTemplate;

    @Override
    public void setToken(String token, String id) {
        redisTemplate.opsForValue().set(token, id, 24, TimeUnit.HOURS);
    }

    @Override
    public String getUserId(String token) {
        String id = redisTemplate.opsForValue().get(token);
        if (id == null) {
            throw new InvalidTokenException("Invalid token");
        }
        redisTemplate.delete(token);
        return id;
    }
}
