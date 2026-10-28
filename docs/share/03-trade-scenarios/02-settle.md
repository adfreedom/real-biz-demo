# 3.2 结算场景

> 预计讲解时长：~8 分钟
> 部分：三、串联场景

2 个 processor + 聚合内聚完成结算。

![结算 processor 编排](../images/settle-pipeline.png)

## processor 与职责

| sort | processor | 职责 |
|------|-----------|------|
| 10 | SettleDataValidation | 结算数据校验 |
| 50 | SettlementPreparation | 结算单准备（编排调用聚合） |

## 聚合内聚下沉点

- `SalesOrder.recalcTotalFee()` —— 商品小计 − 优惠 + 费项，写回 totalFee
- `SalesOrder.newSettlementCarrier()` —— 构造结算单载体订单
- `SalesOrder.addBill()` —— 结账单生成
- `MergePayGroup.assignGroupNo()` —— 合并支付组单号下推

## 要点

- processor 退化为「编排调用」，不探入聚合内部字段
- 写聚合字段的逻辑全部在聚合内，保证一致性边界
- 对比下单场景，结算更突出「聚合内聚」规则

## 截图建议

- 2 个 processor 的类声明（显示退化为编排调用）
- 上述聚合内聚方法的代码
- `SalesOrder` 与 `MergePayGroup` 两个聚合的协作关系

---

下一节 → [3.3 串联回顾](./03-recap.md)
