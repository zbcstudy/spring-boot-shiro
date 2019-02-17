package com.wondertek.shiro.config.cache;

import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @Author zbc
 * @Date 13:25-2019/2/17
 */
@Configuration
public class ShiroSpringCacheConfig {

    @Bean
    public CacheManager shiroRedisCacheManger(RedisTemplate redisTemplate) {
        RedisCacheManager redisCacheManager = new RedisCacheManager(redisTemplate);
        //she
        redisCacheManager.setDefaultExpiration(10);
        return redisCacheManager;
    }
}
