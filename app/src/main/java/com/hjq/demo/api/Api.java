package com.hjq.demo.api;

import lombok.Getter;

public enum Api {
    USER("/user/");


    @Getter
    private String uri;

    Api(String url) {
        this.uri = uri;
    }
}
