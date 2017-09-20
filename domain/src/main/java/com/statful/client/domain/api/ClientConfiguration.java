package com.statful.client.domain.api;

/**
 * The Statful client configuration interface.
 */
public interface ClientConfiguration {

    String TOKEN_HEADER = "M-Api-Token";

    /**
     * Returns if the configuration is valid. The only required option is the transport type.
     *
     * @return True if the client configuration is valid
     */
    boolean isValid();

    /**
     * Returns the global list of application tags.
     *
     * @return A {@link Tags} of the application
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
     * Returns weather the transport os messages is secure. This is only applied to HTTPS.
     *
     * @return True if the transport method is secure
     */
    boolean isSecure();

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
     * Returns the path to use.
     *
     * @return {@code String} path
     */
    String getPath();

    /**
     * Returns the timer default tags. Default is 'unit = ms'.
     *
     * @return A {@link Tags} for timer
     */
    Tags getTimerTags();

    /**
     * Returns the timer default aggregations. Default are: {@link Aggregation#AVG},
     * {@link Aggregation#P90} and
     * {@link Aggregation#COUNT}.
     *
     * @return Default {@link Aggregations} for timer
     */
    Aggregations getTimerAggregations();

    /**
     * Returns the timer default aggregation frequency. Default is
     * {@link AggregationFrequency#FREQ_10}.
     *
     * @return {@link AggregationFrequency} for timer
     */
    AggregationFrequency getTimerAggregationFrequency();

    /**
     * Returns the counter default tags.
     *
     * @return A {@link Tags} for counter
     */
    Tags getCounterTags();

    /**
     * Returns the counter default aggregations. Default are: {@link Aggregation#AVG}
     * and {@link Aggregation#P90}.
     *
     * @return Default {@link Aggregations} for counter
     */
    Aggregations getCounterAggregations();

    /**
     * Returns the counter default aggregation frequency. Default is
     * {@link AggregationFrequency#FREQ_10}.
     *
     * @return {@link AggregationFrequency} for counter
     */
    AggregationFrequency getCounterAggregationFrequency();

    /**
     * Returns the gauge default tags.
     *
     * @return A {@link Tags} for gauge
     */
    Tags getGaugeTags();

    /**
     * Returns the gauge default aggregations. Default is: {@link Aggregation#LAST}.
     *
     * @return Default {@link Aggregations} for gauge
     */
    Aggregations getGaugeAggregations();

    /**
     * Returns the gauge default aggregation frequency. Default is 10.
     *
     * @return {@link AggregationFrequency} for gauge
     */
    AggregationFrequency getGaugeAggregationFrequency();

    /**
     * Returns the default aggregation frequency. Default is
     * {@link AggregationFrequency#FREQ_10}.
     *
     * @return {@link AggregationFrequency} for counter
     */
    AggregationFrequency getDefaultAggregationFreq();

    /**
     * Returns the transport to use. Mandatory to configure.
     *
     * @return {@link Transport} to use to send the metrics
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

    /**
     * Only valid for TCP transports (HTTP and HTTPS).
     * Default is 10.
     *
     * @return Size as int
     */
    int getConnectionPoolSize();

    /**
     * Only valid for TCP transports (HTTP and HTTPS).
     *
     * @return Timeout as int
     */
    int getConnectTimeoutMillis();

    /**
     * Only valid for TCP transports (HTTP and HTTPS).
     *
     * @return Timeout as int
     */
    int getSocketTimeoutMillis();

    /**
     * Returns the maximum size of the queue with the worker tasks.
     *
     * @return Size as int
     */
    int getMaxWorkerTasksQueueSize();

    /**
     * Returns the interval for killing hanging worker tasks.
     *
     * @return Interval in milliseconds
     */
    long getWorkerTaskKillerInterval();
}
