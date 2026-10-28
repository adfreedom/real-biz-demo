package com.demo.business.domain.trade.entity;

import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * 合并支付组持久化数据。联结多张销售订单做合并结算。
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class MergePayGroupEntity extends CreationAuditedEntity {

    /** 合并支付组号 */
    private String groupNo;
    /** 组名 */
    private String groupName;
    /** 主单号（合并结算时以主单承载最终结算单） */
    private String primaryOrderNo;
    /** 是否为多单合并组 */
    private boolean mergeGroup;
    /** 操作人id */
    private String userId;
}
