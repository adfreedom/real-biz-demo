package com.demo.business.application.base;

import com.demo.foundation.domain.base.BaseEnum;
import com.demo.foundation.domain.base.EnumFilter;
import com.demo.foundation.domain.base.ValueObjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 通用枚举字典查询服务实现：反射读取枚举常量，映射为字典项；声明了 filter 的类型按业务上下文裁剪。
 */
@Service
@RequiredArgsConstructor
public class ValueObjectServiceImpl implements ValueObjectService {

    private final List<EnumFilter> filterList;

    @Override
    public List<ValueObjectDto> valueList(BaseType baseType) {
        BaseEnum<Object, String, String>[] constants =
                (BaseEnum<Object, String, String>[]) baseType.getEnumClass().getEnumConstants();
        List<ValueObjectDto> list = Arrays.stream(constants)
                .map(e -> new ValueObjectDto(e.getName(), e.getName(), e.getCode().toString()))
                .collect(Collectors.toList());
        if (baseType.getFilter() != null) {
            EnumFilter filter = findFilter(baseType);
            if (filter != null) {
                list = filter.filterValue(list);
            }
        }
        return list;
    }

    @Override
    public Map<String, List<ValueObjectDto>> valueMap() {
        Map<String, List<ValueObjectDto>> map = new HashMap<>();
        for (BaseType baseType : BaseType.values()) {
            BaseEnum<Object, String, String>[] constants =
                    (BaseEnum<Object, String, String>[]) baseType.getEnumClass().getEnumConstants();
            List<ValueObjectDto> list = Arrays.stream(constants)
                    .map(e -> new ValueObjectDto(e.getName(), e.getName(), e.getCode().toString()))
                    .collect(Collectors.toList());
            map.put(baseType.getCode(), list);
        }
        return map;
    }

    private EnumFilter findFilter(BaseType baseType) {
        for (EnumFilter filter : filterList) {
            if (baseType.getFilter().isAssignableFrom(filter.getClass())) {
                return filter;
            }
        }
        return null;
    }
}
