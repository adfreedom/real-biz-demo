# 2.2 异常的优雅处理

> 预计讲解时长：~5 分钟
> 部分：二、基础组件的优雅实现

业务层只 throw、不 catch；全局异常处理器统一兜底转响应体。

![异常处理链路](../images/exception-handling.png)

## 要点

- `BizException` 携业务码，业务层 `throw new BizException(...)`
- 全局异常处理器分类兜底：业务异常 / 参数异常 / 兜底异常
- Controller / Service 不再写 try-catch 模板

## 截图建议

- `BizException` 类定义
- 全局异常处理器的分类处理代码
- 统一返回体的结构

---

下一节 → [2.3 登录信息的优雅注入](./03-login-inject.md)
