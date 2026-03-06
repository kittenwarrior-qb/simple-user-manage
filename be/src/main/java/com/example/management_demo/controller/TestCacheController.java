package com.example.management_demo.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
public class TestCacheController {

    private final RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/redis")
    public String testRedis() {
        redisTemplate.opsForValue().set("test-key", "Hello Redis at " + LocalDateTime.now());
        Object value = redisTemplate.opsForValue().get("test-key");
        return "Redis test: " + value;
    }

    @GetMapping("/cache")
    @Cacheable(value = "test-cache", key = "'test'")
    public String testCache() {
        return "Cached at: " + LocalDateTime.now();
    }
}
