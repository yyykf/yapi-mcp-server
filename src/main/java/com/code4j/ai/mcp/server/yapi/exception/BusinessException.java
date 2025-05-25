package com.code4j.ai.mcp.server.yapi.exception;

import lombok.*;

/**
 * @Description
 * @Author KaiFan Yu
 * @Date 2025/4/13 17:00
 */
@Getter
@Setter
@NoArgsConstructor
public class BusinessException extends RuntimeException {

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }

    protected BusinessException(String message, Throwable cause, boolean enableSuppression,
            boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
