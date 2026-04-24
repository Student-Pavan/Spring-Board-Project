package com.nikhitha.gateway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LockService {

    private final StringRedisTemplate redisTemplate;

    public LockService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // 🔒 Horizontal cap (atomic INCR)
    public boolean allowBotReply(Long postId) {
        String key = "post:" + postId + ":bot_count";
        Long count = redisTemplate.opsForValue().increment(key);
        return count != null && count <= 100;
    }

    // 🔒 Cooldown (atomic SETNX + TTL)
    public boolean checkCooldown(Long botId, Long humanId) {
        String key = "cooldown:bot_" + botId + ":human_" + humanId;

        // Only sets if key does NOT exist, with TTL = 10 min
        Boolean success = redisTemplate.opsForValue()
                .setIfAbsent(key, "1", 10, TimeUnit.MINUTES);

        // true  -> allowed (first interaction)
        // false -> blocked (cooldown active)
        return Boolean.TRUE.equals(success);
    }
}