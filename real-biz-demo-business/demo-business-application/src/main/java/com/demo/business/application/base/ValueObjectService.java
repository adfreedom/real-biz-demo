package com.demo.business.application.base;

import com.demo.foundation.domain.base.ValueObjectDto;

import java.util.List;
import java.util.Map;

/**
 * 通用枚举字典查询服务。
 */
public interface ValueObjectService {

    /**
     * 查询指定类型的枚举可选项。
     */
    List<ValueObjectDto> valueList(BaseType baseType);

    /**
     * 一次性返回所有枚举类型的可选项，按 BaseType 的 code 分组。
     */
    Map<String, List<ValueObjectDto>> valueMap();
}
