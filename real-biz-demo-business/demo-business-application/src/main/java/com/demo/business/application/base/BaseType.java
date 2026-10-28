package com.demo.business.application.base;

import com.demo.business.domain.enums.DiscountType;
import com.demo.business.domain.enums.EnableStatus;
import com.demo.business.domain.enums.FeeType;
import com.demo.business.domain.enums.PaymentType;
import com.demo.business.domain.enums.SalesChannel;
import com.demo.foundation.domain.base.BaseEnum;
import com.demo.foundation.domain.base.EnumUtils;
import lombok.Getter;

/**
 * 通用枚举字典类型注册表：每一项绑定一个枚举类，可选绑定一个 {@code EnumFilter} 用于按业务上下文裁剪可选项。
 * 前端通过 code 查询某类枚举的全部可选项，或一次性拉取全部。
 */
@Getter
public enum BaseType implements BaseEnum<String, String, String> {

    PAYMENT_TYPE("paymentType", "支付方式", PaymentType.class),
    DISCOUNT_TYPE("discountType", "优惠类型", DiscountType.class),
    FEE_TYPE("feeType", "费项类型", FeeType.class),
    SALES_CHANNEL("salesChannel", "销售渠道", SalesChannel.class),
    ENABLE_STATUS("enableStatus", "启用状态", EnableStatus.class),
    ;

    private final String code;
    private final String name;
    private final Class enumClass;
    private final Class filter;

    BaseType(String code, String name, Class enumClass) {
        this.code = code;
        this.name = name;
        this.enumClass = enumClass;
        this.filter = null;
    }

    BaseType(String code, String name, Class enumClass, Class filter) {
        this.code = code;
        this.name = name;
        this.enumClass = enumClass;
        this.filter = filter;
    }

    @Override
    public String getValue() {
        return getCode();
    }

    public static BaseType codeOf(String code) {
        return EnumUtils.codeOf(values(), code);
    }
}
