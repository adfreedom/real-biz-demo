package com.demo.foundation.domain.base;

/**
 * 枚举基类：统一枚举的 code/name/value 三元组契约，
 * 供通用枚举字典查询与 code 反查使用。
 *
 * @param <T> code 类型
 * @param <I> name 类型
 * @param <J> value 类型
 */
public interface BaseEnum<T, I, J> {

    T getCode();

    I getName();

    J getValue();
}
