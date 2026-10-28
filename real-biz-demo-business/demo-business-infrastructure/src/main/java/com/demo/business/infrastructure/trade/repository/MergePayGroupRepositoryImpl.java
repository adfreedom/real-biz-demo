package com.demo.business.infrastructure.trade.repository;

import com.demo.business.domain.trade.entity.MergePayGroup;
import com.demo.business.domain.trade.entity.MergePayGroupEntity;
import com.demo.business.domain.trade.repository.MergePayGroupRepository;
import com.demo.business.infrastructure.trade.mapper.MergePayGroupEntityMapper;
import com.demo.foundation.mybatis.extension.RepositoryUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * 合并支付组仓储实现：差异落库与按组号加载。
 * 查询返回的实体由 EntitySelectInterceptor 自动建立监控快照，无需仓储手动处理。
 */
@Repository
@RequiredArgsConstructor
public class MergePayGroupRepositoryImpl implements MergePayGroupRepository {

    private final MergePayGroupEntityMapper mergePayGroupEntityMapper;

    @Override
    public void saveChanges(MergePayGroup group) {
        if (group == null) {
            return;
        }
        RepositoryUtils.saveChanges(mergePayGroupEntityMapper, group.getMergePayGroupEntity());
    }

    @Override
    public MergePayGroup findByGroupNo(String groupNo) {
        MergePayGroupEntity entity = mergePayGroupEntityMapper.selectOne(MergePayGroupEntity::getGroupNo, groupNo);
        if (entity == null) {
            return null;
        }
        MergePayGroup group = new MergePayGroup();
        group.setMergePayGroupEntity(entity);
        return group;
    }
}
