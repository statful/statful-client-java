package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;

/**
 * This is a builder for the metrics API. It shouldn't be imported since it's used by the Telemetron client.
 */
public class APIBuilder {

    private static final long TIMESTAMP_DIVIDER = 1000L;

    private final MetricsSender metricsSender;

    private String metricName;
    private String value;
    private String namespace;
    private Tags tags;
    private Aggregations aggregations;
    private AggregationFreq aggregationFreq;
    private Integer sampleRate;

    public APIBuilder(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Sets the metric name.
     *
     * @param metricName Metric name as string
     * @return A reference to this builder
     */
    public final APIBuilder withMetricName(final String metricName) {
        this.metricName = metricName;
        return this;
    }

    /**
     * Sets the metric value.
     *
     * @param value The value as string
     * @return A reference to this builder
     */
    public final APIBuilder withValue(final String value) {
        this.value = value;
        return this;
    }

    /**
     * Sets the Telemetron client configuration to use.
     *
     * @param configuration Client configuration
     * @return A reference to this builder
     */
    public final APIBuilder withConfiguration(final ClientConfiguration configuration) {
        return this.withNamespace(configuration.getNamespace())
                .withSampleRate(configuration.getSampleRate());
    }

    /**
     * Sets the metric sample rate.
     *
     * @param sampleRate Sample rate as integer
     * @return A reference to this builder
     */
    private APIBuilder withSampleRate(final Integer sampleRate) {
        this.sampleRate = sampleRate;
        return this;
    }

    /**
     * Sets a tag to the metric
     *
     * @param type The tag type
     * @param value The tag value
     * @return A reference to this builder
     */
    public final APIBuilder tag(final String type, final String value) {
        // TODO - simplify
        if (type != null && !type.isEmpty() && value != null && !value.isEmpty()) {
            getSafeTags().putTag(type, value);
        }
        return this;
    }

    /**
     * Sets a Tags object to the metric. Which can be a collection.
     *
     * @param tags Tags to use
     * @return A reference to this builder
     */
    public final APIBuilder withTags(final Tags tags) {
        if (tags != null) {
            getSafeTags().merge(tags);
        }
        return this;
    }

    /**
     * Sets aggregations to the metric.
     *
     * @param aggregations Array of aggregations to use
     * @return A reference to this builder
     */
    public final APIBuilder aggregations(final Aggregation... aggregations) {
        // TODO - simplify
        if (aggregations != null) {
            for (Aggregation aggregation : aggregations) {
                if (aggregation != null) {
                    getSafeAggregations().put(aggregation);
                }
            }
        }
        return this;
    }

    /**
     * Sets an Aggregations object to the metric, which can be a collection.
     *
     * @param aggregations Aggregations to use
     * @return A reference to this builder
     */
    public final APIBuilder withAggregations(final Aggregations aggregations) {
        if (aggregations != null) {
            getSafeAggregations().merge(aggregations);
        }
        return this;
    }

    /**
     * Sets the aggregation frequency of the metric.
     *
     * @param aggFreq Aggregation frequency (10, 30, 60, 120, 180, 300)
     * @return A reference to this builder
     */
    public final APIBuilder aggFreq(final AggregationFreq aggFreq) {
        aggregationFreq = aggFreq;
        return this;
    }

    /**
     * Sets the namespace of the metric.
     *
     * @param namespace Namespace as string
     * @return A reference to this builder
     */
    public final APIBuilder namespace(final String namespace) {
        withNamespace(namespace);
        return this;
    }

    private APIBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    /**
     * Sends the metric to Telemetron.
     */
    public final void send() {
        // TODO - UTC Timestamp?
        String epochTime = Long.toString(System.currentTimeMillis() / TIMESTAMP_DIVIDER);
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
