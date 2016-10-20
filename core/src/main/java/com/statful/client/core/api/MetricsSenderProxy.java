package com.statful.client.core.api;

import com.statful.client.domain.api.*;

/**
 * Proxy class for a MetricsSender implementation. Has logic to handle aggregated metrics.
 */
public class MetricsSenderProxy {
    private MetricsSender metricsSender;

    /**
     * Default constructor.
     * @param metricsSender A {@link MetricsSender} implementation
     */
    public MetricsSenderProxy(final MetricsSender metricsSender) {
        this.metricsSender = metricsSender;
    }

    /**
     * Proxies the put method of the {@link MetricsSender} interface.
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link Tags} the tags to be associated with the metric
     * @param aggregations {@link Aggregations} with aggregations of the metric
     * @param aggregationFrequency {@link AggregationFrequency} of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     * @param isAggregated Flag stating if the metric is aggregated
     */
    public final void put(final String name, final String value, final Tags tags, final Aggregations aggregations,
                          final AggregationFrequency aggregationFrequency, final Integer sampleRate,
                          final String namespace, final long timestamp, final boolean isAggregated) {
        if (isAggregated) {
            Aggregation aggregation = aggregations.getAggregations().iterator().next();

            metricsSender.aggregatedPut(name, value, tags, aggregation, aggregationFrequency, sampleRate, namespace,
                    timestamp);
        } else {
            metricsSender.put(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace, timestamp);
        }
    }
}
