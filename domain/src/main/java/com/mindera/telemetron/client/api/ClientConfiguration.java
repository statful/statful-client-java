package com.mindera.telemetron.client.api;

/**
 * The Telemetron client configuration interface.
 */
public interface ClientConfiguration {

    /**
     * Returns if the configuration is valid. The only required options are the prefix and the transport type.
     *
     * @return True if the client configuration is valid
     */
    boolean isValid();

    /**
     * Returns the global list of application tags.
     *
     * @return A {@link com.mindera.telemetron.client.api.Tags} of the application
     */
    Tags getApplicationTags();

    /**
     * Returns the global rate sampling. The default is 100.
     *
     * @return Sample rate as integer
     */
    int getSampleRate();

    /**
     * Returns the default namespace (can be overridden in method calls). The default is 'application'.
     *
     * @return Namespace as string
     */
    String getNamespace();

    /**
     * Returns the global metrics prefix. Mandatory to configure.
     *
     * @return Prefix as string
     */
    String getPrefix();

    /**
     * Returns the periodicity of buffer flushes in buffer size. Default is 10.
     *
     * @return Flush size as integer.
     */
    int getFlushSize();

    /**
     * Returns the periodicity of buffer flushes in time period (milliseconds). Default is 0, which is disabled.
     *
     * @return Flush interval as long
     */
    long getFlushIntervalMillis();

    /**
     * Returns if actually the metrics are sent when flushing the buffer.
     *
     * @return True if the run is on dry mode
     */
    boolean isDryRun();

    /**
     * Returns the hostname to use, independently of the transport. Default is '127.0.0.1'.
     *
     * @return Hostname as string
     */
    String getHost();

    /**
     * Returns the port to use, independently of the transport. Default is '2013'.
     *
     * @return Port as integer
     */
    int getPort();

    /**
     * Returns the timer default tags. Default is 'unit = ms'.
     *
     * @return A {@link com.mindera.telemetron.client.api.Tags} for timer
     */
    Tags getTimerTags();

    /**
     * Returns the timer default aggregations. Default are: {@link com.mindera.telemetron.client.api.Aggregation#AVG},
     * {@link com.mindera.telemetron.client.api.Aggregation#P90} and
     * {@link com.mindera.telemetron.client.api.Aggregation#COUNT}.
     *
     * @return Default {@link com.mindera.telemetron.client.api.Aggregations} for timer
     */
    Aggregations getTimerAggregations();

    /**
     * Returns the timer default aggregation frequency. Default is
     * {@link com.mindera.telemetron.client.api.AggregationFreq#FREQ_10}.
     *
     * @return {@link com.mindera.telemetron.client.api.AggregationFreq} for timer
     */
    AggregationFreq getTimerAggregationFreq();

    /**
     * Returns the counter default tags.
     *
     * @return A {@link com.mindera.telemetron.client.api.Tags} for counter
     */
    Tags getCounterTags();

    /**
     * Returns the counter default aggregations. Default are: {@link com.mindera.telemetron.client.api.Aggregation#AVG}
     * and {@link com.mindera.telemetron.client.api.Aggregation#P90}.
     *
     * @return Default {@link com.mindera.telemetron.client.api.Aggregations} for counter
     */
    Aggregations getCounterAggregations();

    /**
     * Returns the counter default aggregation frequency. Default is
     * {@link com.mindera.telemetron.client.api.AggregationFreq#FREQ_10}.
     *
     * @return {@link com.mindera.telemetron.client.api.AggregationFreq} for counter
     */
    AggregationFreq getCounterAggregationFreq();

    /**
     * Returns the gauge default tags.
     *
     * @return A {@link com.mindera.telemetron.client.api.Tags} for gauge
     */
    Tags getGaugeTags();

    /**
     * Returns the gauge default aggregations. Default is: {@link com.mindera.telemetron.client.api.Aggregation#LAST}.
     *
     * @return Default {@link com.mindera.telemetron.client.api.Aggregations} for gauge
     */
    Aggregations getGaugeAggregations();

    /**
     * Returns the gauge default aggregation frequency. Default is 10.
     *
     * @return {@link com.mindera.telemetron.client.api.AggregationFreq} for gauge
     */
    AggregationFreq getGaugeAggregationFreq();

    /**
     * Returns the transport to use. Mandatory to configure.
     *
     * @return {@link com.mindera.telemetron.client.api.Transport} to use to send the metrics
     */
    Transport getTransport();

    /**
     * Returns the application token.
     *
     * @return Token as string
     */
    String getToken();

    /**
     * Returns the application name. If specified set a tag ‘app=foo’
     *
     * @return App as string
     */
    String getApp();

    /**
     * Default is 1.
     *
     * @return Size as int
     */
    int getWorkersPoolSize();
}
