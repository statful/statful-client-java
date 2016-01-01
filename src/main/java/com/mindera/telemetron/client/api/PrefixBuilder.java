package com.mindera.telemetron.client.api;

class PrefixBuilder {
    private final String prefix;

    PrefixBuilder(String prefix) {
        this.prefix = prefix;
    }

    String getPrefix() {
        return prefix;
    }
}
