package com.demo.business.application.trade.place.processor;

import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.place.PlaceOrderProcessor;
import com.demo.business.domain.trade.entity.Payment;
import com.demo.business.domain.trade.entity.SalesOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 支付明细处理器（sort=90）：为缺失支付单号的支付记录补生成单号。
 */
@Component
public class PaymentProcessor implements PlaceOrderProcessor {

    @Override
    public void process(PlaceOrderContext context) {
        for (SalesOrder order : context.getOrderList()) {
            for (Payment payment : order.getPaymentList()) {
                if (StringUtils.isBlank(payment.getPayNo())) {
                    payment.setPayNo(UUID.randomUUID().toString().replace("-", ""));
                }
            }
        }
    }

    @Override
    public int getSort() {
        return 90;
    }
}
