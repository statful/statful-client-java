package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;

/**
 * This is the metrics sender API, which is exposed to allow building metrics and sending them to Telemetron with a
 * builder-like syntax.
 */
public interface SenderAPI {
    /**
     * Just a syntax sugar method.
     *
     * @return A reference to this instance
     */
    SenderAPI with();

    /**
     * Sets the metric name.
     *
     * @param metricName Metric name as string
     * @return A reference to this instance
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
     * Sets the Telemetron {@link com.mindera.telemetron.client.config.ClientConfiguration} to use.
     *
     * @param configuration The {@link com.mindera.telemetron.client.config.ClientConfiguration} to use
     * @return A reference to this instance
     */
    SenderAPI configuration(final ClientConfiguration configuration);

    /**
     * Sets the metric sample rate.
     *
     * @param sampleRate Sample rate as integer
     * @return A reference to this instance
     */
    SenderAPI sampleRate(final Integer sampleRate);

    /**
     * Sets a tag to the metric
     *
     * @param type The tag type
     * @param value The tag value
     * @return A reference to this instance
     */
    SenderAPI tag(final String type, final String value);

    /**
     * Sets the {@link com.mindera.telemetron.client.api.Tags} of the metric.
     *
     * @param tags The {@link com.mindera.telemetron.client.api.Tags} to use
     * @return A reference to this instance
     */
    SenderAPI tags(final Tags tags);

    /**
     * Sets an array of {@link com.mindera.telemetron.client.api.Aggregation} of the metric.
     *
     * @param aggregations An array of {@link com.mindera.telemetron.client.api.Aggregation} to use
     * @return A reference to this instance
     */
    SenderAPI aggregations(final Aggregation... aggregations);

    /**
     * Sets the {@link com.mindera.telemetron.client.api.Aggregations} of the metric.
     *
     * @param aggregations The {@link com.mindera.telemetron.client.api.Aggregations} to use
     * @return A reference to this instance
     */
    SenderAPI aggregations(final Aggregations aggregations);

    /**
     * Sets the {@link com.mindera.telemetron.client.api.AggregationFreq} of the metric.
     *
     * @param aggFreq The {@link com.mindera.telemetron.client.api.AggregationFreq} of the metric
     * @return A reference to this instance
     */
    SenderAPI aggFreq(final AggregationFreq aggFreq);

    /**
     * Sets the namespace of the metric.
     *
     * @param namespace Namespace as string
     * @return A reference to this instance
     */
    SenderAPI namespace(final String namespace);

    /**
     * Sends the metric to Telemetron.
     */
    void send();
}
