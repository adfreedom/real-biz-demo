package com.demo.business.application.trade.settle.processor;

import com.demo.business.application.trade.settle.SettleContext;
import com.demo.business.application.trade.settle.SettleProcessor;
import com.demo.business.domain.trade.entity.Payment;
import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import com.demo.foundation.domain.user.UserRequestContext;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * 支付明细准备处理器（sort=50）：将结算请求中的支付明细映射为支付实体并补全支付单号，
 * 装入 newPaymentList，供合并支付结算挂到主单。
 */
@Component
public class PaymentPreparationProcessor implements SettleProcessor {

    @Override
    public void process(SettleContext context) {
        UserRequestContext uc = context.getUserRequestContext();
        List<PlaceOrderRequest.PaymentDto> dtoList = context.getRequest().getPaymentList();
        List<Payment> newPaymentList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(dtoList)) {
            for (PlaceOrderRequest.PaymentDto dto : dtoList) {
                Payment payment = new Payment();
                payment.setPaymentType(dto.getPaymentType());
                payment.setPayMoney(dto.getMoney());
                payment.setRemark(dto.getRemark());
                payment.setUserId(uc.getUserId());
                if (StringUtils.isBlank(payment.getPayNo())) {
                    payment.setPayNo(UUID.randomUUID().toString().replace("-", ""));
                }
                newPaymentList.add(payment);
            }
        }
        context.setNewPaymentList(newPaymentList);
    }

    @Override
    public int getSort() {
        return 50;
    }
}
