package com.demo.business.domain.enums;

import com.demo.foundation.domain.base.BaseEnum;
import lombok.Getter;

/**
 * 销售渠道。
 */
@Getter
public enum SalesChannel implements BaseEnum<String, String, String> {

    WEB("web", "官网"),
    APP("app", "APP"),
    MINIAPP("miniapp", "小程序");

    private final String code;
    private final String name;

    SalesChannel(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return code;
    }
}
