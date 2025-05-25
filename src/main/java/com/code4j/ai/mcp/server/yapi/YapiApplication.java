package com.code4j.ai.mcp.server.yapi;

import com.code4j.ai.mcp.server.yapi.config.YapiProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:28
 */
@SpringBootApplication
@EnableFeignClients(basePackages = "com.code4j.ai.mcp.server.yapi.client")
@EnableConfigurationProperties(YapiProperties.class)
public class YapiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YapiApplication.class, args);
    }

}
