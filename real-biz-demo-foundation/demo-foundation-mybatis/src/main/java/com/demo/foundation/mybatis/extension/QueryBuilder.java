package com.demo.foundation.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import java.util.List;

/**
 * 批量查询构造器。
 * 用于 RepositoryUtils.findByMultiTimes 按分片参数构造查询条件。
 */
@FunctionalInterface
public interface QueryBuilder<E, A> {
    QueryWrapper<E> build(List<A> collection);
}
