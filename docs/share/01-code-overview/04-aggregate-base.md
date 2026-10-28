# 1.4 聚合根与基类

> 预计讲解时长：~2 分钟
> 部分：一、代码概览

关键代码截图：聚合根与实体基类的继承链。

![聚合根与基类](../images/aggregate-base.png)

## 要点

- 聚合根 `SalesOrder`：聚合一致性入口，所有写聚合字段的逻辑收敛于此
- `CreationAuditedEntity`：审计字段（创建人/时间等）收敛到基类，实体只声明业务字段
- 实体直接 extends `CreationAuditedEntity`，无中间层

## 截图建议

截 `SalesOrder` 类声明 + 一两个内聚方法（如 `recalcTotalFee`、`addBill`），以及 `CreationAuditedEntity` 的字段定义。

---

下一节 → [1.5 processor 编排入口](./05-action-executor.md)
