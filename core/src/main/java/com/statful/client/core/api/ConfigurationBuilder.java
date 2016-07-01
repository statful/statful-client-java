package com.statful.client.core.api;

import com.statful.client.core.config.DefaultClientConfiguration;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.Tags;
import com.statful.client.domain.api.Transport;

/**
 * This is a {@link com.statful.client.domain.api.ClientConfiguration} builder which allows to chain a
 * <code>T</code> type to return <code>T</code> upon build.
 * <p>
 * This class allows to wrap a {@link ConfigurationBuilderChain}
 * which in sequence returns an instance to a <code>T</code> that can use the build
 * {@link ConfigurationBuilder} as an argument.
 *
 * @param <T> The type to return upon build
 */
public final class ConfigurationBuilder<T> {

    private DefaultClientConfiguration result;
    private ConfigurationBuilderChain<T> builderChain;

    private ConfigurationBuilder() {
        this.result = new DefaultClientConfiguration();
    }

    /**
     * Private default constructor.
     *
     * @param builderChain The {@link ConfigurationBuilderChain} of <code>T</code>.
     */
    private ConfigurationBuilder(final ConfigurationBuilderChain<T> builderChain) {
        this.result = new DefaultClientConfiguration();
        this.builderChain = builderChain;
    }

    /**
     * Creates a new instance of {@link ConfigurationBuilder} of <code>T</code>.
     *
     * @param <T> The type to return upon build
     * @return An instance of {@link ConfigurationBuilder}
     */
    static <T> ConfigurationBuilder<T> newBuilder() {
        return new ConfigurationBuilder<T>();
    }

    /**
     * Creates a new instance of {@link ConfigurationBuilder} with the passed
     * {@link ConfigurationBuilderChain} of <code>T</code>.
     *
     * @param builderChain A {@link ConfigurationBuilderChain} of <code>T</code>
     * @param <T> A T type
     * @return A {@link ConfigurationBuilder} of <code>T</code>
     */
    public static <T> ConfigurationBuilder<T> newBuilder(final ConfigurationBuilderChain<T> builderChain) {
        return new ConfigurationBuilder<T>(builderChain);
    }

    /**
     * Sets the host.
     *
     * @param host The hostname
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> host(final String host) {
        if (isStringSafe(host)) {
            this.result.setHost(host);
        }
        return this;
    }

    /**
     * Sets the port.
     *
     * @param port The port number
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> port(final int port) {
        this.result.setPort(port);
        return this;
    }

    /**
     * Sets the metric prefix.
     *
     * @param prefix The prefix name
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> prefix(final String prefix) {
        if (isStringSafe(prefix)) {
            this.result.setPrefix(prefix);
        }
        return this;
    }

    /**
     * Sets the transport.
     *
     * @param transport The transport type
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> transport(final Transport transport) {
        if (transport != null) {
            this.result.setTransport(transport);
        }
        return this;
    }

    /**
     * Sets the application token
     *
     * @param token The token
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> token(final String token) {
        if (isStringSafe(token)) {
            this.result.setToken(token);
        }
        return this;
    }

    /**
     * Sets the application name.
     *
     * @param app The application name
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> app(final String app) {
        if (isStringSafe(app)) {
            this.result.setApp(app);
        }
        return this;
    }

    /**
     * Sets if telemetry is running in dry mode.
     *
     * @param isDryRun Boolean to set dry run
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> isDryRun(final boolean isDryRun) {
        this.result.setDryRun(isDryRun);
        return this;
    }

    /**
     * Sets the number of workers to process the telemetry.
     *
     * @param workersPoolSize The size of the pool as integer
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> workerPoolSize(final int workersPoolSize) {
        this.result.setWorkersPoolSize(workersPoolSize);
        return this;
    }

    /**
     * Sets the flag to use HTTP or HTTPS
     *
     * @param secure The flag to set if the transport should be secure
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> secure(final boolean secure) {
        this.result.setSecure(secure);
        return this;
    }

    /**
     * Sets the connection pool size for TCP based transports.
     *
     * @param connectPoolSize The connections pool size
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> connectionPoolSize(final int connectPoolSize) {
        this.result.setConnectionPoolSize(connectPoolSize);
        return this;
    }

    /**
     * Sets the connection timeout for TCP based transports.
     *
     * @param connectTimeoutMs The connection timeout in milliseconds
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> connectionTimeoutMs(final int connectTimeoutMs) {
        this.result.setConnectTimeoutMillis(connectTimeoutMs);
        return this;
    }

    /**
     * Sets the socket timeout for TCP based transports.
     *
     * @param socketTimeoutMs The socket timeout in milliseconds
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timeoutMs(final int socketTimeoutMs) {
        this.result.setSocketTimeoutMillis(socketTimeoutMs);
        return this;
    }

    /**
     * Sets the metrics namespace.
     *
     * @param namespace The namespace
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> namespace(final String namespace) {
        if (isStringSafe(namespace)) {
            this.result.setNamespace(namespace);
        }
        return this;
    }

    /**
     * Sets the global tags to use.
     *
     * @param type The tag type
     * @param value The tag value
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> tag(final String type, final String value) {
        if (!Tags.isEmptyOrNull(type, value)) {
            this.result.mergeApplicationTag(type, value);
        }
        return this;
    }

    /**
     * Sets the global sample rate.
     *
     * @param sampleRate The sample rate as integer
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> sampleRate(final int sampleRate) {
        this.result.setSampleRate(sampleRate);
        return this;
    }

    /**
     * Sets the flush size in number of metrics.
     *
     * @param flushSize The flush size as integer
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> flushSize(final int flushSize) {
        this.result.setFlushSize(flushSize);
        return this;
    }

    /**
     * Sets the flush interval in milliseconds.
     *
     * @param flushInterval The flush interval as long
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> flushInterval(final long flushInterval) {
        this.result.setFlushIntervalMillis(flushInterval);
        return this;
    }

    /**
     * Sets the timer method default tags.
     * <p>
     * Example: <code>timer(tag("host", "localhost"), tag("cluster", production)).</code>
     *
     * @param tagBuilders An array of {@link TagBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(final TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeTimerTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the timer method default aggregations.
     * <p>
     * Example: <code>timer(agg(AVG), agg(LAST)).</code>
     *
     * @param aggregationBuilders An array of {@link AggregationBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(final AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeTimerAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    /**
     * Sets the timer method default aggregation frequency.
     * <p>
     * Example: <code>timer(aggFreq(FREQ_120)).</code>
     *
     * @param aggregationFreqBuilder An {@link AggregationFreqBuilder}
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(final AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setTimerAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Sets the counter method default tags.
     * <p>
     * Example: <code>counter(tag("host", "localhost"), tag("cluster", production)).</code>
     *
     * @param tagBuilders An array of {@link TagBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(final TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeCounterTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the counter method default aggregations.
     * <p>
     * Example: <code>counter(agg(AVG), agg(LAST)).</code>
     *
     * @param aggregationBuilders An array of {@link AggregationBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(final AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeCounterAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    /**
     * Sets the counter method default aggregation frequency.
     * <p>
     * Example: <code>counter(aggFreq(FREQ_120)).</code>
     *
     * @param aggregationFreqBuilder An {@link AggregationFreqBuilder}
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(final AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setCounterAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Sets the gauge method default tags.
     * <p>
     * Example: gauge(tag("host", "localhost"), tag("cluster", production)).
     *
     * @param tagBuilders An array of {@link TagBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(final TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeGaugeTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the gauge method default aggregations.
     * <p>
     * Example: <code>gauge(agg(AVG), agg(LAST)).</code>
     *
     * @param aggregationBuilders An array of {@link AggregationBuilder} to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(final AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeGaugeAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    /**
     * Sets the gauge method default aggregation frequency. Example: gauge(aggFreq(FREQ_120)).
     *
     * @param aggregationFreqBuilder An {@link AggregationFreqBuilder}
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(final AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setGaugeAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Builds the configuration and passed it to the {@link ConfigurationBuilderChain}
     * of type <code>T</code>.
     *
     * @return An instance of <code>T</code>
     */
    public T build() {
        return builderChain.build(this.buildConfiguration());
    }

    /**
     * Returns the {@link com.statful.client.domain.api.ClientConfiguration} to be passed to the
     * {@link ConfigurationBuilderChain}.
     *
     * @return The {@link com.statful.client.domain.api.ClientConfiguration}
     */
    ClientConfiguration buildConfiguration() {
        if (!result.isValid()) {
            throw new IllegalStateException("Configuration is not valid. Prefix and transport must be defined");
        }
        return this.result;
    }

    private boolean isStringSafe(final String string) {
        return string != null && !string.isEmpty();
    }
}
