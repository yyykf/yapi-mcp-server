package com.code4j.ai.mcp.server.yapi.assembler;

import org.mapstruct.*;
import org.mapstruct.MappingConstants.ComponentModel;

/**
 * @Description
 * @Author YuKaiFan
 * @Date 2025/4/13 15:56
 */
@MapperConfig(
        componentModel = ComponentModel.SPRING,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface DefaultMapstructConfig {

}
