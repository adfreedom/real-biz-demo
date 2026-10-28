package com.demo.business.application.trade.service;

import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import com.demo.business.facade.trade.dto.PlaceOrderResult;
import com.demo.business.facade.trade.dto.SettleRequest;
import com.demo.business.facade.trade.dto.SettleResult;

import java.util.List;

/**
 * 交易应用服务：下单与结算统一编排入口。
 */
public interface TradeAppService {

    /**
     * 下单：装配聚合 → 处理器链（单号/校验/算价/支付）→ 加入合并支付组 → 事务内落库。
     */
    List<PlaceOrderResult> placeOrder(PlaceOrderRequest request);

    /**
     * 结算：加载合并组与订单 → 处理器链（校验/结算单准备）→ 合并结算与完成 → 事务内落库。
     */
    List<SettleResult> settle(SettleRequest request);
}
