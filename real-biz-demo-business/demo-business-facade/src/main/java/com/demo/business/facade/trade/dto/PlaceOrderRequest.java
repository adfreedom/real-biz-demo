package com.demo.business.facade.trade.dto;

import com.demo.foundation.core.locker.ConcurrentKey;
import com.demo.foundation.domain.user.IUserContext;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 下单请求。承载操作人上下文（由网关头经请求体参数解析器回填）与订单明细。
 */
@Data
public class PlaceOrderRequest implements IUserContext, ConcurrentKey {

    /** 操作人ID */
    private String userId;
    /** 请求来源 */
    private String requestSource;

    /** 渠道 */
    private String channel;
    /** 下单时间 */
    private LocalDateTime orderTime;
    /** 备注 */
    private String remark;

    /** 会员ID */
    private String memberId;
    /** 会员姓名 */
    private String memberName;

    /** 商品行 */
    private List<ItemDto> itemList;
    /** 费项（运费/手续费等） */
    private List<FeeDto> feeList;
    /** 优惠 */
    private List<DiscountDto> discountList;
    /** 支付明细 */
    private List<PaymentDto> paymentList;

    /** 合并支付组名称（可选，提供时自动建组） */
    private String mergePayGroupName;

    @Override
    public String getConcurrentKey() {
        return memberId != null ? memberId : String.valueOf(hashCode());
    }

    @Data
    public static class ItemDto {
        private String skuId;
        private String goodsName;
        private Integer quantity;
        private java.math.BigDecimal unitPrice;
    }

    @Data
    public static class PaymentDto {
        private String paymentType;
        private java.math.BigDecimal money;
        private String remark;
    }

    @Data
    public static class DiscountDto {
        private String type;
        private java.math.BigDecimal amount;
        private String reason;
    }

    @Data
    public static class FeeDto {
        private String type;
        private java.math.BigDecimal amount;
        private String remark;
    }
}
