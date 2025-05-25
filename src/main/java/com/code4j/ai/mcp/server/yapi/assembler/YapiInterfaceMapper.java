package com.code4j.ai.mcp.server.yapi.assembler;

import com.code4j.ai.mcp.server.yapi.model.YapiResponse;
import com.code4j.ai.mcp.server.yapi.model.vo.YapiInterfaceDetailVo;
import org.mapstruct.*;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/5/25 16:12
 */
@Mapper(config = DefaultMapstructConfig.class)
public interface YapiInterfaceMapper {

    default YapiInterfaceDetailVo toVo(YapiResponse.InterfaceDetailData data) {
        if (data == null) {
            return null;
        }
        YapiInterfaceDetailVo.BasicInfo basicInfo = toBasicInfo(data);
        YapiInterfaceDetailVo.RequestInfo requestInfo = toRequestInfo(data);
        YapiInterfaceDetailVo.ResponseInfo responseInfo = toResponseInfo(data);
        YapiInterfaceDetailVo.OtherInfo otherInfo = toOtherInfo(data);
        return new YapiInterfaceDetailVo(basicInfo, requestInfo, responseInfo, otherInfo);
    }

    @Mapping(source = "catid", target = "catId")
    YapiInterfaceDetailVo.BasicInfo toBasicInfo(YapiResponse.InterfaceDetailData data);

    YapiInterfaceDetailVo.RequestInfo toRequestInfo(YapiResponse.InterfaceDetailData data);

    YapiInterfaceDetailVo.ResponseInfo toResponseInfo(YapiResponse.InterfaceDetailData data);

    YapiInterfaceDetailVo.OtherInfo toOtherInfo(YapiResponse.InterfaceDetailData data);
}
