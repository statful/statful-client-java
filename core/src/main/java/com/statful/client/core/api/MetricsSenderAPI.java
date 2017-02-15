package com.statful.client.core.api;

import com.statful.client.domain.api.*;

import java.util.logging.Logger;

/**
 * This class is an implementation of the {@link SenderAPI},
 * which uses {@link MetricsSender} to send metrics.
 */
public final class MetricsSenderAPI implements SenderAPI {

    private static final Logger LOGGER = Logger.getLogger(MetricsSenderAPI.class.getName());

    private static final long TIMESTAMP_DIVIDER = 1000L;

    private MetricsSenderProxy metricsSenderProxy;
    private boolean aggregated;

    private String name;
    private String value;
    private String namespace;
    private Tags tags;
    private Integer sampleRate;
    private Aggregations aggregations;
    private AggregationFrequency aggregationFrequency;

    /**
     * Default constructor.
     *
     * @param metricsSender The {@link MetricsSender} to send metrics
     */
    MetricsSenderAPI(final MetricsSender metricsSender) {
        this.metricsSenderProxy = new MetricsSenderProxy(metricsSender);
        this.aggregated = false;
    }

    MetricsSenderAPI(final MetricsSender metricsSender, final boolean isAggregated) {
        this.metricsSenderProxy = new MetricsSenderProxy(metricsSender);
        this.aggregated = isAggregated;
    }

    /**
     * Builds a new instance of {@link SenderAPI} with the metrics sender.
     *
     * @param metricsSender The {@link MetricsSender}
     * @return An instance of {@link SenderAPI}
     */
    public static MetricsSenderAPI newInstance(final MetricsSender metricsSender) {
        return new MetricsSenderAPI(metricsSender, false);
    }

    /**
     * Builds a new instance of {@link SenderAPI} with the metrics sender.
     *
     * @param metricsSender The {@link MetricsSender}
     * @param isAggregated A {@link Boolean} flag stating if the metric is aggregated
     * @return An instance of {@link SenderAPI}
     */
    public static MetricsSenderAPI newInstance(final MetricsSender metricsSender, final boolean isAggregated) {
        return new MetricsSenderAPI(metricsSender, isAggregated);
    }

    /**
     * A getter for the aggregation flag.
     *
     * @return The aggregation flag
     */
    public boolean isAggregated() {
        return aggregated;
    }

    /**
     * A getter for the metric name.
     *
     * @return The metric name
     */
    public String getName() {
        return name;
    }

    /**
     * A getter for the metric value.
     *
     * @return The metric value
     */
    public String getValue() {
        return value;
    }

    /**
     * A getter for the metric namespace.
     *
     * @return The metric namespace
     */
    public String getNamespace() {
        return namespace;
    }

    /**
     * A getter for the metric tags.
     *
     * @return The metric tags
     */
    public Tags getTags() {
        return tags;
    }

    /**
     * A getter for the sample rate.
     *
     * @return The sample rate
     */
    public Integer getSampleRate() {
        return sampleRate;
    }

    /**
     * A getter for the aggregations.
     *
     * @return The aggregations
     */
    public Aggregations getAggregations() {
        return aggregations;
    }

    /**
     * A getter for the aggregation frequency.
     *
     * @return The aggregation frequency
     */
    public AggregationFrequency getAggregationFrequency() {
        return aggregationFrequency;
    }

    @Override
    public SenderAPI name(final String name) {
        if (isStringSafe(name)) {
            this.name = name;
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
            withNamespace(configuration.getNamespace()).sampleRate(configuration.getSampleRate());
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
    public SenderAPI namespace(final String namespace) {
        withNamespace(namespace);
        return this;
    }

    @Override
    public SenderAPI aggregation(final Aggregation aggregation) {
        withAggregation(aggregation);
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

    @Override
    public SenderAPI aggregationFrequency(final AggregationFrequency aggregationFrequency) {
        if (aggregationFrequency != null) {
            this.aggregationFrequency = aggregationFrequency;
        }
        return this;
    }

    @Override
    public SenderAPI with() {
        return this;
    }

    @Override
    public void send() {
        try {
            if (isValid()) {
                long unixTimestamp = getUnixTimestamp();

                metricsSenderProxy.put(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace,
                        unixTimestamp, aggregated);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please see the client documentation.");
            }
        } catch (Exception e) {
            LOGGER.severe("An exception has occurred while sending the metric to Statful: " + e.toString());
        }
    }

    @Override
    public void send(final long timestamp) {
        try {
            if (isValid()) {
                metricsSenderProxy.put(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace,
                        timestamp, aggregated);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please see the client documentation.");
            }
        } catch (Exception e) {
            LOGGER.severe("An exception has occurred while sending the metric to Statful: " + e.toString());
        }
    }

    private SenderAPI withNamespace(final String namespace) {
        if (isStringSafe(namespace)) {
            this.namespace = namespace;
        }
        return this;
    }

    private boolean isValid() {
        if (aggregated) {
            return isStringSafe(name) && isStringSafe(value) && isAggregatedMetricValid();
        }

        return isStringSafe(name) && isStringSafe(value);
    }

    private boolean isAggregatedMetricValid() {
        return aggregations.getAggregations().size() == 1;
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

    private boolean isStringSafe(final String string) {
        return string != null && !string.isEmpty();
    }

    private SenderAPI withAggregation(final Aggregation aggregation) {
        if (aggregation != null) {
            getSafeAggregations().put(aggregation);
        }
        return this;
    }

    private Aggregations getSafeAggregations() {
        if (aggregations == null) {
            aggregations = new Aggregations();
        }
        return aggregations;
    }
}
