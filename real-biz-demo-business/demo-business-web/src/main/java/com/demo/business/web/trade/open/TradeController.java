package com.demo.business.web.trade.open;

import com.demo.business.application.trade.service.TradeAppService;
import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import com.demo.business.facade.trade.dto.PlaceOrderResult;
import com.demo.business.facade.trade.dto.SettleRequest;
import com.demo.business.facade.trade.dto.SettleResult;
import com.demo.foundation.core.dto.Result;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 交易控制器：下单与结算接口聚合，统一挂在 open 前缀下对外暴露。
 */
@RestController
@RequestMapping("open/trade")
@RequiredArgsConstructor
public class TradeController {

    private final TradeAppService tradeAppService;

    /**
     * 下单。
     */
    @PostMapping("place-order")
    public Result<List<PlaceOrderResult>> placeOrder(@RequestBody PlaceOrderRequest request) {
        return Result.success(tradeAppService.placeOrder(request));
    }

    /**
     * 结算（合并支付结算 + 完成）。
     */
    @PostMapping("settle")
    public Result<List<SettleResult>> settle(@RequestBody SettleRequest request) {
        return Result.success(tradeAppService.settle(request));
    }
}
