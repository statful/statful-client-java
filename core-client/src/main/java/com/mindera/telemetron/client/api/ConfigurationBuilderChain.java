package com.mindera.telemetron.client.api;

public interface ConfigurationBuilderChain<T> {
    T build(ClientConfiguration configuration);
}
