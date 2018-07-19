package com.statful.client.domain.api;

/**
 * Interface that represents the metrics sender to Statful.
 */
public interface MetricsSender {

    /**
     * Puts a metric to be ready to sent to Statful. This can be done immediately, or by an asynchronous flush mechanism.
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link Tags} the tags to be associated with the metric
     * @param aggregations {@link Aggregations} with aggregations of the metric
     * @param aggregationFrequency {@link AggregationFrequency} of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     */
    void put(String name, String value, Tags tags, Aggregations aggregations, AggregationFrequency aggregationFrequency,
             Integer sampleRate, String namespace, long timestamp);

    /**
     * Puts a metric to be ready to sent to Statful. This can be done immediately, or by an asynchronous flush mechanism.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link Tags} the tags to be associated with the metric
     * @param aggregations {@link Aggregations} with aggregations of the metric
     * @param aggregationFrequency {@link AggregationFrequency} of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     */
    void putSampled(String name, String value, Tags tags, Aggregations aggregations, AggregationFrequency aggregationFrequency,
                    Integer sampleRate, String namespace, long timestamp);

    /**
     * Puts a metric to be ready to sent to Statful. This can be done immediately, or by an asynchronous flush mechanism.
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link Tags} the tags to be associated with the metric
     * @param aggregation A {@link Aggregation} aggregation of the metric
     * @param aggregationFrequency A {@link AggregationFrequency} aggregation frequency of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     */
    void aggregatedPut(String name, String value, Tags tags, Aggregation aggregation, AggregationFrequency aggregationFrequency,
                       Integer sampleRate, String namespace, long timestamp);

    /**
     * Puts a metric to be ready to sent to Statful. This can be done immediately, or by an asynchronous flush mechanism.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param name The name of the metric
     * @param value The value of the metric
     * @param tags A {@link Tags} the tags to be associated with the metric
     * @param aggregation A {@link Aggregation} aggregation of the metric
     * @param aggregationFrequency A {@link AggregationFrequency} aggregation frequency of the metric
     * @param sampleRate The metrics sample rate
     * @param namespace The namespace of the metric
     * @param timestamp The timestamp associated with the metric
     */
    void aggregatedSampledPut(String name, String value, Tags tags, Aggregation aggregation, AggregationFrequency aggregationFrequency,
                              Integer sampleRate, String namespace, long timestamp);

    /**
     * Forces synchronous flush of metrics. This method blocks the caller.
     */
    void forceSyncFlush();

    /**
     * Shutdowns the metrics sender. This typically means releasing resources or stopping flush mechanism.
     */
    void shutdown();
}
