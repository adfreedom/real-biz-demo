package com.demo.business.domain.enums;

import com.demo.foundation.domain.base.BaseEnum;
import lombok.Getter;

/**
 * 支付方式。
 */
@Getter
public enum PaymentType implements BaseEnum<String, String, String> {

    ALIPAY("alipay", "支付宝"),
    WECHAT("wechat", "微信支付"),
    BALANCE("balance", "余额"),
    BANK_CARD("bankCard", "银行卡");

    private final String code;
    private final String name;

    PaymentType(String code, String name) {
        this.code = code;
        this.name = name;
    }

    @Override
    public String getValue() {
        return code;
    }
}
