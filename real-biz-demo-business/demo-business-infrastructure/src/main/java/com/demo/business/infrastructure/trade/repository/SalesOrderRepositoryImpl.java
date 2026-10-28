package com.demo.business.infrastructure.trade.repository;

import com.demo.business.domain.trade.entity.Discount;
import com.demo.business.domain.trade.entity.Fee;
import com.demo.business.domain.trade.entity.OrderItem;
import com.demo.business.domain.trade.entity.Payment;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.business.domain.trade.entity.SalesOrderEntity;
import com.demo.business.domain.trade.repository.SalesOrderRepository;
import com.demo.business.infrastructure.trade.mapper.DiscountMapper;
import com.demo.business.infrastructure.trade.mapper.FeeMapper;
import com.demo.business.infrastructure.trade.mapper.OrderItemMapper;
import com.demo.business.infrastructure.trade.mapper.PaymentMapper;
import com.demo.business.infrastructure.trade.mapper.SalesOrderEntityMapper;
import com.demo.foundation.mybatis.extension.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 销售订单仓储实现：聚合差异落库与按单号加载。
 * 主表按单条增改删；各子集合按 isTransient/isModified/isDeleted 差量保存。
 * 查询返回的实体由 EntitySelectInterceptor 自动建立监控快照，无需仓储手动处理。
 */
@Repository
@RequiredArgsConstructor
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final SalesOrderEntityMapper salesOrderEntityMapper;
    private final OrderItemMapper orderItemMapper;
    private final DiscountMapper discountMapper;
    private final PaymentMapper paymentMapper;
    private final FeeMapper feeMapper;

    @Override
    public void saveChanges(List<SalesOrder> orders) {
        if (CollectionUtils.isEmpty(orders)) {
            return;
        }
        for (SalesOrder order : orders) {
            SalesOrderEntity entity = order.getSalesOrderEntity();
            RepositoryUtils.saveChanges(salesOrderEntityMapper, entity);
            RepositoryUtils.saveChanges(orderItemMapper, order.getItemList());
            RepositoryUtils.saveChanges(discountMapper, order.getDiscountList());
            RepositoryUtils.saveChanges(paymentMapper, order.getPaymentList());
            RepositoryUtils.saveChanges(feeMapper, order.getFeeList());
        }
    }

    @Override
    public List<SalesOrder> findByOrderNos(List<String> orderNos) {
        List<SalesOrder> result = new ArrayList<>();
        if (CollectionUtils.isEmpty(orderNos)) {
            return result;
        }
        for (String orderNo : orderNos) {
            SalesOrderEntity entity = salesOrderEntityMapper.selectOne(SalesOrderEntity::getOrderNo, orderNo);
            if (entity == null) {
                continue;
            }
            SalesOrder order = new SalesOrder();
            order.setSalesOrderEntity(entity);
            order.setItemList(orderItemMapper.selectList(OrderItem::getOrderNo, orderNo));
            order.setDiscountList(discountMapper.selectList(Discount::getOrderNo, orderNo));
            order.setPaymentList(paymentMapper.selectList(Payment::getOrderNo, orderNo));
            order.setFeeList(feeMapper.selectList(Fee::getOrderNo, orderNo));
            result.add(order);
        }
        return result;
    }
}
