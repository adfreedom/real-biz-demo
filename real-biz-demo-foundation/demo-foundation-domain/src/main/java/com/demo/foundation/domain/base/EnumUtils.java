package com.demo.foundation.domain.base;

import java.util.Arrays;
import java.util.Objects;

/**
 * 枚举工具：按 code/value/name 反查枚举常量。
 */
public class EnumUtils {

    public static <E extends BaseEnum<T, I, J>, T, I, J> E codeOf(E[] values, String code) {
        return Arrays.stream(values).filter(t -> Objects.equals(code, t.getCode())).findFirst().orElse(null);
    }

    public static <E extends BaseEnum<T, I, J>, T, I, J> E valueOf(E[] values, String value) {
        return Arrays.stream(values).filter(t -> Objects.equals(value, t.getValue())).findFirst().orElse(null);
    }

    public static <E extends BaseEnum<T, I, J>, T, I, J> E nameOf(E[] values, String name) {
        return Arrays.stream(values).filter(t -> Objects.equals(name, t.getName())).findFirst().orElse(null);
    }
}
