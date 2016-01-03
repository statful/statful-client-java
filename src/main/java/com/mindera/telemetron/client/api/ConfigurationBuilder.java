package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.config.DefaultClientConfiguration;

/**
 * This is a configuration builder which allows to chain a T type to return T upon build.
 *
 * @param <T> The type to return upon build
 */
public class ConfigurationBuilder<T> {

    private DefaultClientConfiguration result;
    private ConfigurationBuilderChain<T> builderChain;

    private ConfigurationBuilder() {
        this.result = new DefaultClientConfiguration();
    }

    private ConfigurationBuilder(ConfigurationBuilderChain<T> builderChain) {
        this.result = new DefaultClientConfiguration();
        this.builderChain = builderChain;
    }

    static <T> ConfigurationBuilder<T> newBuilder() {
        return new ConfigurationBuilder<T>();
    }

    /**
     * Instantiates a new configuration builder.
     *
     * @param builderChain A builder chain
     * @param <T> A T type to chain the configuration builder
     * @return A configuration builder that will return T
     */
    public static <T> ConfigurationBuilder<T> newBuilder(ConfigurationBuilderChain<T> builderChain) {
        return new ConfigurationBuilder<T>(builderChain);
    }

    /**
     * Sets the host.
     *
     * @param host Hostname
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> host(String host) {
        this.result.setHost(host);
        return this;
    }

    /**
     * Sets the port.
     *
     * @param port Port number
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> port(int port) {
        this.result.setPort(port);
        return this;
    }

    /**
     * Sets the metric prefix.
     *
     * @param prefix Prefix name
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> prefix(String prefix) {
        this.result.setPrefix(prefix);
        return this;
    }

    /**
     * Sets the transport.
     *
     * @param transport Transport type
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> transport(Transport transport) {
        this.result.setTransport(transport);
        return this;
    }

    /**
     * Sets the application token
     *
     * @param token The token
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> token(String token) {
        this.result.setToken(token);
        return this;
    }

    /**
     * Sets the application name.
     *
     * @param app Application name
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> app(String app) {
        this.result.setApp(app);
        return this;
    }

    /**
     * Sets if telemetry is running on dry mode.
     *
     * @param isDryRun Boolean to set dry run
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> isDryRun(boolean isDryRun) {
        this.result.setDryRun(isDryRun);
        return this;
    }

    /**
     * Sets the metrics namespace.
     *
     * @param namespace The namespace
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> namespace(String namespace) {
        this.result.setNamespace(namespace);
        return this;
    }

    /**
     * Sets the global tags to use.
     *
     * @param type Tag type
     * @param value Tag value
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> tag(String type, String value) {
        // TODO - simplify
        if (type != null && !type.isEmpty() && value != null && !value.isEmpty()) {
            this.result.mergeApplicationTag(type, value);
        }
        return this;
    }

    /**
     * Sets the global sample rate.
     *
     * @param sampleRate Sample rate as integer
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> sampleRate(int sampleRate) {
        this.result.setSampleRate(sampleRate);
        return this;
    }

    /**
     * Sets the flush size in number of metrics.
     *
     * @param flushSize Flush size as integer
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> flushSize(int flushSize) {
        this.result.setFlushSize(flushSize);
        return this;
    }

    /**
     * Sets the flush interval in milliseconds.
     *
     * @param flushInterval The flush interval as long
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> flushInterval(long flushInterval) {
        this.result.setFlushIntervalMillis(flushInterval);
        return this;
    }

    /**
     * Sets the timer method default tags. Example: timer(tag("host", "localhost"), tag("cluster", production)).
     *
     * @param tagBuilders An array of tag builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeTimerTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the timer method default aggregations. Example: timer(agg(AVG), agg(LAST)).
     *
     * @param aggregationBuilders An array of aggregation builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeTimerAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    /**
     * Sets the timer method default aggregation frequency. Example: timer(aggFreq(FREQ_120)).
     *
     * @param aggregationFreqBuilder An aggregation frequency builder
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> timer(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setTimerAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Sets the counter method default tags. Example: counter(tag("host", "localhost"), tag("cluster", production)).
     *
     * @param tagBuilders An array of tag builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeCounterTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the counter method default aggregations. Example: counter(agg(AVG), agg(LAST)).
     *
     * @param aggregationBuilders An array of aggregation builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeCounterAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    /**
     * Sets the counter method default aggregation frequency. Example: counter(aggFreq(FREQ_120)).
     *
     * @param aggregationFreqBuilder An aggregation frequency builder
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> counter(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setCounterAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Sets the gauge method default tags. Example: gauge(tag("host", "localhost"), tag("cluster", production)).
     *
     * @param tagBuilders An array of tag builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeGaugeTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    /**
     * Sets the gauge method default aggregations. Example: gauge(agg(AVG), agg(LAST)).
     *
     * @param aggregationBuilders An array of aggregation builders to use, which can be imported statically
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(AggregationBuilder... aggregationBuilders) {
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
     * @param aggregationFreqBuilder An aggregation frequency builder
     * @return A reference to this configuration builder
     */
    public ConfigurationBuilder<T> gauge(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setGaugeAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    /**
     * Builds the configuration and passed it to the builder chain.
     *
     * @return An instance of T with the build configuration.
     */
    public T build() {
        return builderChain.build(this.buildConfiguration());
    }

    ClientConfiguration buildConfiguration() {
        if (!result.isValid()) {
            throw new IllegalStateException("Configuration is not valid. Prefix and transport must be defined");
        }
        return this.result;
    }
}
