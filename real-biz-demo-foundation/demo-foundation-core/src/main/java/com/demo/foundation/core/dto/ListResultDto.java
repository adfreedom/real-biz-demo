package com.demo.foundation.core.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 列表结果 DTO。
 */
public class ListResultDto<T> implements Serializable {

    private static final long serialVersionUID = 2168939213817839362L;

    private transient List<T> content;

    public ListResultDto() {
    }

    public ListResultDto(List<T> content) {
        this.content = content;
    }

    public List<T> getContent() {
        if (content == null || content.isEmpty()) {
            content = new ArrayList<>();
        }
        return content;
    }

    public void setContent(List<T> content) {
        this.content = content;
    }
}
