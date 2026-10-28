package com.demo.business.application.trade.place;

import com.demo.foundation.domain.user.UserRequestContext;
import com.demo.business.domain.trade.entity.Discount;
import com.demo.business.domain.trade.entity.Fee;
import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.MergePayGroupEntity;
import com.demo.business.domain.trade.entity.OrderItem;
import com.demo.business.domain.trade.entity.Payment;
import com.demo.business.domain.trade.entity.SalesOrder;
import com.demo.business.domain.trade.entity.SalesOrderEntity;
import com.demo.business.facade.trade.dto.PlaceOrderRequest;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 销售订单聚合装配工厂：由请求构建合并支付组与销售订单聚合（含商品行/优惠/支付）。
 */
public final class OrderFactory {

    private OrderFactory() {
    }

    public static PlaceOrderContext create(PlaceOrderRequest request) {
        LocalDateTime now = LocalDateTime.now();
        UserRequestContext userRequestContext = buildUserContext(request);
        MergePayGroup group = createGroup(request, now);
        SalesOrder order = new SalesOrder();
        order.setSalesOrderEntity(createEntity(request, now));
        order.setItemList(mapItems(request.getItemList()));
        order.setFeeList(mapFees(request.getFeeList()));
        order.setDiscountList(mapDiscounts(request.getDiscountList()));
        List<Payment> payments = mapPayments(request);
        if (!payments.isEmpty()) {
            order.addPayList(payments, false);
        }
        List<SalesOrder> orderList = new ArrayList<>();
        orderList.add(order);
        PlaceOrderContext context = new PlaceOrderContext();
        context.setRequest(request);
        context.setUserRequestContext(userRequestContext);
        context.setGroup(group);
        context.setOrderList(orderList);
        return context;
    }

    private static UserRequestContext buildUserContext(PlaceOrderRequest request) {
        UserRequestContext ctx = new UserRequestContext();
        ctx.setUserId(request.getUserId());
        return ctx;
    }

    private static MergePayGroup createGroup(PlaceOrderRequest request, LocalDateTime now) {
        MergePayGroupEntity entity = new MergePayGroupEntity();
        entity.setGroupName(request.getMergePayGroupName());
        entity.setMergeGroup(false);
        entity.setCreatedBy(request.getUserId());
        entity.setLastModifiedBy(request.getUserId());
        entity.setLastModifiedTime(now);
        MergePayGroup group = new MergePayGroup();
        group.setMergePayGroupEntity(entity);
        return group;
    }

    private static SalesOrderEntity createEntity(PlaceOrderRequest request, LocalDateTime now) {
        SalesOrderEntity entity = new SalesOrderEntity();
        entity.setChannel(request.getChannel());
        entity.setOrderTime(request.getOrderTime() != null ? request.getOrderTime() : now);
        entity.setRemark(request.getRemark());
        entity.setMemberId(request.getMemberId());
        entity.setMemberName(request.getMemberName());
        entity.setUserId(request.getUserId());
        entity.setCreatedBy(request.getUserId());
        entity.setLastModifiedBy(request.getUserId());
        entity.setLastModifiedTime(now);
        return entity;
    }

    private static List<OrderItem> mapItems(List<PlaceOrderRequest.ItemDto> itemList) {
        if (CollectionUtils.isEmpty(itemList)) {
            return new ArrayList<>();
        }
        return itemList.stream().map(i -> {
            OrderItem item = new OrderItem();
            item.setSkuId(i.getSkuId());
            item.setGoodsName(i.getGoodsName());
            item.setQuantity(i.getQuantity());
            item.setUnitPrice(i.getUnitPrice());
            return item;
        }).collect(Collectors.toList());
    }

    private static List<Discount> mapDiscounts(List<PlaceOrderRequest.DiscountDto> discountList) {
        if (CollectionUtils.isEmpty(discountList)) {
            return new ArrayList<>();
        }
        return discountList.stream().map(d -> {
            Discount discount = new Discount();
            discount.setType(d.getType());
            discount.setAmount(d.getAmount());
            discount.setReason(d.getReason());
            return discount;
        }).collect(Collectors.toList());
    }

    private static List<Fee> mapFees(List<PlaceOrderRequest.FeeDto> feeList) {
        if (CollectionUtils.isEmpty(feeList)) {
            return new ArrayList<>();
        }
        return feeList.stream().map(f -> {
            Fee fee = new Fee();
            fee.setType(f.getType());
            fee.setAmount(f.getAmount());
            fee.setRemark(f.getRemark());
            return fee;
        }).collect(Collectors.toList());
    }

    private static List<Payment> mapPayments(PlaceOrderRequest request) {
        if (CollectionUtils.isEmpty(request.getPaymentList())) {
            return new ArrayList<>();
        }
        return request.getPaymentList().stream().map(p -> {
            Payment payment = new Payment();
            payment.setPaymentType(p.getPaymentType());
            payment.setPayMoney(p.getMoney());
            payment.setRemark(p.getRemark());
            payment.setUserId(request.getUserId());
            return payment;
        }).collect(Collectors.toList());
    }
}
