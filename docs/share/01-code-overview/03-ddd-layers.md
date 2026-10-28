# 1.3 DDD 五层职责

> 预计讲解时长：~2 分钟
> 部分：一、代码概览

一张图说清 business 每层放什么、不放什么。

![DDD 五层职责](../images/ddd-layers.png)

## 要点

| 层 | 职责 | 不放 |
|----|------|------|
| facade | 接口契约 / DTO | 业务逻辑 |
| domain | 实体 / 聚合根 / 领域服务 / 仓储接口 | DB 细节 |
| infrastructure | 仓储实现 / Mapper / 外部接口适配 | 业务规则 |
| application | 流程编排 / 事务边界 / 应用服务 | 领域一致性逻辑 |
| web | Controller / 配置 / 启动 | 业务逻辑 |

## 截图建议

五层栈图 + 每层一句职责，配一个典型类的包路径截图。

---

下一节 → [1.4 聚合根与基类](./04-aggregate-base.md)
