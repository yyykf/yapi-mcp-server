package com.code4j.ai.mcp.server.yapi.service;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * @Description Yapi缓存服务接口
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:36
 */
public interface YapiCacheService {

    /**
     * 获取缓存值
     *
     * @param key  缓存键
     * @param type 值类型
     * @return 缓存值
     */
    <T> Optional<T> get(String key, Class<T> type);

    /**
     * 设置缓存值
     *
     * @param key   缓存键
     * @param value 缓存值
     */
    <T> void put(String key, T value);

    /**
     * 获取缓存值，如果不存在则执行supplier并缓存结果
     *
     * @param key      缓存键
     * @param type     值类型
     * @param supplier 数据提供者
     * @return 缓存值或新计算的值
     */
    <T> T getOrCompute(String key, Class<T> type, Supplier<T> supplier);

    /**
     * 删除缓存
     *
     * @param key 缓存键
     */
    void remove(String key);

    /**
     * 清空所有缓存
     */
    void clear();

    /**
     * 清空指定前缀的缓存
     *
     * @param keyPrefix 键前缀
     */
    void clearByPrefix(String keyPrefix);
}