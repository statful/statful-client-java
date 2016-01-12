package com.mindera.telemetron.client.api;

/**
 * This is the metrics sender API, which is exposed to allow building metrics and sending them to Telemetron.
 */
public interface SenderAPI {
    /**
     * Just a syntax sugar method.
     *
     * @return A reference to this builder
     */
    SenderAPI with();

    /**
     * Sets the metric name.
     *
     * @param metricName Metric name as string
     * @return A reference to this builder
     */
    SenderAPI metricName(final String metricName);

    /**
     * Sets the metric value.
     *
     * @param value The value as string
     * @return A reference to this builder
     */
    SenderAPI value(final String value);

    /**
     * Sets the Telemetron client configuration to use.
     *
     * @param configuration Client configuration
     * @return A reference to this builder
     */
    SenderAPI configuration(final ClientConfiguration configuration);

    /**
     * Sets the metric sample rate.
     *
     * @param sampleRate Sample rate as integer
     * @return A reference to this builder
     */
    SenderAPI sampleRate(final Integer sampleRate);

    /**
     * Sets a tag to the metric
     *
     * @param type The tag type
     * @param value The tag value
     * @return A reference to this builder
     */
    SenderAPI tag(final String type, final String value);

    /**
     * Sets a Tags object to the metric. Which can be a collection.
     *
     * @param tags Tags to use
     * @return A reference to this builder
     */
    SenderAPI tags(final Tags tags);

    /**
     * Sets aggregations to the metric.
     *
     * @param aggregations Array of aggregations to use
     * @return A reference to this builder
     */
    SenderAPI aggregations(final Aggregation... aggregations);

    /**
     * Sets an Aggregations object to the metric, which can be a collection.
     *
     * @param aggregations Aggregations to use
     * @return A reference to this builder
     */
    SenderAPI aggregations(final Aggregations aggregations);

    /**
     * Sets the aggregation frequency of the metric.
     *
     * @param aggFreq Aggregation frequency (10, 30, 60, 120, 180, 300)
     * @return A reference to this builder
     */
    SenderAPI aggFreq(final AggregationFreq aggFreq);

    /**
     * Sets the namespace of the metric.
     *
     * @param namespace Namespace as string
     * @return A reference to this builder
     */
    SenderAPI namespace(final String namespace);

    /**
     * Sends the metric to Telemetron.
     */
    void send();
}
