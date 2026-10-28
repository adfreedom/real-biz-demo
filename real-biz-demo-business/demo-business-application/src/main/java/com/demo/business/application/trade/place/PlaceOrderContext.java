package com.demo.business.application.trade.place;

import com.demo.foundation.domain.user.UserRequestContext;
import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import com.demo.business.facade.trade.dto.PlaceOrderResult;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 下单处理上下文：贯穿下单处理器链，承载请求、操作人上下文、合并支付组与订单聚合，
 * 并负责构建返回结果。
 */
@Data
public class PlaceOrderContext {

    /** 下单请求 */
    private PlaceOrderRequest request;
    /** 操作人上下文 */
    private UserRequestContext userRequestContext;
    /** 合并支付组 */
    private MergePayGroup group;
    /** 销售订单列表 */
    private List<SalesOrder> orderList;

    /**
     * 构建下单返回结果。
     */
    public List<PlaceOrderResult> buildResults() {
        String groupNo = group.getMergePayGroupEntity().getGroupNo();
        List<PlaceOrderResult> results = new ArrayList<>();
        for (SalesOrder order : orderList) {
            PlaceOrderResult result = new PlaceOrderResult();
            result.setOrderNo(order.getOrderNo());
            result.setMergePayGroupId(groupNo);
            result.setChannel(order.getChannel());
            result.setOrderTime(order.getOrderTime());
            result.setRemark(order.getSalesOrderEntity().getRemark());
            result.setItemCount(order.getItemList() == null ? 0 : order.getItemList().size());
            result.setTotalAmount(order.getSalesOrderEntity().getTotalAmount());
            result.setPaidAmount(order.paidAmount());
            if (order.getItemList() != null) {
                result.setGoodsNames(order.getItemList().stream()
                        .map(i -> i.getGoodsName())
                        .collect(Collectors.toList()));
            }
            results.add(result);
        }
        return results;
    }
}
