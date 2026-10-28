package com.demo.business.application.trade.settle;

import com.demo.foundation.domain.user.UserRequestContext;
import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.Payment;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.business.domain.trade.entity.SalesOrderEntity;
import com.demo.business.facade.trade.dto.SettleRequest;
import com.demo.business.facade.trade.dto.SettleResult;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 结算处理上下文：贯穿结算处理器链，承载请求、操作人上下文、合并支付组、已加载订单，
 * 以及处理器链准备的本次支付明细，并负责构建返回结果。
 */
@Data
public class SettleContext {

    /** 结算请求 */
    private SettleRequest request;
    /** 操作人上下文（由请求装配） */
    private UserRequestContext userRequestContext;
    /** 合并支付组 */
    private MergePayGroup group;
    /** 已加载的销售订单（结算对象） */
    private List<SalesOrder> lastOrderList;
    /** 处理器链准备的本次支付明细 */
    private List<Payment> newPaymentList;

    public SettleContext(MergePayGroup group, List<SalesOrder> lastOrderList, SettleRequest request) {
        this.group = group;
        this.lastOrderList = lastOrderList;
        this.request = request;
        UserRequestContext context = new UserRequestContext();
        context.setUserId(request.getUserId());
        this.userRequestContext = context;
    }

    /**
     * 构建结算返回结果：回显单号、支付与完成状态、金额。
     */
    public List<SettleResult> buildResults() {
        return lastOrderList.stream().map(order -> {
            SalesOrderEntity entity = order.getSalesOrderEntity();
            SettleResult result = new SettleResult();
            result.setOrderNo(entity.getOrderNo());
            result.setChannel(entity.getChannel());
            result.setSettled(entity.isSettled());
            result.setCompleted(entity.isCompleted());
            result.setSettledTime(entity.getSettledTime());
            result.setCompletedTime(entity.getCompletedTime());
            result.setTotalAmount(entity.getTotalAmount());
            result.setPaidAmount(entity.getPaidAmount());
            return result;
        }).collect(Collectors.toList());
    }
}
