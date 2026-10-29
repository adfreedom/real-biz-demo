# 2.4 全局日志处理

每个 Controller 方法都要打访问日志——URL、参数、响应、耗时、异常。手写 `logger.info(...)` 既重复又容易漏。这套切面让**所有 Controller 方法自动有结构化访问日志**，要开要关一个注解搞定。

## 怎么用

### 不用这套组件（传统写法）

每个方法都得自己 `logger.info`，参数、响应、耗时、异常各抄一遍，漏一个就是盲区：

```java
@PostMapping("place-order")
public Result<Void> placeOrder(@RequestBody PlaceOrderRequest req) {
    long start = System.currentTimeMillis();
    logger.info("place-order start | params: {}", req);
    try {
        Result<Void> result = ...;
        logger.info("place-order done | response: {} | time: {} ms", result, System.currentTimeMillis() - start);
        return result;
    } catch (Exception ex) {
        logger.error("place-order error | time: {} ms", System.currentTimeMillis() - start, ex);
        throw ex;
    }
    // 下一个方法再抄一遍……
}
```

### 用了这套组件

Controller 方法只要有 Spring MVC 映射注解，就**自动**有结构化访问日志，方法体里不写任何 logger：

```java
@PostMapping("place-order")          // 自动纳入切面
public Result<Void> placeOrder(@RequestBody PlaceOrderRequest req) {
    return tradeAppService.placeOrder(req);
}
```

### 两个开关注解

```java
@Target({ElementType.METHOD, ElementType.TYPE}) public @interface Loggable {}     // 显式开启
@Target({ElementType.METHOD, ElementType.TYPE}) public @interface Loggdisable {} // 显式关闭
```

Controller 方法因映射注解自动被切；非 Web 方法（如定时任务、应用服务关键方法）加 `@Loggable` 显式纳入；个别方法不想打日志（如健康检查、大文件下载）加 `@Loggdisable` 单独关闭。

## 怎么实现的

### 1. 切面：环绕所有 Web 入口

```java
// foundation-web: LoggingInterceptor
@Aspect @Component
public class LoggingInterceptor {

    @Value("${log.enable.detail:true}")
    private boolean enableDetail;

    @Around("(@annotation(Loggable) || " +
            "@annotation(GetMapping) || @annotation(PostMapping) || " +
            "@annotation(PutMapping)  || @annotation(DeleteMapping)) " +
            "&& !@annotation(Loggdisable)")
    public Object logMethodExecutionTime(ProceedingJoinPoint jp) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            Object result = jp.proceed();
            logRequestAndResponse(jp, result, System.currentTimeMillis() - start);
            return result;
        } catch (Exception ex) {
            logException(jp, ex, System.currentTimeMillis() - start);
            throw ex;
        }
    }
}
```

切点用一行 `@Around` 表达式说清「命中条件」：标注 `@Loggable`、或任一 Spring MVC 映射注解，且未被 `@Loggdisable` 关闭。

### 2. 结构化输出

```java
if (enableDetail) {
    logger.info("URL: {} | Method: {} | Params: {} | Response: {} | Time: {} ms",
        requestUrl, methodName, requestParams, responseResult, executionTime);
} else {
    logger.info("URL: {} | Method: {} | Time: {} ms", requestUrl, methodName, executionTime);
}
```

- `requestParams`：序列化入参，自动跳过 `HttpServletRequest` / `HttpServletResponse` / `MultipartFile`（含列表内），避免序列化整个容器。
- `getRequestUrl`：拼接类 `@RequestMapping` + 方法 mapping 路径；代理对象时回退到接口上的映射注解取路径。
- 异常走 `logException`，error 级 + rethrow，与全局异常处理器衔接。

![全局日志切面](../images/logging.svg)

## 关键点

- **自动覆盖**：所有 `@GetMapping` / `@PostMapping` / `@PutMapping` / `@DeleteMapping` 方法自动有访问日志，无需手写 logger。
- **一行切点表达多层语义**：`@Loggable || 四种 Mapping` 命中、`!@Loggdisable` 排除，开闭都在注解层。
- **结构化字段**：URL / 方法 / 参数 / 响应 / 耗时统一格式，便于日志采集与检索；`log.enable.detail` 一键切换详略。
- **入参净化**：自动跳过 Request/Response/MultipartFile，避免误序列化容器或大对象。
- **异常衔接**：异常 error 级记录后 rethrow，交由全局异常处理器归一为 `Result`，链路完整。

---

← [上一节：2.3 数据落库的极简处理](./03-entity-interceptor.md) ｜ [下一节：2.5 通用枚举查询](./05-enum-query.md) →
