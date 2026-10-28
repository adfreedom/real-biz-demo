package com.demo.business.web.base.open;

import com.demo.business.application.base.BaseType;
import com.demo.business.application.base.ValueObjectService;
import com.demo.foundation.core.dto.Result;
import com.demo.foundation.domain.base.ValueObjectDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

/**
 * 通用枚举字典查询：按类型查可选项，或一次性返回全部。
 */
@RestController
@RequestMapping("open/value/object")
@RequiredArgsConstructor
public class ValueObjectController {

    private final ValueObjectService valueObjectService;

    /**
     * 查询指定类型的枚举可选项。
     */
    @GetMapping("list")
    public Result<List<ValueObjectDto>> list(@RequestParam BaseType baseType) {
        return Result.success(valueObjectService.valueList(baseType));
    }

    /**
     * 一次性返回所有枚举类型的可选项，按类型 code 分组。
     */
    @GetMapping("map")
    public Result<Map<String, List<ValueObjectDto>>> map() {
        return Result.success(valueObjectService.valueMap());
    }
}
