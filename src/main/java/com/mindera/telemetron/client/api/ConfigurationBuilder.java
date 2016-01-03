package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.config.DefaultClientConfiguration;

public class ConfigurationBuilder<T> {

    private DefaultClientConfiguration result;
    private ConfigurationBuilderChain<T> builderChain;

    public ConfigurationBuilder<T> with = this;

    private ConfigurationBuilder() {
        this.result = new DefaultClientConfiguration();
    }

    private ConfigurationBuilder(ConfigurationBuilderChain<T> builderChain) {
        this.result = new DefaultClientConfiguration();
        this.builderChain = builderChain;
    }

    public static <T> ConfigurationBuilder<T> newBuilder(ConfigurationBuilderChain<T> builderChain) {
        return new ConfigurationBuilder<T>(builderChain);
    }

    static <T> ConfigurationBuilder<T> newBuilder() {
        return new ConfigurationBuilder<T>();
    }

    public ConfigurationBuilder<T> host(String host) {
        this.result.setHost(host);
        return this;
    }

    public ConfigurationBuilder<T> port(int port) {
        this.result.setPort(port);
        return this;
    }

    public ConfigurationBuilder<T> prefix(String prefix) {
        this.result.setPrefix(prefix);
        return this;
    }

    public ConfigurationBuilder<T> transport(Transport transport) {
        this.result.setTransport(transport);
        return this;
    }

    public ConfigurationBuilder<T> token(String token) {
        this.result.setToken(token);
        return this;
    }

    public ConfigurationBuilder<T> app(String app) {
        this.result.setApp(app);
        return this;
    }

    public ConfigurationBuilder<T> isDryRun(boolean isDryRun) {
        this.result.setDryRun(isDryRun);
        return this;
    }

    public ConfigurationBuilder<T> namespace(String namespace) {
        this.result.setNamespace(namespace);
        return this;
    }

    public ConfigurationBuilder<T> tag(String type, String value) {
        // TODO - simplify
        if (type != null && !type.isEmpty() && value != null && !value.isEmpty()) {
            this.result.mergeApplicationTag(type, value);
        }
        return this;
    }

    public ConfigurationBuilder<T> sampleRate(int sampleRate) {
        this.result.setSampleRate(sampleRate);
        return this;
    }

    public ConfigurationBuilder<T> flushSize(int flushSize) {
        this.result.setFlushSize(flushSize);
        return this;
    }

    public ConfigurationBuilder<T> flushInterval(long flushInterval) {
        this.result.setFlushIntervalMillis(flushInterval);
        return this;
    }

    public ConfigurationBuilder<T> timer(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeTimerTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> timer(AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeTimerAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> timer(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setTimerAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    public ConfigurationBuilder<T> counter(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeCounterTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> counter(AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeCounterAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> counter(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setCounterAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

    public ConfigurationBuilder<T> gauge(TagBuilder... tagBuilders) {
        if (tagBuilders != null) {
            for (TagBuilder tagBuilder : tagBuilders) {
                this.result.mergeGaugeTag(tagBuilder.getType(), tagBuilder.getValue());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> gauge(AggregationBuilder... aggregationBuilders) {
        if (aggregationBuilders != null) {
            for (AggregationBuilder aggregationBuilder : aggregationBuilders) {
                this.result.mergeGaugeAggregation(aggregationBuilder.getAggregation());
            }
        }
        return this;
    }

    public ConfigurationBuilder<T> gauge(AggregationFreqBuilder aggregationFreqBuilder) {
        if (aggregationFreqBuilder != null) {
            this.result.setGaugeAggregationFreq(aggregationFreqBuilder.getAggFreq());
        }
        return this;
    }

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
