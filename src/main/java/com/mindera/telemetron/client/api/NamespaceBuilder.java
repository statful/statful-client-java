package com.mindera.telemetron.client.api;

public final class NamespaceBuilder {
    private final String namespace;

    NamespaceBuilder(String namespace) {
        this.namespace = namespace;
    }

    String getNamespace() {
        return namespace;
    }
}
