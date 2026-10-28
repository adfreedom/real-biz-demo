package com.demo.foundation.domain.base;

import java.util.List;

/**
 * 枚举字典过滤器：按业务上下文裁剪某类枚举的可选项。
 * 实现为 Spring bean，由业务侧 {@code BaseType} 声明的 filter 类型匹配生效。
 */
public interface EnumFilter {

    List<ValueObjectDto> filterValue(List<ValueObjectDto> list);
}
