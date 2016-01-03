package com.mindera.telemetron.client.api;

public final class TagBuilder {
    private final String type;
    private final String value;

    TagBuilder(final String type, final String value) {
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
