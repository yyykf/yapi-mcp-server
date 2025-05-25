package com.code4j.ai.mcp.server.yapi.service.impl;

import com.code4j.ai.mcp.server.yapi.config.YapiProperties;
import com.code4j.ai.mcp.server.yapi.service.YapiCacheService;
import com.github.benmanes.caffeine.cache.*;
import jakarta.annotation.*;
import java.time.Duration;
import java.util.Optional;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @Description 基于Caffeine内存缓存的单层缓存策略
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:36
 */
@Slf4j
@Service
public class YapiCacheServiceImpl implements YapiCacheService {

    private final YapiProperties yapiProperties;
    private Cache<String, Object> memoryCache;

    public YapiCacheServiceImpl(YapiProperties yapiProperties) {
        this.yapiProperties = yapiProperties;
    }

    @PostConstruct
    public void init() {
        initMemoryCache();
        log.info("YapiCacheService 初始化完成");
    }

    private void initMemoryCache() {
        if (!yapiProperties.getCache().isEnabled()) {
            log.info("缓存已禁用");
            return;
        }

        this.memoryCache = Caffeine.newBuilder()
                .maximumSize(yapiProperties.getCache().getMaxSize())
                .expireAfterWrite(Duration.ofMinutes(yapiProperties.getCache().getExpireAfterWriteMinutes()))
                .build();

        log.info("内存缓存初始化完成，最大容量: {}, 过期时间: {} 分钟",
                yapiProperties.getCache().getMaxSize(),
                yapiProperties.getCache().getExpireAfterWriteMinutes());
    }

    @Override
    public <T> Optional<T> get(String key, Class<T> type) {
        if (!yapiProperties.getCache().isEnabled() || memoryCache == null) {
            return Optional.empty();
        }

        Object cached = memoryCache.getIfPresent(key);
        if (cached != null) {
            try {
                return Optional.of(type.cast(cached));
            } catch (ClassCastException e) {
                log.warn("内存缓存类型转换失败，key: {}", key, e);
                memoryCache.invalidate(key);
            }
        }

        return Optional.empty();
    }

    @Override
    public <T> void put(String key, T value) {
        if (!yapiProperties.getCache().isEnabled() || memoryCache == null) {
            return;
        }

        memoryCache.put(key, value);
    }

    @Override
    public <T> T getOrCompute(String key, Class<T> type, Supplier<T> supplier) {
        Optional<T> cached = get(key, type);
        if (cached.isPresent()) {
            log.debug("缓存命中: {}", key);
            return cached.get();
        }

        log.debug("缓存未命中，执行计算: {}", key);
        T value = supplier.get();
        if (value != null) {
            put(key, value);
        }
        return value;
    }

    @Override
    public void remove(String key) {
        if (memoryCache != null) {
            memoryCache.invalidate(key);
            log.debug("移除缓存: {}", key);
        }
    }

    @Override
    public void clear() {
        if (memoryCache != null) {
            memoryCache.invalidateAll();
            log.info("已清空所有缓存");
        }
    }

    @Override
    public void clearByPrefix(String keyPrefix) {
        if (memoryCache != null) {
            long removedCount = memoryCache.asMap().keySet().stream()
                    .filter(key -> key.startsWith(keyPrefix))
                    .peek(memoryCache::invalidate)
                    .count();
            log.info("清空前缀为 '{}' 的缓存，共 {} 项", keyPrefix, removedCount);
        }
    }

    /**
     * 获取缓存统计信息
     */
    public String getCacheStats() {
        if (memoryCache == null) {
            return "缓存未启用";
        }

        return String.format("缓存统计 - 大小: %d, 命中率: %.2f%%",
                memoryCache.estimatedSize(),
                memoryCache.stats().hitRate() * 100);
    }

    @PreDestroy
    public void destroy() {
        log.info("YapiCacheService 正在关闭...");
        if (memoryCache != null) {
            log.info("最终缓存统计: {}", getCacheStats());
        }
    }
} 