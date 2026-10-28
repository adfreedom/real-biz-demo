package com.demo.business.domain.enums;

import com.demo.foundation.domain.base.BaseEnum;
import lombok.Getter;

/**
 * 费项类型（订单总额构成项）。
 */
@Getter
public enum FeeType implements BaseEnum<String, String, String> {

    SHIPPING("shipping", "运费"),
    HANDLING("handling", "手续费"),
    PACKAGING("packaging", "包装费");

    private final String code;
    private final String name;

    FeeType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return code;
    }
}
