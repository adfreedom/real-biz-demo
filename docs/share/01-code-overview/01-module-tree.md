# 1.1 多模块分层树

> 预计讲解时长：~2 分钟
> 部分：一、代码概览

展示 foundation（基础组件）与 business（业务流程）两大聚合模块，以及内部分层。

![多模块分层树](../images/module-tree.png)

## 要点

- foundation 9 个基础组件模块：core / web / mybatis / domain / extension / lock / message / notify
- business DDD 五层：facade → domain → infrastructure → application → web
- 基础组件可独立复用，业务按 DDD 收敛

## 截图建议

整个模块树（IDE 项目视图或目录树），一眼看清两大块与内部子模块。

---

下一节 → [1.2 pom 依赖方向](./02-pom-dependency.md)
