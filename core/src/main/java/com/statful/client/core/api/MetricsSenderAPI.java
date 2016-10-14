package com.statful.client.core.api;

import com.statful.client.domain.api.*;

import java.util.logging.Logger;

/**
 * This class is an implementation of the {@link com.statful.client.domain.api.SenderAPI},
 * which uses {@link com.statful.client.domain.api.MetricsSender} to send metrics.
 */
public final class MetricsSenderAPI extends AbstractSenderAPI {

    private static final Logger LOGGER = Logger.getLogger(MetricsSenderAPI.class.getName());

    private MetricsSender metricsSender;

    private Aggregations aggregations;
    private AggregationFreq aggregationFreq;

    /**
     * Default constructor.
     *
     * @param metricsSender The {@link MetricsSender} to send metrics
     */
    MetricsSenderAPI(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Builds a new instance of {@link SenderAPI} with the metrics sender.
     *
     * @param metricsSender The {@link MetricsSender}
     * @return An instance of {@link SenderAPI}
     */
    public static MetricsSenderAPI newInstance(final MetricsSender metricsSender) {
        return new MetricsSenderAPI(metricsSender);
    }

    @Override
    public MetricsSenderAPI aggregations(final Aggregation... aggregations) {
        if (aggregations != null) {
            for (Aggregation aggregation : aggregations) {
                withAggregation(aggregation);
            }
        }
        return this;
    }

    @Override
    public MetricsSenderAPI aggregations(final Aggregations aggregations) {
        if (aggregations != null) {
            getSafeAggregations().merge(aggregations);
        }
        return this;
    }

    private MetricsSenderAPI withAggregation(final Aggregation aggregation) {
        if (aggregation != null) {
            getSafeAggregations().put(aggregation);
        }
        return this;
    }

    @Override
    public MetricsSenderAPI aggFreq(final AggregationFreq aggFreq) {
        if (aggFreq != null) {
            aggregationFreq = aggFreq;
        }
        return this;
    }

    private Aggregations getSafeAggregations() {
        if (aggregations == null) {
            aggregations = new Aggregations();
        }
        return aggregations;
    }

    /**
     * A getter for the {@link Aggregations} of the metric.
     *
     * @return The {@link Aggregations} of the metric
     */
    public Aggregations getAggregations() {
        return aggregations;
    }

    /**
     * A getter for the {@link AggregationFreq} of the metric.
     *
     * @return The {@link AggregationFreq} of the metric.
     */
    public AggregationFreq getAggregationFreq() {
        return aggregationFreq;
    }

    @Override
    public MetricsSenderAPI with() {
        return this;
    }

    @Override
    public MetricsSenderAPI aggregation(final Aggregation aggregation) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void send() {
        try {
            if (isValid()) {
                long unixTimestamp = getUnixTimestamp();
                metricsSender.put(this.getMetricName(), this.getValue(), this.getTags(), this.getAggregations(),
                        this.getAggregationFreq(), this.getSampleRate(), this.getNamespace(), unixTimestamp);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please send metric name and value.");
            }
        } catch (Exception e) {
            LOGGER.warning("An exception has occurred while sending the metric to Statful: " + e.toString());
        }
    }
}
