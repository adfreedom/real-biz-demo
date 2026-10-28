# 1.2 pom 依赖方向

> 预计讲解时长：~2 分钟
> 部分：一、代码概览

依赖单向、不回流：foundation 不反向依赖 business。

![pom 依赖方向](../images/pom-dependency.png)

## 要点

- foundation 内部：core ← web / mybatis / domain / extension
- business 内部：facade ← domain ← infrastructure；facade / domain ← application ← web
- business 依赖 foundation，反之不成立
- 单向依赖保证基础组件可被其他业务复用

## 截图建议

用依赖方向图（mermaid 或画图工具）比截图清晰；或截几个关键 pom 的 dependencies 段佐证。

---

下一节 → [1.3 DDD 五层职责](./03-ddd-layers.md)
