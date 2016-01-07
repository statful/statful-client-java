package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;

public interface ConfigurationBuilderChain<T> {
    T build(ClientConfiguration configuration);
}
