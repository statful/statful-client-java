package com.mindera.telemetron.client.config;

import com.mindera.telemetron.client.api.AggregationFreq;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.api.Transport;

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
     * @return A Tags object containing the tags
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
     * Returns the timer default tags. Default is 'unit => ms'.
     *
     * @return Tags object, which may contain a collection
     */
    Tags getTimerTags();

    /**
     * Returns the timer default aggregations. Default are: AVG, P90, COUNT, COUNT_PS.
     *
     * @return Aggregations object, which may contain a collection
     */
    Aggregations getTimerAggregations();

    /**
     * Returns the timer default aggregation frequency. Default is 10.
     *
     * @return Aggregation frequency (10, 30, 60, 120, 180, 300)
     */
    AggregationFreq getTimerAggregationFreq();

    /**
     * Returns the counter default tags.
     *
     * @return Tags object, which may contain a collection
     */
    Tags getCounterTags();

    /**
     * Returns the counter default aggregations. Default are: AVG, P90, COUNT_PS.
     *
     * @return Aggregations object, which may contain a collection
     */
    Aggregations getCounterAggregations();

    /**
     * Returns the counter default aggregation frequency. Default is 10.
     *
     * @return Aggregation frequency (10, 30, 60, 120, 180, 300)
     */
    AggregationFreq getCounterAggregationFreq();

    /**
     * Returns the gauge default tags.
     *
     * @return Tags object, which may contain a collection
     */
    Tags getGaugeTags();

    /**
     * Returns the gauge default aggregations. Default are: LAST.
     *
     * @return Aggregations object, which may contain a collection
     */
    Aggregations getGaugeAggregations();

    /**
     * Returns the gauge default aggregation frequency. Default is 10.
     *
     * @return Aggregation frequency (10, 30, 60, 120, 180, 300)
     */
    AggregationFreq getGaugeAggregationFreq();

    /**
     * Returns the transport to use. Mandatory to configure. (UDP, TCP or HTTP).
     *
     * @return Transport to use to send the metrics
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
     * @return
     */
    int getWorkersPoolSize();
}
