package com.mindera.telemetron.client.api;

/**
 * This is an interface to use with {@link com.mindera.telemetron.client.api.ConfigurationBuilder} to allow chain the
 * builder with other types ans return <code>T</code> and passing the built
 * {@link com.mindera.telemetron.client.api.ClientConfiguration} to the {@link #build} method.
 *
 * @param <T>
 */
public interface ConfigurationBuilderChain<T> {

    /**
     * Builds <code>T</code> with {@link com.mindera.telemetron.client.api.ClientConfiguration}.
     *
     * @param configuration The {@link com.mindera.telemetron.client.api.ClientConfiguration} to use
     * @return An instance of <code>T</code>
     */
    T build(ClientConfiguration configuration);
}
