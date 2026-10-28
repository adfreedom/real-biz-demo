package com.demo.foundation.core.dto;

/**
 * 分页查询基础限制。
 */
public class LimitedResultRequestDto {

    private int maxResultCount = 10;

    public int getMaxResultCount() {
        return maxResultCount;
    }

    public void setMaxResultCount(int maxResultCount) {
        this.maxResultCount = maxResultCount;
    }
}
