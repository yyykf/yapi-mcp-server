package com.code4j.ai.mcp.server.yapi.service;

import com.code4j.ai.mcp.server.yapi.assembler.YapiInterfaceMapper;
import com.code4j.ai.mcp.server.yapi.client.YapiClient;
import com.code4j.ai.mcp.server.yapi.config.YapiProperties;
import com.code4j.ai.mcp.server.yapi.exception.BusinessException;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.InterfaceListItem;
import com.code4j.ai.mcp.server.yapi.model.vo.*;
import jakarta.annotation.Resource;
import java.util.*;
import java.util.concurrent.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.*;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/22 23:30
 */
@Slf4j
@Service
public class YapiService {

    private final YapiClient yapiClient;
    private final YapiProperties yapiProperties;
    private final YapiInterfaceMapper yapiInterfaceMapper;
    private final YapiCacheService cacheService;

    @Resource(name = "yapiExecutorService")
    private ExecutorService executorService;

    public YapiService(YapiClient yapiClient,
            YapiProperties yapiProperties,
            YapiInterfaceMapper yapiInterfaceMapper,
            YapiCacheService cacheService) {
        this.yapiClient = yapiClient;
        this.yapiProperties = yapiProperties;
        this.yapiInterfaceMapper = yapiInterfaceMapper;
        this.cacheService = cacheService;
    }

    /**
     * 应用启动完成后进行缓存预热
     */
    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (yapiProperties.getCache().isWarmUpOnStartup()) {
            // 异步预热缓存，避免阻塞应用启动
            executorService.submit(this::warmUpCache);
        }
    }

    /**
     * 缓存预热
     */
    private void warmUpCache() {
        if (!yapiProperties.getCache().isEnabled()) {
            return;
        }

        log.info("开始预热缓存...");
        try {
            // 预热项目列表
            listProjects();

            // 预热所有项目的分类信息
            yapiProperties.getProjectTokens().keySet().forEach(projectId -> {
                try {
                    listCategories(projectId);
                } catch (Exception e) {
                    log.warn("预热项目 {} 的分类信息失败", projectId, e);
                }
            });

            log.info("缓存预热完成");
        } catch (Exception e) {
            log.error("缓存预热失败", e);
        }
    }

    @Tool(name = "listCategories", description = "根据Yapi项目ID查询接口分类列表")
    public List<CategoryInfoVo> listCategories(@ToolParam(description = "Yapi项目ID") Long projectId) {
        String cacheKey = "categories_" + projectId;

        return cacheService.getOrCompute(cacheKey, List.class, () -> {
            String token = yapiProperties.getProjectTokens().get(projectId);
            if (token == null) {
                throw new BusinessException("未找到项目ID为 " + projectId + " 的token配置");
            }

            YapiResponse<List<YapiResponse.CatMenuData>> response = yapiClient.getCatMenu(projectId, token);
            if (response == null || response.errcode() != 0 || response.data() == null) {
                throw new BusinessException(
                        "获取接口分类列表失败: " + (response != null ? response.errmsg() : "未知错误"));
            }

            List<CompletableFuture<CategoryInfoVo>> futures = response.data().stream()
                    .map(catMenuData -> CompletableFuture.supplyAsync(() -> {
                        List<YapiResponse.CatInterfaceData> interfaces = null;
                        try {
                            // 默认获取所有接口，limit设置为足够大的数字
                            YapiResponse<YapiResponse.CatInterfaceList> interfaceListResponse = yapiClient.listCatInterfaces(
                                    token, catMenuData.id(), 1, 1000);
                            if (interfaceListResponse != null && interfaceListResponse.errcode() == 0
                                    && interfaceListResponse.data() != null) {
                                interfaces = interfaceListResponse.data().list();
                            }
                        } catch (Exception e) {
                            // 即使获取接口列表失败，也不影响分类的正常返回
                            log.error("[listCategories] get cat interfaces error.", e);
                        }
                        return new CategoryInfoVo(catMenuData.id(), catMenuData.name(), catMenuData.projectId(),
                                catMenuData.desc(), interfaces);
                    }, executorService))
                    .toList();

            return futures.stream()
                    // 等待所有异步任务完成
                    .map(CompletableFuture::join)
                    .toList();
        });
    }

    @Tool(name = "listProjects", description = "查看当前已经配置的Yapi项目列表，返回项目ID和项目名称")
    public List<ProjectInfoVo> listProjects() {
        String cacheKey = "projects";

        return cacheService.getOrCompute(cacheKey, List.class, () -> {
            Map<Long, String> projectTokens = yapiProperties.getProjectTokens();
            if (projectTokens == null || projectTokens.isEmpty()) {
                throw new BusinessException("未配置任何Yapi项目");
            }

            return projectTokens.entrySet().stream()
                    .map(entry -> {
                        Long projectId = entry.getKey();
                        String token = entry.getValue();
                        YapiResponse<YapiResponse.ProjectData> response = yapiClient.getProject(token);
                        if (response != null && response.errcode() == 0 && response.data() != null) {
                            return new ProjectInfoVo(projectId, response.data().name());
                        } else {
                            throw new BusinessException(
                                    "获取项目信息失败: " + (response != null ? response.errmsg() : "未知错误"));
                        }
                    })
                    .toList();
        });
    }

    @Tool(name = "listCatInterfaces", description = "根据Yapi分类ID和项目ID查询分类下的接口列表")
    public List<YapiResponse.CatInterfaceData> listCatInterfaces(
            @ToolParam(description = "Yapi分类ID") Long catId,
            @ToolParam(description = "Yapi项目ID") Long projectId,
            @ToolParam(description = "当前页面，默认为1") Integer page,
            @ToolParam(description = "每页数量，默认为10，如果不想要分页数据，可将 limit 设置为比较大的数字，比如 1000") Integer limit) {

        String cacheKey = String.format("cat_interfaces_%d_%d_%d_%d", catId, projectId, page, limit);

        return cacheService.getOrCompute(cacheKey, List.class, () -> {
            String token = yapiProperties.getProjectTokens().get(projectId);
            if (token == null) {
                throw new BusinessException("未找到项目ID为 " + projectId + " 的token配置");
            }

            YapiResponse<YapiResponse.CatInterfaceList> response = yapiClient.listCatInterfaces(token, catId, page,
                    limit);
            if (response != null && response.errcode() == 0 && response.data() != null) {
                return response.data().list();
            } else {
                throw new BusinessException(
                        "获取分类下接口列表失败: " + (response != null ? response.errmsg() : "未知错误"));
            }
        });
    }

    @Tool(name = "searchInterfaces", description = "根据项目名、关键字、接口路径模糊搜索API接口")
    public List<InterfaceSearchResultVo> searchInterfaces(
            @ToolParam(description = "项目名称（支持模糊搜索），可以为空") String projectName,
            @ToolParam(description = "关键字（在接口标题中搜索，支持模糊搜索），可以为空") String keyword,
            @ToolParam(description = "接口路径（支持模糊搜索），可以为空") String path) {

        Map<Long, String> projectTokens = yapiProperties.getProjectTokens();
        if (projectTokens == null || projectTokens.isEmpty()) {
            throw new BusinessException("未配置任何Yapi项目");
        }

        // 获取所有项目信息并根据项目名过滤
        List<ProjectInfoVo> allProjects = listProjects();
        List<ProjectInfoVo> filteredProjects = allProjects;

        if (projectName != null && !projectName.trim().isEmpty()) {
            String lowerProjectName = projectName.toLowerCase();
            filteredProjects = allProjects.stream()
                    .filter(project -> project.projectName().toLowerCase().contains(lowerProjectName))
                    .toList();
        }

        if (filteredProjects.isEmpty()) {
            return Collections.emptyList();
        }

        // 并行搜索所有匹配项目中的接口
        List<CompletableFuture<List<InterfaceSearchResultVo>>> futures = filteredProjects.stream()
                .map(project -> CompletableFuture.supplyAsync(() -> {
                    try {
                        String cacheKey = "interface_list_" + project.projectId();

                        // 从缓存获取或查询接口列表
                        YapiResponse.InterfaceListData interfaceListData = cacheService.getOrCompute(
                                cacheKey,
                                YapiResponse.InterfaceListData.class,
                                () -> {
                                    String token = projectTokens.get(project.projectId());
                                    YapiResponse<YapiResponse.InterfaceListData> response = yapiClient.listInterfaces(
                                            project.projectId(), token, 1, 2000);

                                    if (response == null || response.errcode() != 0 || response.data() == null) {
                                        log.warn("获取项目 {} 的接口列表失败: {}", project.projectName(),
                                                response != null ? response.errmsg() : "未知错误");
                                        return null;
                                    }
                                    return response.data();
                                });

                        if (interfaceListData == null || interfaceListData.list() == null) {
                            return Collections.<InterfaceSearchResultVo>emptyList();
                        }

                        // 在接口列表中进行模糊搜索
                        return interfaceListData.list().stream()
                                .filter(interfaceItem -> matchesSearchCriteria(interfaceItem, keyword, path))
                                .map(interfaceItem -> new InterfaceSearchResultVo(
                                        project.projectId(),
                                        project.projectName(),
                                        interfaceItem.id(),
                                        interfaceItem.title(),
                                        interfaceItem.path(),
                                        interfaceItem.method(),
                                        interfaceItem.catid(),
                                        interfaceItem.status()
                                ))
                                .toList();
                    } catch (Exception e) {
                        log.error("搜索项目 {} 的接口时发生错误", project.projectName(), e);
                        return Collections.<InterfaceSearchResultVo>emptyList();
                    }
                }, executorService))
                .toList();

        // 收集所有搜索结果
        return futures.stream()
                .map(CompletableFuture::join)
                .flatMap(List::stream)
                .toList();
    }

    /**
     * 检查接口是否匹配搜索条件
     */
    private boolean matchesSearchCriteria(InterfaceListItem interfaceItem, String keyword, String path) {
        // 关键字搜索（在标题中搜索）
        if (keyword != null && !keyword.trim().isEmpty()) {
            String lowerKeyword = keyword.toLowerCase();
            if (interfaceItem.title() == null ||
                    !interfaceItem.title().toLowerCase().contains(lowerKeyword)) {
                return false;
            }
        }

        // 路径搜索
        if (path != null && !path.trim().isEmpty()) {
            String lowerPath = path.toLowerCase();
            if (interfaceItem.path() == null ||
                    !interfaceItem.path().toLowerCase().contains(lowerPath)) {
                return false;
            }
        }

        return true;
    }

    @Tool(name = "getInterfaceDetail", description = "根据Yapi接口ID和项目ID查询接口详情")
    public YapiInterfaceDetailVo getInterfaceDetail(
            @ToolParam(description = "Yapi接口ID") Long interfaceId,
            @ToolParam(description = "Yapi项目ID，用于获取token") Long projectId) {

        String cacheKey = "interface_detail_" + interfaceId + "_" + projectId;

        return cacheService.getOrCompute(cacheKey, YapiInterfaceDetailVo.class, () -> {
            String token = yapiProperties.getProjectTokens().get(projectId);
            if (token == null) {
                throw new BusinessException("未找到项目ID为 " + projectId + " 的token配置");
            }

            YapiResponse<YapiResponse.InterfaceDetailData> response = yapiClient.getInterfaceDetail(interfaceId, token);
            if (response != null && response.errcode() == 0 && response.data() != null) {
                return yapiInterfaceMapper.toVo(response.data());
            } else {
                throw new BusinessException(
                        "获取接口详情失败: " + (response != null ? response.errmsg() : "未知错误"));
            }
        });
    }

    /**
     * 手动刷新缓存
     */
    @Tool(name = "refreshCache", description = "手动刷新Yapi缓存")
    public String refreshCache() {
        try {
            cacheService.clear();
            // 重新预热
            warmUpCache();
            return "缓存刷新成功";
        } catch (Exception e) {
            log.error("缓存刷新失败", e);
            return "缓存刷新失败: " + e.getMessage();
        }
    }

    /**
     * 清空指定项目的缓存
     */
    @Tool(name = "clearProjectCache", description = "清空指定项目的缓存")
    public String clearProjectCache(@ToolParam(description = "项目ID") Long projectId) {
        try {
            cacheService.clearByPrefix("categories_" + projectId);
            cacheService.clearByPrefix("interface_list_" + projectId);
            cacheService.clearByPrefix("interface_detail_" + projectId);
            return "项目 " + projectId + " 的缓存清空成功";
        } catch (Exception e) {
            log.error("清空项目缓存失败", e);
            return "清空项目缓存失败: " + e.getMessage();
        }
    }

    /**
     * 获取缓存统计信息
     */
    @Tool(name = "getCacheStats", description = "获取缓存统计信息")
    public String getCacheStats() {
        try {
            if (cacheService instanceof com.code4j.ai.mcp.server.yapi.service.impl.YapiCacheServiceImpl impl) {
                return impl.getCacheStats();
            }
            return "无法获取缓存统计信息";
        } catch (Exception e) {
            log.error("获取缓存统计失败", e);
            return "获取缓存统计失败: " + e.getMessage();
        }
    }
}
