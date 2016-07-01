package com.statful.client.core.api;

import com.statful.client.domain.api.StatfulClient;

/**
 * This is a builder for the Statful client.
 */
public final class StatfulClientBuilder {

    private final ConfigurationBuilder<StatfulClient> configurationBuilder;

    /**
     * Default constructor.
     *
     * @param configurationBuilder A {@link ConfigurationBuilder} chained with
     * {@link com.statful.client.domain.api.StatfulClient}
     */
    public StatfulClientBuilder(final ConfigurationBuilder<StatfulClient> configurationBuilder) {
        this.configurationBuilder = configurationBuilder;
    }

    /**
     * A syntax sugar method.
     *
     * @return A reference to {@link ConfigurationBuilder} chained with
     * {@link com.statful.client.domain.api.StatfulClient}
     */
    public ConfigurationBuilder<StatfulClient> with() {
        return configurationBuilder;
    }

    /**
     * An instance of {@link com.statful.client.domain.api.StatfulClient} configured with
     * {@link ConfigurationBuilder}.
     *
     * @return An instance of {@link com.statful.client.domain.api.StatfulClient}
     */
    public StatfulClient build() {
        return configurationBuilder.build();
    }
}
