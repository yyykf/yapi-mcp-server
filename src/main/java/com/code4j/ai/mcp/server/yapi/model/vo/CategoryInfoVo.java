package com.code4j.ai.mcp.server.yapi.model.vo;

import com.code4j.ai.mcp.server.yapi.model.YapiResponse.CatInterfaceData;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.io.Serializable;
import java.util.List;

/**
 * @Description 接口分类信息
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:28
 */
public record CategoryInfoVo(
        @JsonPropertyDescription("接口分类ID")
        Long id,
        @JsonPropertyDescription("接口分类名称")
        String name,
        @JsonPropertyDescription("Yapi项目ID")
        Long projectId,
        @JsonPropertyDescription("接口分类描述")
        String desc,
        @JsonPropertyDescription("分类下的接口列表")
        List<CatInterfaceData> interfaces
) implements Serializable {

}