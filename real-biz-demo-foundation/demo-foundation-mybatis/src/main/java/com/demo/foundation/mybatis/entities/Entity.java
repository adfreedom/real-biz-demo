package com.demo.foundation.mybatis.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.EqualsAndHashCode;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Objects;

/**
 * 持久化实体基类。
 * id 主键自增；isTransient() 判断是否未持久化；monitor()/isModified() 配合做差量更新。
 */
@EqualsAndHashCode(callSuper = false)
public class Entity implements Serializable {

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 监控用快照哈希，加载后由 monitor() 写入，isModified() 对比判断是否变更。
     */
    @TableField(exist = false)
    private int hashCode;

    /**
     * 监控用快照字符串，加载后由 monitor() 写入。
     */
    @TableField(exist = false)
    private String stringValue;

    public void monitor() {
        this.hashCode = hashCode();
        this.stringValue = this.toString();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isTransient() {
        return this.id == null || this.id <= 0;
    }

    @JsonIgnore
    public boolean isModified() {
        if (this.hashCode == 0 && !StringUtils.hasLength(this.stringValue)) {
            throw new UnsupportedOperationException("current object monitor is not called");
        }
        if (!Objects.equals(this.hashCode, this.hashCode())) {
            return true;
        }
        return !Objects.equals(this.toString(), this.stringValue);
    }
}
