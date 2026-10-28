# 2.3 登录信息的优雅注入

> 预计讲解时长：~5 分钟
> 部分：二、基础组件的优雅实现

网关写入请求头 → Controller 方法参数无感拿到登录用户。两套并行机制：

![登录注入双套机制](../images/login-inject.png)

## 要点

- **`@LoginUser` 参数解析器**：标注在 `UserContext` 及子类参数上，`LoginUserHandlerMethodArgumentResolver` 装配
- **`@RequestBody` 回填**：反序列化后把 userId/userName/clientIp/requestSource 回填到实现了 `IUserContext` 的 DTO，由 `LoginRequestBodyMethodProcessor` 完成
- 两套互不依赖，按场景选用
- 网关侧写头，业务侧无感

## 截图建议

- 两套机制的类结构（`UserContext` 层次 + 两个处理器）
- `@LoginUser` 使用示例 + `@RequestBody` DTO implements `IUserContext` 示例
- 拦截器把头写入 `UserInfoContext` 的代码

---

下一节 → [2.4 数据落库的极简处理](./04-entity-interceptor.md)
