package com.demo.foundation.core.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 分页结果 DTO。
 */
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@Data
public class PageResultDto<T> extends ListResultDto<T> {

    private static final long serialVersionUID = 1068101089266872771L;

    private long total;

    public PageResultDto(List<T> content, long total) {
        super(content);
        this.total = total;
    }
}
