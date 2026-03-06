package com.example.management_demo.repository.cache;

import lombok.With;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@RedisHash("cacheLimit")
public record CacheLimit(
        @Id
        String key,
        String type,
        String code,
        @With Integer quantity,
        @TimeToLive
        Integer expired
) {
}
