package com.code4j.ai.mcp.server.yapi.config;

import com.code4j.ai.mcp.server.yapi.model.YapiResponse;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.CatInterfaceData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.CatInterfaceList;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.CatMenuData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.InterfaceDetailData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.InterfaceListData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.InterfaceListItem;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.ProjectData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.ReqBodyFormParamData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.ReqHeaderParamData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.ReqPathParamData;
import com.code4j.ai.mcp.server.yapi.model.YapiResponse.ReqQueryParamData;
import com.code4j.ai.mcp.server.yapi.model.vo.CategoryInfoVo;
import com.code4j.ai.mcp.server.yapi.model.vo.InterfaceSearchResultVo;
import com.code4j.ai.mcp.server.yapi.model.vo.ProjectInfoVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.BasicInfo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.OtherInfo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.ReqBodyFormParamVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.ReqHeaderParamVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.ReqPathParamVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.ReqQueryParamVo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.RequestInfo;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo.ResponseInfo;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/27 21:06
 */
public class CustomRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(org.springframework.aot.hint.RuntimeHints hints, ClassLoader classLoader) {
        hints.serialization().registerType(YapiResponse.class);
        hints.serialization().registerType(CatMenuData.class);
        hints.serialization().registerType(ProjectData.class);
        hints.serialization().registerType(CatInterfaceList.class);
        hints.serialization().registerType(CatInterfaceData.class);
        hints.serialization().registerType(InterfaceListItem.class);
        hints.serialization().registerType(InterfaceListData.class);
        hints.serialization().registerType(InterfaceDetailData.class);
        hints.serialization().registerType(ReqBodyFormParamData.class);
        hints.serialization().registerType(ReqPathParamData.class);
        hints.serialization().registerType(ReqHeaderParamData.class);
        hints.serialization().registerType(ReqQueryParamData.class);
        hints.serialization().registerType(CategoryInfoVo.class);
        hints.serialization().registerType(ProjectInfoVo.class);
        hints.serialization().registerType(InterfaceSearchResultVo.class);
        hints.serialization().registerType(YapiInterfaceDetailVo.class);
        hints.serialization().registerType(BasicInfo.class);
        hints.serialization().registerType(RequestInfo.class);
        hints.serialization().registerType(ResponseInfo.class);
        hints.serialization().registerType(OtherInfo.class);
        hints.serialization().registerType(ReqBodyFormParamVo.class);
        hints.serialization().registerType(ReqPathParamVo.class);
        hints.serialization().registerType(ReqHeaderParamVo.class);
        hints.serialization().registerType(ReqQueryParamVo.class);
    }
}
