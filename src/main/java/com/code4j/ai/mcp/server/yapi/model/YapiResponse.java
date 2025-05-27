package com.code4j.ai.mcp.server.yapi.model;

import com.fasterxml.jackson.annotation.*;
import java.io.Serializable;
import java.util.List;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:28
 */
@JsonClassDescription("Yapi通用响应结构")
public record YapiResponse<T>(
        Integer errcode,
        String errmsg,
        @JsonPropertyDescription("响应数据")
        T data
) implements Serializable {

    @JsonClassDescription("接口分类数据")
    public record CatMenuData(
            @JsonProperty("_id")
            Long id,
            @JsonPropertyDescription("接口分类名称")
            String name,
            @JsonPropertyDescription("Yapi项目ID")
            @JsonProperty("project_id")
            Long projectId,
            @JsonPropertyDescription("接口分类描述")
            String desc
    ) implements Serializable {

    }

    @JsonClassDescription("项目数据")
    public record ProjectData(
            @JsonProperty("_id")
            Long id,
            @JsonPropertyDescription("项目名称")
            String name,
            @JsonProperty("project_type")
            String projectType,
            Long uid,
            @JsonProperty("group_id")
            Long groupId,
            @JsonProperty("add_time")
            Long addTime,
            @JsonProperty("up_time")
            Long upTime
    ) implements Serializable {

    }

    @JsonClassDescription("分类下接口列表数据")
    public record CatInterfaceList(
            Integer count,
            Integer total,
            List<CatInterfaceData> list
    ) implements Serializable {

    }

    @JsonClassDescription("分类下接口数据")
    public record CatInterfaceData(
            @JsonProperty("_id")
            Long id,
            String title
    ) implements Serializable {

    }

    @JsonClassDescription("接口列表项数据")
    public record InterfaceListItem(
            @JsonProperty("_id")
            Long id,
            @JsonProperty("project_id")
            Long projectId,
            Long catid,
            String title,
            String path,
            String method,
            Long uid,
            @JsonProperty("add_time")
            Long addTime,
            @JsonProperty("up_time")
            Long upTime,
            String status,
            @JsonProperty("edit_uid")
            Long editUid,
            @JsonProperty("api_opened")
            Boolean apiOpened,
            List<String> tag
    ) implements Serializable {

    }

    @JsonClassDescription("接口列表数据")
    public record InterfaceListData(
            Integer count,
            Integer total,
            List<InterfaceListItem> list
    ) implements Serializable {

    }

    @JsonClassDescription("接口详情数据")
    public record InterfaceDetailData(
            @JsonProperty("_id")
            Long id,
            @JsonProperty("project_id")
            Long projectId,
            Long catid,
            String title,
            String path,
            String method,
            @JsonProperty("req_body_type")
            String reqBodyType,
            @JsonProperty("res_body")
            String resBody,
            @JsonProperty("res_body_type")
            String resBodyType,
            Long uid,
            @JsonProperty("add_time")
            Long addTime,
            @JsonProperty("up_time")
            Long upTime,
            @JsonProperty("req_body_form")
            List<ReqBodyFormParamData> reqBodyForm,
            @JsonProperty("req_params")
            List<ReqPathParamData> reqParams,
            @JsonProperty("req_headers")
            List<ReqHeaderParamData> reqHeaders,
            @JsonProperty("req_query")
            List<ReqQueryParamData> reqQuery,
            String status,
            @JsonProperty("edit_uid")
            Long editUid,
            @JsonProperty("res_body_is_json_schema")
            boolean resBodyIsJsonSchema,
            @JsonProperty("req_body_other")
            String reqBodyOther,
            String username,
            String markdown
    ) implements Serializable {

    }

    public record ReqBodyFormParamData(
            @JsonProperty("_id") String id,
            String name,
            String type,
            String example,
            String desc,
            String required
    ) implements Serializable {

    }

    public record ReqPathParamData(
            @JsonProperty("_id") String id,
            String name,
            String example,
            String desc
    ) implements Serializable {

    }

    public record ReqHeaderParamData(
            @JsonProperty("_id") String id,
            String name,
            String value,
            String example,
            String desc,
            String required
    ) implements Serializable {

    }

    public record ReqQueryParamData(
            @JsonProperty("_id") String id,
            String name,
            String example,
            String desc,
            String required
    ) implements Serializable {

    }
}
