package com.hjq.demo.api;

import lombok.Getter;

public enum Api {
    USER("User/"),
    ACTIVITY("Activity/"),
    MOMENT("Moment/"),
    REQUEST("Request/"),
    RELATION("Relation/"),
    RECORD("Record/"),
    FILE("/"),
    INFORMATION("Information/");

    private String uri;

    Api(String uri) {
        this.uri = uri;
    }

    public String getUri() {
        return uri;
    }
}
