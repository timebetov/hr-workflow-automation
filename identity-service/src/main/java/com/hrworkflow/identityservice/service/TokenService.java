package com.hrworkflow.identityservice.service;

import com.hrworkflow.common.exceptions.CustomServiceException;
import com.hrworkflow.identityservice.dto.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@RequiredArgsConstructor
@Slf4j
public class TokenService {

    private static final int MAX_TOKENS = 3;
    private final RedisTemplate<String, Token> redisTemplate;

    public Token save(Token token) {

        String userKey = "user:" + token.getUserId() + ":tokens";

        List<Token> oldTokens = redisTemplate.opsForList().range(userKey, MAX_TOKENS - 1, -1);

        redisTemplate.opsForList().leftPush(userKey, token);
        redisTemplate.opsForList().trim(userKey, 0, MAX_TOKENS - 1);

        if (oldTokens != null) {
            for (Token oldToken : oldTokens) {
                redisTemplate.delete(oldToken.getValue());
            }
        }

        long expirationTimeInSeconds = (token.getExpiresAt().getTime() - System.currentTimeMillis()) / 1000;
        redisTemplate.opsForValue().set(token.getValue(), token, expirationTimeInSeconds, TimeUnit.SECONDS);
        return token;
    }

    public Token get(String token) {

        Token storedToken = redisTemplate.opsForValue().get(token);
        if (storedToken == null) {
            throw new CustomServiceException("Token not found: " + token);
        }
        return storedToken;
    }

    public void evict(String token) {

        Token storedToken = redisTemplate.opsForValue().get(token);
        if (storedToken != null) {
            String userKey = "user:" + storedToken.getUserId() + ":tokens";

            redisTemplate.delete(token);
            redisTemplate.opsForList().remove(userKey, 1, storedToken);
        }
    }

    public void evictAllForUser(Long userId) {

        String userKey = "user:" + userId + ":tokens";

        List<Token> tokens = redisTemplate.opsForList().range(userKey, 0, -1);
        if (tokens != null) {
            for (Token token : tokens) {
                redisTemplate.delete(token.getValue());
            }
        }

        redisTemplate.delete(userKey);
    }
}
