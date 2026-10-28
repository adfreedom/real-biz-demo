# real-biz-demo 多模块工程

本工程分为两大聚合模块：**基础组件** 与 **业务流程**，用于演示入住、退房、订单查询等业务流程。

## 目录结构

```
real-biz-demo
├── pom.xml                       # 根聚合 pom（继承 spring-boot-starter-parent，统一版本管理）
├── real-biz-demo-foundation/     # ===== 基础组件模块 =====
│   ├── pom.xml                   # 聚合 pom
│   ├── demo-foundation-core      # 核心组件：常量/枚举/异常/工具/上下文/校验
│   ├── demo-foundation-web       # Web 组件：统一返回体/全局异常处理/拦截器
│   ├── demo-foundation-mybatis   # 持久层组件：MyBatis-Plus 配置/基础 Mapper/分页
│   ├── demo-foundation-domain    # 领域基类：基础实体/值对象/聚合根/领域事件
│   ├── demo-foundation-extension # 扩展组件：错误码/业务码/枚举字典
│   ├── demo-foundation-lock      # 分布式锁组件：Redisson 注解式分布式锁
│   ├── demo-foundation-message   # 消息组件：RocketMQ 生产/消费封装
│   └── demo-foundation-notify    # 通知组件：短信/邮件/站内信发送抽象
│
└── real-biz-demo-business/       # ===== 业务流程模块（DDD 分层）=====
    ├── pom.xml                   # 聚合 pom
    ├── demo-business-facade        # 对外门面：接口契约/DTO（外部可依赖）
    ├── demo-business-domain        # 领域层：实体/聚合根/领域服务/仓储接口
    ├── demo-business-infrastructure# 基础设施层：仓储实现/Mapper/外部接口适配
    ├── demo-business-application   # 应用层：流程编排/事务边界/应用服务
    └── demo-business-web           # 启动接入层：Controller/配置/Spring Boot 启动类
```

## 分层依赖方向

```
foundation:  core ← web / mybatis / domain / extension
business:    facade ← domain ← infrastructure
             facade / domain ← application ← web(启动)
business 依赖 foundation，foundation 不反向依赖 business
```

## 技术选型（初始）

- Java 8 / Spring Boot 2.7.18 / MyBatis-Plus 3.5.x / Hutool / Lombok
- groupId: `com.demo`，根包: `com.demo.foundation.*` 与 `com.demo.business.*`

## 构建

```bash
mvn -q clean install   # 在根目录构建全部模块
```

> 后续内容逐步完善：core 通用工具、web 统一返回体、mybatis 配置、业务域示例等。
