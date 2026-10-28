package com.demo.foundation.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.Collection;
import java.util.List;

/**
 * 扩展 Mapper。
 * 在 MyBatis-Plus BaseMapper 之上增加：
 * - insertBatchSomeColumn：批量插入（由 CustomSqlInjector 注入实现）
 * - selectOne/selectList 常用条件重载，简化调用
 */
public interface ExtensionMapper<T> extends BaseMapper<T> {

    /**
     * 批量插入（仅插入非默认填充字段）。实现由 CustomSqlInjector 的 InsertBatchSomeColumn 提供。
     *
     * @param list 需要插入的实例集合
     * @return 插入数量
     */
    int insertBatchSomeColumn(List<T> list);

    default T selectOne(SFunction<T, ?> field, Object value) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default T selectOne(SFunction<T, ?> field1, Object value1, SFunction<T, ?> field2, Object value2) {
        return selectOne(new LambdaQueryWrapper<T>().eq(field1, value1).eq(field2, value2));
    }

    default List<T> selectList() {
        return selectList(new QueryWrapper<>());
    }

    default List<T> selectList(SFunction<T, ?> field, Object value) {
        return selectList(new LambdaQueryWrapper<T>().eq(field, value));
    }

    default List<T> selectList(SFunction<T, ?> field, Collection<?> values) {
        return selectList(new LambdaQueryWrapper<T>().in(field, values));
    }

    default List<T> selectList(SFunction<T, ?> leField, SFunction<T, ?> geField, Object value) {
        return selectList(new LambdaQueryWrapper<T>().le(leField, value).ge(geField, value));
    }
}
