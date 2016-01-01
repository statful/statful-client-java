package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.nonNull;

public class APIBuilder {

    private static final Logger LOGGER = Logger.getLogger(APIBuilder.class.getName());

    private final MetricsSender metricsSender;

    private String metricName;
    private String value;
    private String namespace;
    private Tags tags;
    private Aggregations aggregations;
    private Integer aggregationFreq;
    private Integer sampleRate;

    public APIBuilder(MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    public APIBuilder withMetricName(String metricName) {
        this.metricName = metricName;
        return this;
    }

    public APIBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public APIBuilder withConfiguration(ClientConfiguration configuration) {
        return this.withNamespace(configuration.getNamespace())
                .withSampleRate(configuration.getSampleRate());
    }

    public APIBuilder withSampleRate(Integer sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    public APIBuilder with(Tags tags) {
        if (tags != null) {
            getSafeTags().merge(tags);
        }
        return this;
    }

    public APIBuilder with(TagBuilder... tagsBuilders) {
        asList(tagsBuilders).stream().filter(Objects::nonNull)
                .forEach(builder -> getSafeTags().putTag(builder.getType(), builder.getValue()));
        return this;
    }

    public APIBuilder with(AggregationBuilder... aggsBuilders) {
        asList(aggsBuilders).stream().filter(Objects::nonNull)
                .forEach(builder -> getSafeAggregations().put(builder.getAggregation()));
        return this;
    }

    public APIBuilder with(Aggregation... aggs) {
        getSafeAggregations().putAll(asList(aggs).stream().filter(Objects::nonNull).collect(Collectors.toList()));
        return this;
    }

    public APIBuilder with(Aggregations aggs) {
        getSafeAggregations().merge(aggs);
        return this;
    }

    public APIBuilder with(AggregationFreqBuilder aggrFreqBuilder) {
        if (nonNull(aggrFreqBuilder)) {
            this.withAggrFreq(aggrFreqBuilder.getAggrFreq());
        }
        return this;
    }

    public APIBuilder withAggrFreq(Integer aggrFreq) {
        aggregationFreq = aggrFreq;
        return this;
    }

    public APIBuilder with(NamespaceBuilder namespaceBuilder) {
        if (nonNull(namespaceBuilder)) {
            this.withNamespace(namespaceBuilder.getNamespace());
        }
        return this;
    }

    public APIBuilder withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public void send() {
        try {
            // TODO - UTC Timestamp?
            String epochTime = Long.toString(System.currentTimeMillis() / 1000L);
            metricsSender.put(metricName, value, tags, aggregations, aggregationFreq, sampleRate, namespace, epochTime);
        } catch (Exception e) {
            LOGGER.warning(e.toString());
        }
    }

    private Tags getSafeTags() {
        if (tags == null) {
            tags = new Tags();
        }
        return tags;
    }

    private Aggregations getSafeAggregations() {
        if (aggregations == null) {
            aggregations = new Aggregations();
        }
        return aggregations;
    }
}
