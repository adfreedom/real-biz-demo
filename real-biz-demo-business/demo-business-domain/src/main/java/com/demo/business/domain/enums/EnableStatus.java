package com.demo.business.domain.enums;

import com.demo.foundation.domain.base.BaseEnum;
import lombok.Getter;

/**
 * 启用状态。
 */
@Getter
public enum EnableStatus implements BaseEnum<String, String, String> {

    ENABLED("1", "启用"),
    DISABLED("0", "禁用");

    private final String code;
    private final String name;

    EnableStatus(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return code;
    }
}
