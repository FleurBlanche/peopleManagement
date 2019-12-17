package com.hjq.demo.common.response;

import java.util.List;

import lombok.Data;

@Data
public class PageResult<T> {
    List<T> content;

    int number;

    int totalPages;
}
