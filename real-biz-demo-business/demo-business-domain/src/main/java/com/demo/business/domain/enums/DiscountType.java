package com.demo.business.domain.enums;

import com.demo.foundation.domain.base.BaseEnum;
import lombok.Getter;

/**
 * 优惠类型。
 */
@Getter
public enum DiscountType implements BaseEnum<String, String, String> {

    FULL_REDUCTION("fullReduction", "满减"),
    COUPON("coupon", "优惠券"),
    POINTS("points", "积分抵扣");

    private final String code;
    private final String name;

    DiscountType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return code;
    }
}
