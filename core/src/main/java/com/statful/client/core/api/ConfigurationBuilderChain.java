package com.statful.client.core.api;

import com.statful.client.domain.api.ClientConfiguration;

/**
 * This is an interface to use with {@link ConfigurationBuilder} to allow chain the
 * builder with other types ans return <code>T</code> and passing the built
 * {@link com.statful.client.domain.api.ClientConfiguration} to the {@link #build} method.
 *
 * @param <T> The chained type
 */
public interface ConfigurationBuilderChain<T> {

    /**
     * Builds <code>T</code> with {@link com.statful.client.domain.api.ClientConfiguration}.
     *
     * @param configuration The {@link com.statful.client.domain.api.ClientConfiguration} to use
     * @return An instance of <code>T</code>
     */
    T build(ClientConfiguration configuration);
}
