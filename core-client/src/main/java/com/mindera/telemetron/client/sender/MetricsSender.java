package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.api.AggregationFreq;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;

/**
 * Interface that represents the metrics sender to Telemetron.
 */
public interface MetricsSender {

    /**
     * Puts a metric to be ready to sent to Telemetron. This can be done immediately, or by an asynchronous flush mechanism.
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link com.mindera.telemetron.client.api.Tags} the tags to be associated with the metric
     * @param aggregations {@link com.mindera.telemetron.client.api.Aggregations} with aggregations of the metric
     * @param aggregationFreq {@link com.mindera.telemetron.client.api.AggregationFreq} of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     */
    void put(String name, String value, Tags tags, Aggregations aggregations, AggregationFreq aggregationFreq,
             Integer sampleRate, String namespace, long timestamp);

    /**
     * Shutdowns the metrics sender. This typically means releasing resources or stopping flush mechanism.
     */
    void shutdown();
}
