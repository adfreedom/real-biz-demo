# 3.1 下单场景

> 预计讲解时长：~8 分钟
> 部分：三、串联场景

4 个 processor 编排完成下单，按 sort 顺序执行。

![下单 processor 编排](../images/place-order-pipeline.png)

## processor 与职责

| sort | processor | 职责 |
|------|-----------|------|
| 1 | OrderNoGenerator | 生成单号（UUID） |
| 10 | ItemValidation | 商品校验 |
| 50 | PriceCalc | 价格计算 |
| 90 | Payment | 支付信息装配 |

## 要点

- `TradeActionExecutor` 用 TreeSet 构造时排序，不每次重新排序
- 事务只包 `repository.saveChanges`，processor 耗时操作不进事务
- 单号生成策略在 processor，但下推到子实体的逻辑内聚到 `SalesOrder.assignOrderNo()`

## 截图建议

- 4 个 processor 的类声明 + sort 值
- `TradeActionExecutor` 编排执行代码
- `SalesOrder.assignOrderNo()` 内聚方法

---

下一节 → [3.2 结算场景](./02-settle.md)
