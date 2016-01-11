package com.mindera.telemetron.client.api;

/**
 * This is a builder for the Telemetron client.
 */
public final class TelemetronClientBuilder {

    private final ConfigurationBuilder<TelemetronClient> configurationBuilder;

    /**
     * Default constructor.
     *
     * @param configurationBuilder A {@link com.mindera.telemetron.client.api.ConfigurationBuilder} chained with
     * {@link com.mindera.telemetron.client.api.TelemetronClient}
     */
    public TelemetronClientBuilder(final ConfigurationBuilder<TelemetronClient> configurationBuilder) {
        this.configurationBuilder = configurationBuilder;
    }

    /**
     * A syntax sugar method.
     *
     * @return A reference to {@link com.mindera.telemetron.client.api.ConfigurationBuilder} chained with
     * {@link com.mindera.telemetron.client.api.TelemetronClient}
     */
    public ConfigurationBuilder<TelemetronClient> with() {
        return configurationBuilder;
    }

    /**
     * An instance of {@link com.mindera.telemetron.client.api.TelemetronClient} configured with
     * {@link com.mindera.telemetron.client.api.ConfigurationBuilder}.
     *
     * @return An instance of {@link com.mindera.telemetron.client.api.TelemetronClient}
     */
    public TelemetronClient build() {
        return configurationBuilder.build();
    }
}
