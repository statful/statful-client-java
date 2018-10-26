package com.statful.client.domain.api;

/**
 * This is the metrics sender API, which is exposed to allow building metrics and sending them to Statful with a
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
     * @param name Metric name as string
     * @return A reference to this instance
     */
    SenderAPI name(final String name);

    /**
     * Sets the metric value.
     *
     * @param value The value as string
     * @return A reference to this builder
     */
    SenderAPI value(final String value);

    /**
     * Sets the Statful {@link ClientConfiguration} to use.
     *
     * @param configuration The {@link ClientConfiguration} to use
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
     * Sets the {@link Tags} of the metric.
     *
     * @param tags The {@link Tags} to use
     * @return A reference to this instance
     */
    SenderAPI tags(final Tags tags);

    /**
     * Sets the {@link Aggregation} applied to the metric.
     *
     * @param aggregation {@link Aggregation} applied to the metric
     * @return A reference to this instance
     */
    SenderAPI aggregation(final Aggregation aggregation);

    /**
     * Sets an array of {@link Aggregation} of the metric.
     *
     * @param aggregations An array of {@link Aggregation} to use
     * @return A reference to this instance
     */
    SenderAPI aggregations(final Aggregation... aggregations);

    /**
     * Sets the {@link Aggregations} of the metric.
     *
     * @param aggregations The {@link Aggregations} to use
     * @return A reference to this instance
     */
    SenderAPI aggregations(final Aggregations aggregations);

    /**
     * Sets the {@link AggregationFrequency} of the metric.
     *
     * @param aggregationFrequency The {@link AggregationFrequency} of the metric
     * @return A reference to this instance
     */
    SenderAPI aggregationFrequency(final AggregationFrequency aggregationFrequency);

    /**
     * Sets the namespace of the metric.
     *
     * @param namespace Namespace as string
     * @return A reference to this instance
     */
    SenderAPI namespace(final String namespace);

    /**
     * Sets the unix timestamp (in seconds) of the metric.
     *
     * @param timestamp Timestamp as long
     * @return A reference to this instance
     */
    SenderAPI timestamp(final Long timestamp);

    /**
     * Sends the metric to Statful.
     */
    void send();
}
