package com.demo.business.application.trade.place.processor;

import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.place.PlaceOrderProcessor;
import com.demo.business.domain.trade.entity.OrderItem;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.foundation.core.exception.BizException;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;

/**
 * 商品行校验处理器（sort=10）：校验商品行非空、数量与单价合法，并计算小计。
 */
@Component
public class ItemValidationProcessor implements PlaceOrderProcessor {

    @Override
    public void process(PlaceOrderContext context) {
        for (SalesOrder order : context.getOrderList()) {
            if (CollectionUtils.isEmpty(order.getItemList())) {
                throw new BizException("商品行不能为空");
            }
            for (OrderItem item : order.getItemList()) {
                if (item.getQuantity() == null || item.getQuantity() <= 0) {
                    throw new BizException(item.getGoodsName() + " 商品数量必须大于0");
                }
                if (item.getUnitPrice() == null || item.getUnitPrice().compareTo(BigDecimal.ZERO) < 0) {
                    throw new BizException(item.getGoodsName() + " 商品单价不能为负");
                }
                item.setSubtotal(item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())));
            }
        }
    }

    @Override
    public int getSort() {
        return 10;
    }
}
