package com.demo.business.application.trade.place.processor;

import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.place.PlaceOrderProcessor;
import com.demo.business.domain.trade.entity.SalesOrder;
import org.springframework.stereotype.Component;

/**
 * 价格计算处理器（sort=50）：委托聚合重算订单总金额。
 */
@Component
public class PriceCalcProcessor implements PlaceOrderProcessor {

    @Override
    public void process(PlaceOrderContext context) {
        for (SalesOrder order : context.getOrderList()) {
            order.recalculateTotalAmount();
        }
    }

    @Override
    public int getSort() {
        return 50;
    }
}
