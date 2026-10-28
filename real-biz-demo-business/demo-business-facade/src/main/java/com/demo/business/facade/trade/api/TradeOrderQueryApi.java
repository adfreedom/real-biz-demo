package com.demo.business.facade.trade.api;

import com.demo.business.facade.trade.dto.TradeOrderDto;
import com.demo.business.facade.trade.dto.TradeOrderQueryDto;
import com.demo.foundation.core.dto.PageResultDto;
import com.demo.foundation.core.dto.Result;
import com.demo.foundation.web.feign.InnerClientInterceptor;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 交易订单内部查询契约：供其他微服务通过 Feign 注入调用，
 * 路径统一挂在 inner 前缀下，区别于对外的 open 接口。
 */
@FeignClient(name = "demo-business-service", configuration = InnerClientInterceptor.class,
        contextId = "trade-order-query", path = "inner/trade/order")
public interface TradeOrderQueryApi {

    /**
     * 分页查询销售订单。
     */
    @PostMapping("list")
    Result<PageResultDto<TradeOrderDto>> list(@RequestBody TradeOrderQueryDto query);

    /**
     * 按订单号查订单详情。
     */
    @GetMapping("detail")
    Result<TradeOrderDto> detail(@RequestParam("orderNo") String orderNo);
}
