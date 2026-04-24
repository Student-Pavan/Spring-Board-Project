package com.nikhitha.gateway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class NotificationService {

    private final StringRedisTemplate redisTemplate;

    public NotificationService(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void handleNotification(Long userId, String message) {

        String cooldownKey = "notif:cooldown:user_" + userId;
        String listKey = "user:" + userId + ":pending_notifs";

        Boolean exists = redisTemplate.hasKey(cooldownKey);

        if (Boolean.TRUE.equals(exists)) {
            // 🔥 Store for batching
            redisTemplate.opsForList().rightPush(listKey, message);
        } else {
            // 🚀 Send immediately
            System.out.println("Push Notification Sent to User: " + message);

            // ⏳ Set cooldown 15 min
            redisTemplate.opsForValue().set(cooldownKey, "1", 15, TimeUnit.MINUTES);
        }
    }
}