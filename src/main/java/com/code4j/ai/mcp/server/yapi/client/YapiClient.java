package com.code4j.ai.mcp.server.yapi.client;

import com.code4j.ai.mcp.server.yapi.model.YapiResponse;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.CatMenuData;
import java.util.List;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:29
 */
@FeignClient(name = "yapiClient")
public interface YapiClient {

    /**
     * 获取项目下的接口分类
     *
     * @param projectId
     * @param token
     * @return
     */
    @GetMapping("/api/interface/getCatMenu")
    YapiResponse<List<CatMenuData>> getCatMenu(@RequestParam("project_id") Long projectId,
            @RequestParam("token") String token);

    /**
     * 获取项目信息
     *
     * @param token
     * @return
     */
    @GetMapping("/api/project/get")
    YapiResponse<YapiResponse.ProjectData> getProject(@RequestParam("token") String token);

    /**
     * 获取分类下的接口列表
     *
     * @param token
     * @param catid
     * @param page
     * @param limit
     * @return
     */
    @GetMapping("/api/interface/list_cat")
    YapiResponse<YapiResponse.CatInterfaceList> listCatInterfaces(@RequestParam("token") String token,
            @RequestParam("catid") Long catid,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "limit", required = false) Integer limit);

    /**
     * 获取接口列表数据
     *
     * @param projectId 项目ID
     * @param token     令牌
     * @param page      当前页数
     * @param limit     每页数量，默认为10
     * @return
     */
    @GetMapping("/api/interface/list")
    YapiResponse<YapiResponse.InterfaceListData> listInterfaces(@RequestParam("project_id") Long projectId,
            @RequestParam("token") String token,
            @RequestParam("page") Integer page,
            @RequestParam("limit") Integer limit);

    /**
     * 获取接口详情
     *
     * @param interfaceId
     * @param token
     * @return
     */
    @GetMapping("/api/interface/get")
    YapiResponse<YapiResponse.InterfaceDetailData> getInterfaceDetail(@RequestParam("id") Long interfaceId,
            @RequestParam("token") String token);
}