package com.mindera.telemetron.client.api;

public final class TagBuilder {
    private final String type;
    private final String value;

    TagBuilder(String type, String value) {
        this.type = type;
        this.value = value;
    }

    String getType() {
        return type;
    }

    String getValue() {
        return value;
    }
}
