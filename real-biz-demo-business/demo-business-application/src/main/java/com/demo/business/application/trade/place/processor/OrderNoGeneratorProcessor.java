package com.demo.business.application.trade.place.processor;

import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.place.PlaceOrderProcessor;
import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.SalesOrder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * 单号生成处理器（sort=1）：生成订单号与合并支付组号，并通过聚合方法下推。
 * demo 简化：使用 UUID 生成，原 PMS 使用 Redis 自增序列号生成器。
 */
@Component
public class OrderNoGeneratorProcessor implements PlaceOrderProcessor {

    @Override
    public void process(PlaceOrderContext context) {
        MergePayGroup group = context.getGroup();
        if (StringUtils.isBlank(group.getGroupNo())) {
            group.assignGroupNo(nextNo());
        }
        String groupNo = group.getGroupNo();
        for (SalesOrder order : context.getOrderList()) {
            order.assignOrderNo(nextNo(), groupNo);
        }
    }

    private String nextNo() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    @Override
    public int getSort() {
        return 1;
    }
}
