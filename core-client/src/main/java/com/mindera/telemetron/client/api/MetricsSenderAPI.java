package com.mindera.telemetron.client.api;

import java.util.logging.Logger;

/**
 * This class is an implementation of the {@link com.mindera.telemetron.client.api.SenderAPI},
 * which uses {@link com.mindera.telemetron.client.api.MetricsSender} to send metrics.
 */
public final class MetricsSenderAPI implements SenderAPI {

    private static final Logger LOGGER = Logger.getLogger(MetricsSenderAPI.class.getName());

    private static final long TIMESTAMP_DIVIDER = 1000L;

    private final MetricsSender metricsSender;

    private String metricName;
    private String value;
    private String namespace;
    private Tags tags;
    private Aggregations aggregations;
    private AggregationFreq aggregationFreq;
    private Integer sampleRate;

    /**
     * Default constructor.
     *
     * @param metricsSender The {@link com.mindera.telemetron.client.api.MetricsSender} to send metrics
     */
    MetricsSenderAPI(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Builds a new instance of {@link com.mindera.telemetron.client.api.SenderAPI} with the metrics sender.
     *
     * @param metricsSender The {@link com.mindera.telemetron.client.api.MetricsSender}
     * @return An instance of {@link com.mindera.telemetron.client.api.SenderAPI}
     */
    public static SenderAPI newInstance(final MetricsSender metricsSender) {
        return new MetricsSenderAPI(metricsSender);
    }

    @Override
    public SenderAPI with() {
        return this;
    }

    @Override
    public SenderAPI metricName(final String metricName) {
        if (isStringSafe(metricName)) {
            this.metricName = metricName;
        }
        return this;
    }

    @Override
    public SenderAPI value(final String value) {
        if (isStringSafe(value)) {
            this.value = value;
        }
        return this;
    }

    @Override
    public SenderAPI configuration(final ClientConfiguration configuration) {
        if (configuration != null) {
            this.withNamespace(configuration.getNamespace()).sampleRate(configuration.getSampleRate());
        }
        return this;
    }

    @Override
    public SenderAPI sampleRate(final Integer sampleRate) {
        if (sampleRate != null) {
            this.sampleRate = sampleRate;
        }
        return this;
    }

    @Override
    public SenderAPI tag(final String type, final String value) {
        if (!Tags.isEmptyOrNull(type, value)) {
            getSafeTags().putTag(type, value);
        }
        return this;
    }

    @Override
    public SenderAPI tags(final Tags tags) {
        if (tags != null) {
            getSafeTags().merge(tags);
        }
        return this;
    }

    @Override
    public SenderAPI aggregations(final Aggregation... aggregations) {
        if (aggregations != null) {
            for (Aggregation aggregation : aggregations) {
                withAggregation(aggregation);
            }
        }
        return this;
    }

    @Override
    public SenderAPI aggregations(final Aggregations aggregations) {
        if (aggregations != null) {
            getSafeAggregations().merge(aggregations);
        }
        return this;
    }

    private SenderAPI withAggregation(final Aggregation aggregation) {
        if (aggregation != null) {
            getSafeAggregations().put(aggregation);
        }
        return this;
    }

    @Override
    public SenderAPI aggFreq(final AggregationFreq aggFreq) {
        if (aggFreq != null) {
            aggregationFreq = aggFreq;
        }
        return this;
    }

    @Override
    public SenderAPI namespace(final String namespace) {
        withNamespace(namespace);
        return this;
    }

    private SenderAPI withNamespace(final String namespace) {
        if (isStringSafe(namespace)) {
            this.namespace = namespace;
        }
        return this;
    }

    @Override
    public void send() {
        try {
            if (isValid()) {
                long unixTimestamp = getUnixTimestamp();
                metricsSender.put(metricName, value, tags, aggregations, aggregationFreq, sampleRate, namespace, unixTimestamp);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please send metric name and value.");
            }
        } catch (Exception e) {
            LOGGER.warning("An exception has occurred while sending the metric to Telemetron: " + e.toString());
        }
    }

    private boolean isValid() {
        return isStringSafe(metricName) && isStringSafe(value);
    }

    private long getUnixTimestamp() {
        return System.currentTimeMillis() / TIMESTAMP_DIVIDER;
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

    private boolean isStringSafe(final String string) {
        return string != null && !string.isEmpty();
    }

    /**
     * A getter for the metric name.
     *
     * @return The metric name
     */
    String getMetricName() {
        return metricName;
    }

    /**
     * A getter for the metric value.
     *
     * @return The value of the metric
     */
    String getValue() {
        return value;
    }

    /**
     * A getter for the namespace.
     *
     * @return The namespace of the metric
     */
    String getNamespace() {
        return namespace;
    }

    /**
     * A getter for the {@link com.mindera.telemetron.client.api.Tags} of the metric.
     *
     * @return The {@link com.mindera.telemetron.client.api.Tags} of the metric
     */
    Tags getTags() {
        return tags;
    }

    /**
     * A getter for the {@link com.mindera.telemetron.client.api.Aggregation} of the metric.
     *
     * @return The {@link com.mindera.telemetron.client.api.Aggregations} of the metric
     */
    Aggregations getAggregations() {
        return aggregations;
    }

    /**
     * A getter for the {@link com.mindera.telemetron.client.api.AggregationFreq} of the metric.
     *
     * @return The {@link com.mindera.telemetron.client.api.AggregationFreq} of the metric.
     */
    AggregationFreq getAggregationFreq() {
        return aggregationFreq;
    }

    /**
     * A getter for the sample rate.
     *
     * @return The sample rate
     */
    Integer getSampleRate() {
        return sampleRate;
    }
}
