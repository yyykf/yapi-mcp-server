package com.code4j.ai.mcp.server.yapi.model.vo;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * @Description 项目信息
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:28
 */
public record ProjectInfoVo(
        @JsonPropertyDescription("项目ID")
        Long projectId,
        @JsonPropertyDescription("项目名称")
        String projectName
) {

}