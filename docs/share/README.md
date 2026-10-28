# real-biz-demo 分享

一个用于演示 Java 多模块工程结构与优雅实现的项目。本目录是分享材料，按三个部分组织，每部分下按小节拆成独立文件，便于逐篇讲解。

## 目录

### 一、代码概览

- [1.1 多模块分层树](./01-code-overview/01-module-tree.md)
- [1.2 pom 依赖方向](./01-code-overview/02-pom-dependency.md)
- [1.3 DDD 五层职责](./01-code-overview/03-ddd-layers.md)
- [1.4 聚合根与基类](./01-code-overview/04-aggregate-base.md)
- [1.5 processor 编排入口](./01-code-overview/05-action-executor.md)

### 二、基础组件的优雅实现

- [2.1 分布式锁的优雅实现](./02-foundation-elegance/01-lock.md)
- [2.2 异常的优雅处理](./02-foundation-elegance/02-exception.md)
- [2.3 登录信息的优雅注入](./02-foundation-elegance/03-login-inject.md)
- [2.4 数据落库的极简处理](./02-foundation-elegance/04-entity-interceptor.md)

### 三、串联场景

- [3.1 下单场景](./03-trade-scenarios/01-place-order.md)
- [3.2 结算场景](./03-trade-scenarios/02-settle.md)
- [3.3 串联回顾](./03-trade-scenarios/03-recap.md)

## 说明

- 截图统一放在 `./images/` 下，各小节 md 中用相对路径 `../images/xxx.png` 引用。
- 每篇顶部标注预计讲解时长，便于控时。
- 代码截图建议深色主题、隐藏行号区敏感路径；结构图可用 mermaid。
