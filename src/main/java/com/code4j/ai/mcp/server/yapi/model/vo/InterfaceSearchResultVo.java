package com.code4j.ai.mcp.server.yapi.model.vo;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

/**
 * @Description 接口搜索结果
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:28
 */
public record InterfaceSearchResultVo(
        @JsonPropertyDescription("项目ID")
        Long projectId,
        @JsonPropertyDescription("项目名称")
        String projectName,
        @JsonPropertyDescription("接口ID")
        Long interfaceId,
        @JsonPropertyDescription("接口标题")
        String title,
        @JsonPropertyDescription("接口路径")
        String path,
        @JsonPropertyDescription("HTTP方法")
        String method,
        @JsonPropertyDescription("分类ID")
        Long catid,
        @JsonPropertyDescription("接口状态")
        String status
) {

}