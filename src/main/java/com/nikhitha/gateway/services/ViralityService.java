package com.nikhitha.gateway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class ViralityService {

    private final StringRedisTemplate redisTemplate;

    public ViralityService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void updateScore(Long postId, String actionType) {

        String key = "post:" + postId + ":virality_score";

        int points = switch (actionType) {
            case "BOT_REPLY" -> 1;
            case "HUMAN_LIKE" -> 20;
            case "HUMAN_COMMENT" -> 50;
            default -> 0;
        };

        redisTemplate.opsForValue().increment(key, points);
    }

    public String getScore(Long postId) {
        return redisTemplate.opsForValue().get("post:" + postId + ":virality_score");
    }
}