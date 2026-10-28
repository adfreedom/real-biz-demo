package com.demo.business.application.trade;

import com.demo.business.application.trade.place.PlaceOrderContext;
import com.demo.business.application.trade.place.PlaceOrderProcessor;
import com.demo.business.application.trade.settle.SettleContext;
import com.demo.business.application.trade.settle.SettleProcessor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * 交易动作执行器：收集下单、结算两类处理器。处理器实现 {@link Comparable}，
 * 构造时一次性装入 {@link TreeSet} 排定顺序，调用时直接按序执行，无需每次排序。
 */
@Service
public class TradeActionExecutor {

    private final Set<PlaceOrderProcessor> placeProcessors;
    private final Set<SettleProcessor> settleProcessors;

    public TradeActionExecutor(List<PlaceOrderProcessor> placeProcessors,
                               List<SettleProcessor> settleProcessors) {
        this.placeProcessors = new TreeSet<>(placeProcessors);
        this.settleProcessors = new TreeSet<>(settleProcessors);
    }

    /**
     * 执行下单处理链（按 sort 升序）。
     */
    public void placeProcess(PlaceOrderContext context) {
        placeProcessors.forEach(processor -> processor.process(context));
    }

    /**
     * 执行结算处理链（数据校验 + 结算单准备，按 sort 升序）。
     */
    public void settleProcess(SettleContext context) {
        settleProcessors.forEach(processor -> processor.process(context));
    }
}
