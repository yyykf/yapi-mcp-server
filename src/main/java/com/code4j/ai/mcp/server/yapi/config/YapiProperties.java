package com.code4j.ai.mcp.server.yapi.config;

import java.util.Map;
import lombok.Data;
import org.springframework.boot.context.properties.*;
import org.springframework.context.annotation.Configuration;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:29
 */
@Configuration
@ConfigurationProperties(prefix = "yapi")
@Data
public class YapiProperties {

    private String baseUrl;
    private Map<Long, String> projectTokens;
    @NestedConfigurationProperty
    private ThreadPool threadPool = new ThreadPool();
    @NestedConfigurationProperty
    private Cache cache = new Cache();

    /**
     * 线程池配置
     */
    @Data
    public static class ThreadPool {

        /**
         * 核心线程数
         */
        private int coreSize = 5;

        /**
         * 最大线程数
         */
        private int maxSize = 10;

        /**
         * 队列容量
         */
        private int queueCapacity = 100;

        /**
         * 线程存活时间（秒）
         */
        private int keepAliveSeconds = 60;

        /**
         * 线程名称前缀
         */
        private String threadNamePrefix = "yapi-executor-";
    }

    /**
     * 缓存配置
     */
    @Data
    public static class Cache {

        /**
         * 是否启用缓存
         */
        private boolean enabled = true;

        /**
         * 内存缓存最大条目数
         */
        private int maxSize = 1000;

        /**
         * 缓存过期时间（分钟）
         */
        private int expireAfterWriteMinutes = 10;

        /**
         * 是否启动时预热缓存
         */
        private boolean warmUpOnStartup = true;
    }
}
