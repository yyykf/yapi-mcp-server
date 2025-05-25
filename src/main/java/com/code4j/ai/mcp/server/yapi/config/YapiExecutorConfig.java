package com.code4j.ai.mcp.server.yapi.config;

import cn.hutool.core.thread.ThreadFactoryBuilder;
import jakarta.annotation.PreDestroy;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.*;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:23
 */
@Slf4j
@Configuration
public class YapiExecutorConfig {

    private ExecutorService executorService;

    /**
     * 创建Yapi专用的线程池
     *
     * @param yapiProperties Yapi配置
     * @return ExecutorService
     */
    @Bean(name = "yapiExecutorService")
    public ExecutorService yapiExecutorService(YapiProperties yapiProperties) {
        YapiProperties.ThreadPool config = yapiProperties.getThreadPool();

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                config.getCoreSize(),
                config.getMaxSize(),
                config.getKeepAliveSeconds(),
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(config.getQueueCapacity()),
                ThreadFactoryBuilder.create().setNamePrefix(config.getThreadNamePrefix()).setDaemon(true).build(),
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        // 允许核心线程超时
        executor.allowCoreThreadTimeOut(true);

        log.info("Yapi线程池初始化完成: 核心线程数={}, 最大线程数={}, 队列容量={}, 存活时间={}秒",
                config.getCoreSize(), config.getMaxSize(), config.getQueueCapacity(), config.getKeepAliveSeconds());

        this.executorService = executor;
        return executor;
    }

    /**
     * 优雅关闭线程池
     */
    @PreDestroy
    public void shutdown() {
        if (executorService != null) {
            log.info("开始关闭Yapi线程池...");

            // 禁止提交新任务
            executorService.shutdown();

            try {
                // 等待已提交的任务完成
                if (!executorService.awaitTermination(10, TimeUnit.SECONDS)) {
                    log.warn("线程池在10秒内未能正常关闭，尝试强制关闭");
                    // 强制关闭
                    executorService.shutdownNow();

                    // 再等待一段时间
                    if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                        log.error("线程池强制关闭失败");
                    } else {
                        log.info("线程池强制关闭成功");
                    }
                } else {
                    log.info("Yapi线程池已正常关闭");
                }
            } catch (InterruptedException e) {
                log.warn("线程池关闭过程中被中断");
                // 恢复中断状态
                Thread.currentThread().interrupt();
                // 强制关闭
                executorService.shutdownNow();
            }
        }
    }
}