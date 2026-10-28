package com.demo.business.application.trade.service;

import com.demo.business.application.trade.TradeActionExecutor;
import com.demo.business.application.trade.place.OrderFactory;
import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.settle.SettleContext;
import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.business.domain.trade.repository.MergePayGroupRepository;
import com.demo.business.domain.trade.repository.SalesOrderRepository;
import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import com.demo.business.facade.trade.dto.PlaceOrderResult;
import com.demo.business.facade.trade.dto.SettleRequest;
import com.demo.business.facade.trade.dto.SettleResult;
import com.demo.foundation.lock.ConcurrencyFilter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.List;

/**
 * 交易应用服务实现：下单与结算统一编排。
 * 服务方法仅做聚合加载与编排，数据校验下沉到处理器链；事务仅包住落库，
 * 单号生成、合并、结算、金额校验等长耗时或可能抛业务异常的操作均放在事务外。
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TradeAppServiceImpl implements TradeAppService {

    private final TradeActionExecutor tradeActionExecutor;
    private final SalesOrderRepository salesOrderRepository;
    private final MergePayGroupRepository mergePayGroupRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    @ConcurrencyFilter(waitTime = 1000, leaseTime = 5000)
    public List<PlaceOrderResult> placeOrder(PlaceOrderRequest request) {
        PlaceOrderContext context = OrderFactory.create(request);
        // 事务外：单号生成 + 校验 + 算价 + 支付
        tradeActionExecutor.placeProcess(context);
        context.getGroup().joinGroup(context.getOrderList(), context.getUserRequestContext().getUserId());
        // 事务内：仅落库
        transactionTemplate.executeWithoutResult(status -> {
            mergePayGroupRepository.saveChanges(context.getGroup());
            salesOrderRepository.saveChanges(context.getOrderList());
        });
        return context.buildResults();
    }

    @Override
    @ConcurrencyFilter(waitTime = 1000, leaseTime = 5000)
    public List<SettleResult> settle(SettleRequest request) {
        List<SalesOrder> lastOrderList = salesOrderRepository.findByOrderNos(request.getOrderNoList());
        MergePayGroup group = mergePayGroupRepository.findByGroupNo(request.getMergePayGroupId());
        SettleContext context = new SettleContext(group, lastOrderList, request);
        // 事务外：结算处理链（校验 + 支付明细准备） + 合并支付结算（足额校验 + 逐单已支付 + 完成）
        tradeActionExecutor.settleProcess(context);
        group.complete(lastOrderList, context.getNewPaymentList(), context.getUserRequestContext());
        // 事务内：仅落库
        transactionTemplate.executeWithoutResult(status -> {
            mergePayGroupRepository.saveChanges(group);
            salesOrderRepository.saveChanges(lastOrderList);
        });
        return context.buildResults();
    }
}
