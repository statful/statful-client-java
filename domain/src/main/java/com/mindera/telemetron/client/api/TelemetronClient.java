package com.mindera.telemetron.client.api;

/**
 * This interface allows to send metrics to Telemetron.
 *
 * @see #timer
 * @see #counter
 * @see #gauge
 * @see #put
 */
public interface TelemetronClient extends MetricsSender {

    /**
     * Creates a new timer metrics builder.
     *
     * @param metricName The timer name to create
     * @param timestamp The timer timestamp value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade timer(final String metricName, final long timestamp);

    /**
     * Creates a new counter metrics builder, which increments by one by default.
     *
     * @param metricName The counter name to create
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade counter(final String metricName);

    /**
     * Creates a new counter metrics builder.
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade counter(final String metricName, final int value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Long value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Double value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Float value);

    /**
     * Creates a new gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Telemetron
     * @return A {@link com.mindera.telemetron.client.api.SenderFacade}, ready to send or to configure a metric before sending
     */
    SenderFacade gauge(final String metricName, final Integer value);

    /**
     * Enables Telemetron client.
     */
    void enable();

    /**
     * Disables Telemetron client.
     */
    void disable();
}
