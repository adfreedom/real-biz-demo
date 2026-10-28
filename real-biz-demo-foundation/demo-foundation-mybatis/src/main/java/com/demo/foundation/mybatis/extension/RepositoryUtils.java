package com.demo.foundation.mybatis.extension;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.demo.foundation.mybatis.entities.CreationAuditedEntity;
import com.demo.foundation.mybatis.entities.Entity;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 仓储工具。
 * 提供聚合差量落库 saveChanges：按 isTransient/isModified/isDeleted 分流 增/改/删。
 * demo 版用 JDK 内联分片，去掉了 pms 依赖的 guava Lists 与 commons-collections4。
 */
public class RepositoryUtils {

    public static <T> List<T> findByMultiTimes(QueryWrapper<T> query, BaseMapper<T> mapper, int onceMaxCount) {
        Long count = mapper.selectCount(query);
        long times = (count / onceMaxCount) + (count % onceMaxCount == 0 ? 0 : 1);

        if (count <= onceMaxCount) {
            return mapper.selectList(query);
        }

        List<T> list = new LinkedList<>();
        for (long i = 0; i < times; i++) {
            IPage<T> page = new Page<>(i + 1, onceMaxCount);
            IPage<T> pageResult = mapper.selectPage(page, query);
            if (pageResult.getSize() > 0) {
                list.addAll(pageResult.getRecords());
            }
        }
        return list;
    }

    public static <T extends Entity, A> List<T> findByMultiTimes(BaseMapper<T> mapper, List<A> collection, int onceMaxCount, QueryBuilder<T, A> builder) {
        List<List<A>> partitions = partition(collection, onceMaxCount);

        List<T> totalItems = new LinkedList<>();
        partitions.forEach(partition -> {
            QueryWrapper<T> query = builder.build(partition);
            List<T> list = mapper.selectList(query);
            if (list != null && !list.isEmpty()) {
                totalItems.addAll(list);
            }
        });
        return totalItems;
    }

    public static <T extends CreationAuditedEntity, M extends ExtensionMapper<T>> void saveChanges(M mapper, List<T> collection, int onceMaxCount) {
        if (collection == null || collection.isEmpty()) {
            return;
        }
        if (mapper == null) {
            throw new IllegalArgumentException("mapper is null");
        }

        List<T> updatedItems = collection.stream()
                .filter(entity -> !entity.isTransient() && entity.isModified() && Objects.equals(entity.getIsDeleted(), 0))
                .collect(Collectors.toList());

        List<T> addedItems = collection.stream()
                .filter(Entity::isTransient)
                .collect(Collectors.toList());

        List<T> deletedItems = collection.stream()
                .filter(e -> Objects.equals(e.getIsDeleted(), 1))
                .collect(Collectors.toList());

        if (!deletedItems.isEmpty()) {
            List<Long> deletedIds = deletedItems.stream().map(Entity::getId).collect(Collectors.toList());
            int count = mapper.deleteBatchIds(deletedIds);
            Assert.isTrue(count == deletedItems.size(), "delete detail fail.");
        }

        if (!updatedItems.isEmpty()) {
            updatedItems.forEach(item -> {
                int row = mapper.updateById(item);
                Assert.isTrue(row == 1, "update detail fail，并发修改");
            });
        }

        if (!addedItems.isEmpty()) {
            List<List<T>> partitions = partition(addedItems, onceMaxCount);
            partitions.forEach(items -> {
                int count = mapper.insertBatchSomeColumn(items);
                Assert.isTrue(count == items.size(), "insert detail fail.");
            });
        }
    }

    public static <T extends CreationAuditedEntity, M extends ExtensionMapper<T>> void saveChanges(M mapper, List<T> collection) {
        saveChanges(mapper, collection, 100);
    }

    public static <T extends CreationAuditedEntity, M extends ExtensionMapper<T>> void saveChanges(M mapper, T entity) {
        if (mapper == null) {
            throw new IllegalArgumentException("mapper is null");
        }
        if (entity == null) {
            throw new IllegalArgumentException("entity is null");
        }

        if (entity.isTransient()) {
            int row = mapper.insert(entity);
            Assert.isTrue(row == 1, "insert detail fail，并发写入");
        } else if (Objects.equals(entity.getIsDeleted(), 1)) {
            int row = mapper.deleteById(entity);
            Assert.isTrue(row == 1, "delete detail fail，并发删除");
        } else {
            int row = mapper.updateById(entity);
            Assert.isTrue(row == 1, "update detail fail，并发修改");
        }
    }

    /**
     * 内联分片，等价 guava Lists.partition，避免引入 guava。
     */
    private static <E> List<List<E>> partition(List<E> source, int size) {
        List<List<E>> result = new ArrayList<>();
        if (source == null || source.isEmpty() || size <= 0) {
            return result;
        }
        int total = source.size();
        for (int from = 0; from < total; from += size) {
            int to = Math.min(from + size, total);
            result.add(new ArrayList<>(source.subList(from, to)));
        }
        return result;
    }
}
