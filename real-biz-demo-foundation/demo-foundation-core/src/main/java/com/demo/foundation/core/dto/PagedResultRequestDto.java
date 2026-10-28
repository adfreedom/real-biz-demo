package com.demo.foundation.core.dto;

/**
 * 分页查询请求 DTO。
 * current 为当前页码（从 1 开始），maxResultCount 为每页条数。
 */
public class PagedResultRequestDto extends LimitedResultRequestDto {

    private int current;

    public int getCurrent() {
        return current;
    }

    public void setCurrent(int current) {
        this.current = current;
    }
}
