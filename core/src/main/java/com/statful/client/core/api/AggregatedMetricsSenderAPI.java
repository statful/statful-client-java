package com.statful.client.core.api;

import com.statful.client.domain.api.*;

import java.util.logging.Logger;

/**
 * This class is an implementation of the {@link SenderAPI},
 * which uses {@link MetricsSender} to send metrics.
 */
public final class AggregatedMetricsSenderAPI extends AbstractSenderAPI {

    private static final Logger LOGGER = Logger.getLogger(AggregatedMetricsSenderAPI.class.getName());

    private MetricsSender metricsSender;

    private Aggregation aggregation;
    private AggregationFreq aggregationFreq;

    /**
     * Default constructor.
     *
     * @param metricsSender The {@link MetricsSender} to send metrics
     */
    AggregatedMetricsSenderAPI(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Builds a new instance of {@link SenderAPI} with the metrics sender.
     *
     * @param metricsSender The {@link MetricsSender}
     * @return An instance of {@link SenderAPI}
     */
    public static AggregatedMetricsSenderAPI newInstance(final MetricsSender metricsSender) {
        return new AggregatedMetricsSenderAPI(metricsSender);
    }

    @Override
    public AggregatedMetricsSenderAPI aggregation(final Aggregation aggregation) {
        this.aggregation = aggregation;
        return this;
    }

    @Override
    public AggregatedMetricsSenderAPI aggregations(final Aggregation... aggregations) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AggregatedMetricsSenderAPI aggregations(final Aggregations aggregations) throws UnsupportedOperationException {
        throw new UnsupportedOperationException();
    }

    @Override
    public AggregatedMetricsSenderAPI aggFreq(final AggregationFreq aggregationFreq) {
        this.aggregationFreq = aggregationFreq;
        return this;
    }

    /**
     * A getter for the {@link Aggregation} of the metric.
     *
     * @return The {@link Aggregation} of the metric
     */
    public Aggregation getAggregation() {
        return aggregation;
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
    public AggregatedMetricsSenderAPI with() {
        return this;
    }

    @Override
    public void send() {
        try {
            if (isValid()) {
                long unixTimestamp = getUnixTimestamp();
                metricsSender.putAggregated(this.getMetricName(), this.getValue(), this.getTags(), aggregation,
                        aggregationFreq, this.getSampleRate(), this.getNamespace(), unixTimestamp);
            } else {
                LOGGER.warning("Unable to send metric because it's not valid. Please send metric name and value.");
            }
        } catch (Exception e) {
            LOGGER.warning("An exception has occurred while sending the metric to Statful: " + e.toString());
        }
    }
}
