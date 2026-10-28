package com.demo.foundation.domain.base;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 枚举字典项（前端下拉用）：label/name/value。
 */
@Data
@Accessors(chain = true)
@AllArgsConstructor
public class ValueObjectDto implements BaseCode {

    /** 展示文案 */
    private String label;
    /** 枚举名称 */
    private String name;
    /** 枚举值 */
    private Object value;

    @Override
    public String getCode() {
        return value == null ? null : String.valueOf(value);
    }
}
