package com.code4j.ai.mcp.server.yapi.config;

import com.code4j.ai.mcp.server.yapi.service.YapiService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.*;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:29
 */
@Configuration
public class ToolConfig {

    @Bean
    public ToolCallbackProvider yapiTools(YapiService yapiService) {
        return MethodToolCallbackProvider.builder().toolObjects(yapiService).build();
    }

}
