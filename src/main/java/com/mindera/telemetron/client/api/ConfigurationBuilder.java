package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.config.DefaultClientConfiguration;

public class ConfigurationBuilder {

    private DefaultClientConfiguration result;

    private ConfigurationBuilder() {
        this.result = new DefaultClientConfiguration();
    }

    public static ConfigurationBuilder newBuilder() {
        return new ConfigurationBuilder();
    }

    public ConfigurationBuilder with(HostBuilder hostBuilder) {
        withHost(hostBuilder.getHost());
        return this;
    }

    public ConfigurationBuilder withHost(String host) {
        this.result.setHost(host);
        return this;
    }

    public ConfigurationBuilder with(PortBuilder portBuilder) {
        withPort(portBuilder.getPort());
        return this;
    }

    public ConfigurationBuilder withPort(int port) {
        this.result.setPort(port);
        return this;
    }

    public ConfigurationBuilder with(PrefixBuilder prefixBuilder) {
        withPrefix(prefixBuilder.getPrefix());
        return this;
    }

    public ConfigurationBuilder withPrefix(String prefix) {
        this.result.setPrefix(prefix);
        return this;
    }

    public ConfigurationBuilder with(TransportBuilder transportBuilder) {
        withTransport(transportBuilder.getTransport());
        return this;
    }

    public ConfigurationBuilder withTransport(Transport transport) {
        this.result.setTransport(transport);
        return this;
    }

    public ConfigurationBuilder with(TokenBuilder tokenBuilder) {
        withToken(tokenBuilder.getToken());
        return this;
    }

    public ConfigurationBuilder withToken(String token) {
        this.result.setToken(token);
        return this;
    }

    public ConfigurationBuilder with(AppBuilder appBuilder) {
        withApp(appBuilder.getApp());
        return this;
    }
    
    public ConfigurationBuilder withApp(String app) {
        this.result.setApp(app);
        return this;
    }

    public ConfigurationBuilder with(DryRunBuilder dryRunBuilder) {
        isDryRun(dryRunBuilder.isDryRun());
        return this;
    }

    public ConfigurationBuilder isDryRun(boolean isDryRun) {
        this.result.setDryRun(isDryRun);
        return this;
    }

    public ConfigurationBuilder with(NamespaceBuilder namespaceBuilder) {
        withNamespace(namespaceBuilder.getNamespace());
        return this;
    }

    public ConfigurationBuilder withNamespace(String namespace) {
        this.result.setNamespace(namespace);
        return this;
    }

    public ConfigurationBuilder with(TagBuilder... tagsBuilders) {
        for (TagBuilder tagBuilder : tagsBuilders) {
            this.result.mergeApplicationTag(tagBuilder.getType(), tagBuilder.getValue());
        }
        return this;
    }

    public ConfigurationBuilder with(SampleRateBuilder sampleRateBuilder) {
        withSampleRate(sampleRateBuilder.getSampleRate());
        return this;
    }

    public ConfigurationBuilder withSampleRate(int sampleRate) {
        this.result.setSampleRate(sampleRate);
        return this;
    }

    public ConfigurationBuilder with(FlushSizeBuilder flushSizeBuilder) {
        withFlushSize(flushSizeBuilder.getFlushSize());
        return this;
    }

    public ConfigurationBuilder withFlushSize(int flushSize) {
        this.result.setFlushSize(flushSize);
        return this;
    }

    public ConfigurationBuilder with(FlushIntervalBuilder flushIntervalBuilder) {
        withFlushInterval(flushIntervalBuilder.getFlushInterval());
        return this;
    }

    public ConfigurationBuilder withFlushInterval(long flushInterval) {
        this.result.setFlushIntervalMillis(flushInterval);
        return this;
    }

    public ConfigurationBuilder with(TimerConfigBuilder timerConfigBuilder) {
        this.result.mergeTimerTags(timerConfigBuilder.getTags());
        this.result.mergeTimerAggregations(timerConfigBuilder.getAggregations());

        if (timerConfigBuilder.getAggregationFreq() != null) {
            this.result.setTimerAggregationFreq(timerConfigBuilder.getAggregationFreq());
        }
        return this;
    }

    public ConfigurationBuilder with(CounterConfigBuilder counterConfigBuilder) {
        this.result.mergeCounterTags(counterConfigBuilder.getTags());
        this.result.mergeCounterAggregations(counterConfigBuilder.getAggregations());

        if (counterConfigBuilder.getAggregationFreq() != null) {
            this.result.setCounterAggregationFreq(counterConfigBuilder.getAggregationFreq());
        }
        return this;
    }

    public ConfigurationBuilder with(GaugeConfigBuilder gaugeConfigBuilder) {
        this.result.mergeGaugeTags(gaugeConfigBuilder.getTags());
        this.result.mergeGaugeAggregations(gaugeConfigBuilder.getAggregations());

        if (gaugeConfigBuilder.getAggregationFreq() != null) {
            this.result.setGaugeAggregationFreq(gaugeConfigBuilder.getAggregationFreq());
        }
        return this;
    }

    public ClientConfiguration build() {
        if (!result.isValid()) {
            throw new IllegalStateException("Configuration is not valid. Prefix and transport must be defined");
        }
        return this.result;
    }

    public static PrefixBuilder prefix(String prefix) {
        return new PrefixBuilder(prefix);
    }

    public static HostBuilder host(String host) {
        return new HostBuilder(host);
    }

    public static PortBuilder port(int port) {
        return new PortBuilder(port);
    }

    public static TransportBuilder transport(Transport transport) {
        return new TransportBuilder(transport);
    }

    public static AppBuilder app(String app) {
        return new AppBuilder(app);
    }

    public static TokenBuilder token(String token) {
        return new TokenBuilder(token);
    }

    public static DryRunBuilder dryRun(boolean dryRun) {
        return new DryRunBuilder(dryRun);
    }

    public static SampleRateBuilder sampleRate(int sampleRate) {
        return new SampleRateBuilder(sampleRate);
    }

    public static FlushSizeBuilder flushSize(int flushSize) {
        return new FlushSizeBuilder(flushSize);
    }

    public static FlushIntervalBuilder flushInterval(long flushInterval) {
        return new FlushIntervalBuilder(flushInterval);
    }

    public static TimerConfigBuilder timer(TagBuilder... tagsBuilders) {
        return new TimerConfigBuilder(tagsBuilders);
    }

    public static TimerConfigBuilder timer(AggregationBuilder... aggsBuilders) {
        return new TimerConfigBuilder(aggsBuilders);
    }

    public static TimerConfigBuilder timer(Aggregation... aggregations) {
        return new TimerConfigBuilder(aggregations);
    }

    public static TimerConfigBuilder timer(AggregationFreqBuilder aggrFreqBuilder) {
        return new TimerConfigBuilder(aggrFreqBuilder);
    }

    public static CounterConfigBuilder counter(TagBuilder... tagsBuilders) {
        return new CounterConfigBuilder(tagsBuilders);
    }

    public static CounterConfigBuilder counter(AggregationBuilder... aggsBuilders) {
        return new CounterConfigBuilder(aggsBuilders);
    }

    public static CounterConfigBuilder counter(Aggregation... aggs) {
        return new CounterConfigBuilder(aggs);
    }

    public static CounterConfigBuilder counter(AggregationFreqBuilder aggrFreqBuilder) {
        return new CounterConfigBuilder(aggrFreqBuilder);
    }

    public static GaugeConfigBuilder gauge(TagBuilder... tagsBuilders) {
        return new GaugeConfigBuilder(tagsBuilders);
    }

    public static GaugeConfigBuilder gauge(AggregationBuilder... aggsBuilders) {
        return new GaugeConfigBuilder(aggsBuilders);
    }

    public static GaugeConfigBuilder gauge(Aggregation... aggs) {
        return new GaugeConfigBuilder(aggs);
    }

    public static GaugeConfigBuilder gauge(AggregationFreqBuilder aggrFreqBuilder) {
        return new GaugeConfigBuilder(aggrFreqBuilder);
    }

}
