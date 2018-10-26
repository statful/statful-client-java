package com.statful.client.domain.api;

/**
 * This interface allows to send metrics to Statful.
 *
 * @see #timer
 * @see #counter
 * @see #gauge
 * @see #put
 */
public interface StatfulClient extends MetricsSender {

    /**
     * Creates a new timer metrics builder.
     *
     * @param metricName The timer name to create
     * @param value The timer value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade timer(final String metricName, final long value);

    /**
     * Creates a new counter metrics builder, which increments by one by default.
     *
     * @param metricName The counter name to create
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade counter(final String metricName);

    /**
     * Creates a new counter metrics builder.
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade counter(final String metricName, final int value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Long value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Double value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Float value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Integer value);

    /**
     * Creates a new simple put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade put(final String metricName, final Long value);

    /**
     * Creates a new simple put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade put(final String metricName, final Double value);

    /**
     * Creates a new simple put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade put(final String metricName, final Float value);

    /**
     * Creates a new simple put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade put(final String metricName, final Integer value);

    /**
     * Creates a new timer metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The timer name to create
     * @param value The timer value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledTimer(final String metricName, final long value, final Integer sampleRate);

    /**
     * Creates a new counter metrics builder, which increments by one by default.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The counter name to create
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledCounter(final String metricName, final Integer sampleRate);

    /**
     * Creates a new counter metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledCounter(final String metricName, final int value, final Integer sampleRate);

    /**
     * Creates a new gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledGauge(final String metricName, final Long value, final Integer sampleRate);

    /**
     * Creates a new gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledGauge(final String metricName, final Double value, final Integer sampleRate);

    /**
     * Creates a new gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledGauge(final String metricName, final Float value, final Integer sampleRate);

    /**
     * Creates a new gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledGauge(final String metricName, final Integer value, final Integer sampleRate);

    /**
     * Creates a new simple put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledPut(final String metricName, final Long value, final Integer sampleRate);

    /**
     * Creates a new simple put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledPut(final String metricName, final Double value, final Integer sampleRate);

    /**
     * Creates a new simple put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledPut(final String metricName, final Float value, final Integer sampleRate);

    /**
     * Creates a new simple put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledPut(final String metricName, final Integer value, final Integer sampleRate);

    /**
     * Creates a new aggregated timer metrics builder.
     *
     * @param metricName The timer name to create
     * @param timestamp The timer timestamp value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedTimer(final String metricName, final long timestamp, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated counter metrics builder.
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedCounter(final String metricName, final int value, final Aggregation aggregation,
                                   final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedGauge(final String metricName, final Long value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedGauge(final String metricName, final Double value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedGauge(final String metricName, final Float value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedGauge(final String metricName, final Integer value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new simple aggregated put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedPut(final String metricName, final Long value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new simple aggregated put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedPut(final String metricName, final Double value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new simple aggregated put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedPut(final String metricName, final Float value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new simple aggregated put builder.
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade aggregatedPut(final String metricName, final Integer value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency);

    /**
     * Creates a new aggregated timer metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The timer name to create
     * @param timestamp The timer timestamp value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedTimer(final String metricName, final long timestamp, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new aggregated counter metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedCounter(final String metricName, final int value, final Aggregation aggregation,
                                   final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedGauge(final String metricName, final Long value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedGauge(final String metricName, final Double value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedGauge(final String metricName, final Float value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new aggregated gauge metrics builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedGauge(final String metricName, final Integer value, final Aggregation aggregation,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new simple aggregated put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedPut(final String metricName, final Long value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new simple aggregated put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedPut(final String metricName, final Double value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new simple aggregated put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedPut(final String metricName, final Float value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Creates a new simple aggregated put builder.
     *
     * This metric is considered to have been already sampled so no sampling will be applied by the client and will just
     * push that information to the server when flushing
     *
     * @param metricName The metric name to create
     * @param value The metric value to send to Statful
     * @param aggregation The aggregation applied
     * @param aggregationFrequency The aggregation frequency applied
     * @param sampleRate The sample rate applied
     * @return A {@link SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade sampledAggregatedPut(final String metricName, final Integer value, final Aggregation aggregation,
                               final AggregationFrequency aggregationFrequency, final Integer sampleRate);

    /**
     * Enables Statful client.
     */
    void enable();

    /**
     * Disables Statful client.
     */
    void disable();
}
