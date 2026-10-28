package com.demo.foundation.mybatis.entities;

import com.baomidou.mybatisplus.annotation.TableLogic;
import com.baomidou.mybatisplus.annotation.Version;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * 带审计字段的持久化实体基类。
 * 含创建/修改时间、创建/修改人、乐观锁 version、逻辑删除 isDeleted。
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CreationAuditedEntity extends Entity {

    private static final long serialVersionUID = -2694871920349030133L;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 修改时间
     */
    private LocalDateTime lastModifiedTime;

    /**
     * 创建人
     */
    private String createdBy;

    /**
     * 修改人
     */
    private String lastModifiedBy;

    @Version
    private Integer version;

    @TableLogic
    private Integer isDeleted = 0;

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }
}
