# 2.4 数据落库的极简处理

> 预计讲解时长：~5 分钟
> 部分：二、基础组件的优雅实现

`EntitySelectInterceptor` 在 `Executor.query` 层自动 monitor，diff-save 按 id 是否 transient 驱动 INSERT/UPDATE。

![数据落库自动 monitor](../images/entity-interceptor.png)

## 要点

- 业务代码不需要手动「标记已加载」
- 加载后 id 已回填 → UPDATE；新建 id=null → INSERT
- 聚合 `saveChanges` 时整树 diff，无需逐字段比对
- 监控发生在拦截器层，业务无感

## 截图建议

- `EntitySelectInterceptor` 的 query 拦截 + monitor 代码
- `Entity.isTransient()` 驱动 INSERT/UPDATE 的分支
- （可选）对比手动标记加载的繁琐写法

---

部分二结束 → 进入 [三、串联场景](../03-trade-scenarios/01-place-order.md)
