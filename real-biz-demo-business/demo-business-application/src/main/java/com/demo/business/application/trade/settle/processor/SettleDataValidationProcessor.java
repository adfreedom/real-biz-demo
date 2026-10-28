package com.demo.business.application.trade.settle.processor;

import com.demo.business.application.trade.settle.SettleContext;
import com.demo.business.application.trade.settle.SettleProcessor;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.foundation.core.exception.BizException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 结算数据校验处理器（sort=10）：合并组存在、订单存在且未完成、且归属当前合并组。
 */
@Component
public class SettleDataValidationProcessor implements SettleProcessor {

    @Override
    public void process(SettleContext context) {
        if (context.getGroup() == null) {
            throw new BizException("未找到合并支付组");
        }
        List<SalesOrder> lastOrderList = context.getLastOrderList();
        if (CollectionUtils.isEmpty(lastOrderList)) {
            throw new BizException("未找到销售订单");
        }
        String groupNo = context.getGroup().getMergePayGroupEntity().getGroupNo();
        for (SalesOrder order : lastOrderList) {
            if (Boolean.TRUE.equals(order.isCompleted())) {
                throw new BizException(order.getOrderNo() + " 订单已完成，不允许重复结算");
            }
            if (!StringUtils.equals(order.getSalesOrderEntity().getMergePayGroupId(), groupNo)) {
                throw new BizException(order.getOrderNo() + " 订单不属于该合并组，不允许结算");
            }
        }
    }

    @Override
    public int getSort() {
        return 10;
    }
}
