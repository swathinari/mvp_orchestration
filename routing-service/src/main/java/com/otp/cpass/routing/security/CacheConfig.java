package com.swathi.cpass.routing.security;
import java.time.Duration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
@Configuration @EnableCaching public class CacheConfig {
 @Bean RedisCacheConfiguration cacheConfiguration(){return RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofSeconds(60));}
}