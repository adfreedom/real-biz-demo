package com.demo.foundation.domain.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 枚举字典项：code/name/value。
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class BaseEnumDto {

    /** 枚举编码 */
    private String code;
    /** 枚举名称 */
    private String name;
    /** 枚举值 */
    private String value;
}
