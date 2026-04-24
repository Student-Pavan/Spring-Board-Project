package com.nikhitha.gateway.service;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class NotificationScheduler {

    private final StringRedisTemplate redisTemplate;

    public NotificationScheduler(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // Runs every 5 minutes
    @Scheduled(fixedRate = 300000)
    public void processNotifications() {

        Set<String> keys = redisTemplate.keys("user:*:pending_notifs");

        if (keys == null || keys.isEmpty()) return;

        for (String key : keys) {

            Long size = redisTemplate.opsForList().size(key);

            if (size != null && size > 0) {
                System.out.println("Summarized Notification: " + size +
                        " new interactions on your posts");

                redisTemplate.delete(key);
            }
        }
    }
}