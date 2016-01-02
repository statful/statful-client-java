package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;

import java.util.logging.Logger;

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
        for (TagBuilder builder : tagsBuilders) {
            if (builder != null) {
                getSafeTags().putTag(builder.getType(), builder.getValue());
            }
        }
        return this;
    }

    public APIBuilder with(AggregationBuilder... aggregationBuilders) {
        for (AggregationBuilder builder : aggregationBuilders) {
            if (builder != null) {
                getSafeAggregations().put(builder.getAggregation());
            }
        }
        return this;
    }

    public APIBuilder with(Aggregation... aggregations) {
        if (aggregations != null) {
            for (Aggregation aggregation : aggregations) {
                if (aggregation != null) {
                    getSafeAggregations().put(aggregation);
                }
            }
        }
        return this;
    }

    public APIBuilder with(Aggregations aggregations) {
        if (aggregations != null) {
            getSafeAggregations().merge(aggregations);
        }
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
        // TODO - UTC Timestamp?
        String epochTime = Long.toString(System.currentTimeMillis() / 1000L);
        metricsSender.put(metricName, value, tags, aggregations, aggregationFreq, sampleRate, namespace, epochTime);
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
