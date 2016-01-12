package com.mindera.telemetron.client.api;

/**
 * This interface allows to send metrics (timer, counter, gauge or raw metric) to Telemetron.
 */
public interface TelemetronClient extends MetricsSender {

    /**
     * Creates a new Timer metrics builder.
     *
     * @param metricName The timer name to create
     * @param timestamp The timer timestamp value to send to Telemetron
     * @return A Timer metric builder, ready to be sent or configure
     */
    SenderFacade timer(final String metricName, final long timestamp);

    /**
     * Creates a new Counter metrics builder, which increments by one by default.
     *
     * @param metricName The counter name to create
     * @return A Counter metric builder, ready to be sent or configure
     */
    SenderFacade counter(final String metricName);

    /**
     * Creates a new Counter metrics builder.
     *
     * @param metricName The counter name to create
     * @param value The counter increment value to send to Telemetron
     * @return A Counter metric builder, ready to be sent or configure
     */
    SenderFacade counter(final String metricName, final int value);

    /**
     * Creates a new Gauge metrics builder.
     *
     * @param metricName The gauge name to create
     * @param value The gauge value to send to Telemetron
     * @return A Gauge metric builder, ready to be sent or configure
     */
    SenderFacade gauge(final String metricName, final String value);

    /**
     * Enables Telemetron client.
     */
    void enable();

    /**
     * Disables Telemetron client.
     */
    void disable();
}
