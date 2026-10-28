# 1.5 processor 编排入口

> 预计讲解时长：~2 分钟
> 部分：一、代码概览

`TradeActionExecutor` —— 业务流程的编排核心。

![processor 编排入口](../images/action-executor.png)

## 要点

- processor 按 sort 顺序执行
- `TradeActionExecutor` 用 TreeSet 构造时排序，不每次重新排序
- 编排入口只负责调用，不写业务逻辑

## 截图建议

截 `TradeActionExecutor` 类 + processor 注册/执行的关键代码。

---

部分一结束 → 进入 [二、基础组件的优雅实现](../02-foundation-elegance/01-lock.md)
