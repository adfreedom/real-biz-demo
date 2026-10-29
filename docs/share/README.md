# real-biz-demo 分享

本目录是分享材料，按三个部分组织，每部分下按小节拆成独立文件，便于逐篇阅读。

## 仓库定位

**基础组件库（foundation）与业务系统（business）在实际中是两个独立仓库**：前者是一个可被任意业务复用的技术组件库，后者是消费该组件库的具体业务系统，二者各自开发、发版、演进。本项目为展示方便将两者置于同一目录，但阅读时应把它们视作两个独立工程——第二部分单独介绍组件库，第三部分单独介绍业务系统。

## 目录

- [关于我](./00-about.md)

### 一、代码概览

- [1.1 多模块分层树](./01-code-overview/01-module-tree.md)
- [1.2 pom 依赖方向](./01-code-overview/02-pom-dependency.md)
- [1.3 DDD 五层职责](./01-code-overview/03-ddd-layers.md)
- [1.4 聚合根与基类](./01-code-overview/04-aggregate-base.md)
- [1.5 processor 编排入口](./01-code-overview/05-action-executor.md)

### 二、基础组件库

- [2.1 锁的一种优雅实现](./02-foundation-elegance/01-lock.md)
- [2.2 登录信息的优雅注入](./02-foundation-elegance/02-login-inject.md)
- [2.3 数据落库的极简处理](./02-foundation-elegance/03-entity-interceptor.md)
- [2.4 全局日志处理](./02-foundation-elegance/04-logging.md)
- [2.5 通用枚举查询](./02-foundation-elegance/05-enum-query.md)

### 三、业务系统

- [3.1 下单场景](./03-trade-scenarios/01-place-order.md)
- [3.2 结算场景](./03-trade-scenarios/02-settle.md)
- [3.3 串联回顾](./03-trade-scenarios/03-recap.md)

## 说明

- 截图统一放在 `./images/` 下，各小节 md 中用相对路径 `../images/xxx.svg` 引用。
