package com.hjq.demo.common.entity;

import java.io.Serializable;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TimeRange implements Serializable {
    int start;

    int end;

    public TimeRange(int start, int end) {
        this.start = start;
        this.end = end;
    }
}
