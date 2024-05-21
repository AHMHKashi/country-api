package com.example.countryapi.configuration;

import com.google.common.cache.CacheBuilder;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {
    @Value("${config.cache.ttl}")
    private Integer cacheTTL;

    @Override
    public CacheManager cacheManager() {
        ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {

            @Override
            @NonNull
            protected Cache createConcurrentMapCache(@NonNull final String name) {
                return new ConcurrentMapCache(
                        name,
                        CacheBuilder.newBuilder()
                                .expireAfterWrite(cacheTTL, TimeUnit.SECONDS)
                                .build()
                                .asMap(),
                        false
                );
            }
        };

        cacheManager.setCacheNames(Arrays.asList("countryInfos", "countriesList", "weatherInfos"));

        return cacheManager;
    }
}
