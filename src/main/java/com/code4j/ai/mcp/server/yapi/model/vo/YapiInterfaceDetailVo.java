package com.code4j.ai.mcp.server.yapi.model.vo;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import java.util.List;

/**
 * @Description 接口详情信息
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:28
 */
public record YapiInterfaceDetailVo(
        @JsonPropertyDescription("基本信息") BasicInfo basicInfo,
        @JsonPropertyDescription("请求参数信息") RequestInfo requestInfo,
        @JsonPropertyDescription("响应参数信息") ResponseInfo responseInfo,
        @JsonPropertyDescription("其他信息") OtherInfo otherInfo
) {

    public record BasicInfo(
            @JsonPropertyDescription("接口ID") Long id,
            @JsonPropertyDescription("接口标题") String title,
            @JsonPropertyDescription("接口路径") String path,
            @JsonPropertyDescription("请求方法") String method,
            @JsonPropertyDescription("所属项目ID") Long projectId,
            @JsonPropertyDescription("所属分类ID") Long catId
    ) {

    }

    public record RequestInfo(
            @JsonPropertyDescription("请求体类型 (raw, form, json)") String reqBodyType,
            @JsonPropertyDescription("请求体内容 (JSON Schema 或其他)") String reqBodyOther,
            @JsonPropertyDescription("请求体表单参数") List<ReqBodyFormParamVo> reqBodyForm,
            @JsonPropertyDescription("请求路径参数") List<ReqPathParamVo> reqParams,
            @JsonPropertyDescription("请求头") List<ReqHeaderParamVo> reqHeaders,
            @JsonPropertyDescription("请求查询参数") List<ReqQueryParamVo> reqQuery
    ) {

    }

    public record ResponseInfo(
            @JsonPropertyDescription("响应体类型 (json, raw)") String resBodyType,
            @JsonPropertyDescription("响应体内容 (JSON Schema 或其他)") String resBody,
            @JsonPropertyDescription("响应体是否为JSON Schema") boolean resBodyIsJsonSchema
    ) {

    }

    public record OtherInfo(
            @JsonPropertyDescription("接口状态 (undone, done, etc.)") String status,
            @JsonPropertyDescription("接口说明(Markdown)") String markdown
    ) {

    }

    public record ReqBodyFormParamVo(
            @JsonPropertyDescription("参数名称") String name,
            @JsonPropertyDescription("参数类型 (e.g., text, file)") String type,
            @JsonPropertyDescription("示例值") String example,
            @JsonPropertyDescription("参数描述") String desc,
            @JsonPropertyDescription("是否必须 (1:是, 0:否)") String required
    ) {

    }

    public record ReqPathParamVo(
            @JsonPropertyDescription("参数名称") String name,
            @JsonPropertyDescription("示例值") String example,
            @JsonPropertyDescription("参数描述") String desc
    ) {

    }

    public record ReqHeaderParamVo(
            @JsonPropertyDescription("参数名称") String name,
            @JsonPropertyDescription("参数值") String value,
            @JsonPropertyDescription("参数示例") String example,
            @JsonPropertyDescription("参数描述/备注") String desc,
            @JsonPropertyDescription("是否必须 (1:是, 0:否)") String required
    ) {

    }

    public record ReqQueryParamVo(
            @JsonPropertyDescription("参数名称") String name,
            @JsonPropertyDescription("示例值") String example,
            @JsonPropertyDescription("参数描述") String desc,
            @JsonPropertyDescription("是否必须 (1:是, 0:否)") String required
    ) {

    }
} 