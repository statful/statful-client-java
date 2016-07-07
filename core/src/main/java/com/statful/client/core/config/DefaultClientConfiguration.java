package com.statful.client.core.config;

import com.statful.client.domain.api.*;

/**
 * This is thh default client configuration class.
 */
public class DefaultClientConfiguration implements ClientConfiguration {

    private static final int WORKER_POOL_SIZE = 1;
    private static final int CONNECTION_POOL_SIZE = 10;
    private static final int CONNECT_TIMEOUT_MS = 500;
    private static final int SOCKET_TIMEOUT_MS = 1000;

    private static final String DEFAULT_HOST = "127.0.0.1";
    private static final boolean DEFAULT_SECURE = true;
    private static final int DEFAULT_PORT = 2013;
    private static final int DEFAULT_SAMPLE_RATE = 100;
    private static final String DEFAULT_NAMESPACE = "application";
    private static final int DEFAULT_FLUSH_SIZE = 10;
    private static final int DEFAULT_FLUSH_INTERVAL_MS = 5000;
    private static final AggregationFreq DEFAULT_AGGREGATION_FREQ = AggregationFreq.FREQ_10;

    private static final Tags DEFAULT_APP_TAGS = Tags.from("statful_client", "java");
    private static final Tags DEFAULT_TIMER_TAGS = Tags.from("unit", "ms");

    private static final Aggregation[] DEFAULT_TIMER_AGGREGATIONS = new Aggregation[] {Aggregation.AVG, Aggregation.P90, Aggregation.COUNT};
    private static final Aggregation[] DEFAULT_COUNTER_AGGREGATIONS = new Aggregation[] {Aggregation.SUM, Aggregation.COUNT};
    private static final Aggregation[] DEFAULT_GAUGE_AGGREGATIONS = new Aggregation[] {Aggregation.LAST};

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private int sampleRate = DEFAULT_SAMPLE_RATE;
    private String namespace = DEFAULT_NAMESPACE;
    private int flushSize = DEFAULT_FLUSH_SIZE;
    private long flushIntervalMillis = DEFAULT_FLUSH_INTERVAL_MS;

    private boolean isDryRun;
    private boolean secure = DEFAULT_SECURE;
    private Transport transport;
    private String token;
    private String app;
    private int workersPoolSize = WORKER_POOL_SIZE;
    private int connectionPoolSize = CONNECTION_POOL_SIZE;
    private int connectTimeoutMillis = CONNECT_TIMEOUT_MS;
    private int socketTimeoutMillis = SOCKET_TIMEOUT_MS;

    private Tags applicationTags = Tags.from(DEFAULT_APP_TAGS);
    private Tags timerTags = Tags.from(DEFAULT_TIMER_TAGS);
    private Tags counterTags;
    private Tags gaugeTags;

    private Aggregations timerAggregations = Aggregations.from(DEFAULT_TIMER_AGGREGATIONS);
    private Aggregations counterAggregations = Aggregations.from(DEFAULT_COUNTER_AGGREGATIONS);
    private Aggregations gaugeAggregations = Aggregations.from(DEFAULT_GAUGE_AGGREGATIONS);

    private AggregationFreq timerAggregationFreq = DEFAULT_AGGREGATION_FREQ;
    private AggregationFreq counterAggregationFreq = DEFAULT_AGGREGATION_FREQ;
    private AggregationFreq gaugeAggregationFreq = DEFAULT_AGGREGATION_FREQ;

    @Override
    public final boolean isValid() {
        return transport != null;
    }

    @Override
    public final Tags getApplicationTags() {
        return applicationTags;
    }

    @Override
    public final int getSampleRate() {
        return sampleRate;
    }

    @Override
    public final String getNamespace() {
        return namespace;
    }

    @Override
    public final int getFlushSize() {
        return flushSize;
    }

    @Override
    public final long getFlushIntervalMillis() {
        return flushIntervalMillis;
    }

    @Override
    public final boolean isDryRun() {
        return isDryRun;
    }

    @Override
    public final boolean isSecure() {
        return secure;
    }

    @Override
    public final String getHost() {
        return host;
    }

    @Override
    public final int getPort() {
        return port;
    }

    @Override
    public final Transport getTransport() {
        return transport;
    }

    @Override
    public final String getToken() {
        return token;
    }

    @Override
    public final String getApp() {
        return app;
    }

    @Override
    public final int getWorkersPoolSize() {
        return workersPoolSize;
    }

    @Override
    public final int getConnectionPoolSize() {
        return connectionPoolSize;
    }

    @Override
    public final int getConnectTimeoutMillis() {
        return connectTimeoutMillis;
    }

    @Override
    public final int getSocketTimeoutMillis() {
        return socketTimeoutMillis;
    }

    @Override
    public final Tags getTimerTags() {
        return timerTags;
    }

    @Override
    public final Aggregations getTimerAggregations() {
        return timerAggregations;
    }

    @Override
    public final AggregationFreq getTimerAggregationFreq() {
        return timerAggregationFreq;
    }

    @Override
    public final Tags getCounterTags() {
        return counterTags;
    }

    @Override
    public final Aggregations getCounterAggregations() {
        return counterAggregations;
    }

    @Override
    public final AggregationFreq getCounterAggregationFreq() {
        return counterAggregationFreq;
    }

    @Override
    public final Tags getGaugeTags() {
        return gaugeTags;
    }

    @Override
    public final Aggregations getGaugeAggregations() {
        return gaugeAggregations;
    }

    @Override
    public final AggregationFreq getGaugeAggregationFreq() {
        return gaugeAggregationFreq;
    }

    /**
     * Setter for hostname.
     *
     * @param host The hostname
     */
    public final void setHost(final String host) {
        this.host = host;
    }

    /**
     * Setter for port.
     *
     * @param port The port
     */
    public final void setPort(final int port) {
        this.port = port;
    }

    /**
     * Setter for sample rate.
     *
     * @param sampleRate The sample rate
     */
    public final void setSampleRate(final int sampleRate) {
        this.sampleRate = sampleRate;
    }

    /**
     * Setter for namespace.
     *
     * @param namespace The namespace
     */
    public final void setNamespace(final String namespace) {
        this.namespace = namespace;
    }

    /**
     * Setter for flush size
     *
     * @param flushSize The flush size
     */
    public final void setFlushSize(final int flushSize) {
        this.flushSize = flushSize;
    }

    /**
     * Setter for secure.
     *
     * @param secure The secure flag
     */
    public final void setSecure(final boolean secure) {
        this.secure = secure;
    }

    /**
     * Setter for dry run
     *
     * @param isDryRun Boolean value for dry run
     */
    public final void setDryRun(final boolean isDryRun) {
        this.isDryRun = isDryRun;
    }

    /**
     * Setter for flush interval in milliseconds.
     *
     * @param flushIntervalMillis The flush interval in milliseconds
     */
    public final void setFlushIntervalMillis(final long flushIntervalMillis) {
        this.flushIntervalMillis = flushIntervalMillis;
    }

    /**
     * Setter for transport.
     *
     * @param transport The {@link com.statful.client.domain.api.Transport} to use
     */
    public final void setTransport(final Transport transport) {
        this.transport = transport;
    }

    /**
     * Setter for application token.
     *
     * @param token The token
     */
    public final void setToken(final String token) {
        this.token = token;
    }

    /**
     * Setter for app.
     * <p>
     * If set, it merges the app name with the application tags.
     *
     * @param app The app name
     */
    public final void setApp(final String app) {
        this.app = app;
        mergeTagIntoMethods("app", app);
    }

    /**
     * Setter for the worker pool size.
     *
     * @param workersPoolSize The poll size
     */
    public final void setWorkersPoolSize(final int workersPoolSize) {
        this.workersPoolSize = workersPoolSize;
    }

    /**
     * Setter for the connection pool size.
     *
     * @param connectionPoolSize Connection pool size
     */
    public final void setConnectionPoolSize(final int connectionPoolSize) {
        this.connectionPoolSize = connectionPoolSize;
    }

    /**
     * Setter for the connection timeout.
     *
     * @param connectTimeoutMillis connect timeout in milliseconds
     */
    public final void setConnectTimeoutMillis(final int connectTimeoutMillis) {
        this.connectTimeoutMillis = connectTimeoutMillis;
    }

    /**
     * Setter for the socket timeout.
     *
     * @param socketTimeoutMillis socket timeout in milliseconds
     */
    public final void setSocketTimeoutMillis(final int socketTimeoutMillis) {
        this.socketTimeoutMillis = socketTimeoutMillis;
    }

    /**
     * Setter for aggregation frequency for timer.
     *
     * @param timerAggregationFreq The {@link com.statful.client.domain.api.AggregationFreq} to use
     */
    public final void setTimerAggregationFreq(final AggregationFreq timerAggregationFreq) {
        this.timerAggregationFreq = timerAggregationFreq;
    }

    /**
     * Merges the tag defined by <code>type</code> and <code>value</code> with the existent timer tags.
     *
     * @param type The type of the tag to merge
     * @param value The value of the tag to merge
     */
    public final void mergeTimerTag(final String type, final String value) {
        this.timerTags.putTag(type, value);
    }

    /**
     * Merges the tag defined by <code>aggregation</code> with the existent timer aggregations.
     *
     * @param aggregation The {@link com.statful.client.domain.api.Aggregation} to merge
     */
    public final void mergeTimerAggregation(final Aggregation aggregation) {
        this.timerAggregations.put(aggregation);
    }

    /**
     * Setter for aggregation frequency for counter.
     *
     * @param counterAggregationFreq The {@link com.statful.client.domain.api.AggregationFreq} to use
     */
    public final void setCounterAggregationFreq(final AggregationFreq counterAggregationFreq) {
        this.counterAggregationFreq = counterAggregationFreq;
    }

    /**
     * Merges the tag defined by <code>type</code> and <code>value</code> with the existent counter tags.
     *
     * @param type The type of the tag to merge
     * @param value The value of the tag to merge
     */
    public final void mergeCounterTag(final String type, final String value) {
        getSafeCounterTags().putTag(type, value);
    }

    /**
     * Merges the tag defined by <code>aggregation</code> with the existent counter aggregations.
     *
     * @param aggregation The {@link com.statful.client.domain.api.Aggregation} to merge
     */
    public final void mergeCounterAggregation(final Aggregation aggregation) {
        this.counterAggregations.put(aggregation);
    }

    /**
     * Setter for aggregation frequency for gauge.
     *
     * @param gaugeAggregationFreq The {@link com.statful.client.domain.api.AggregationFreq} to use
     */
    public final void setGaugeAggregationFreq(final AggregationFreq gaugeAggregationFreq) {
        this.gaugeAggregationFreq = gaugeAggregationFreq;
    }

    /**
     * Merges the tag defined by <code>type</code> and <code>value</code> with the existent gauge tags.
     *
     * @param type The type of the tag to merge
     * @param value The value of the tag to merge
     */
    public final void mergeGaugeTag(final String type, final String value) {
        getSafeGaugeTags().putTag(type, value);
    }

    /**
     * Merges the tag defined by <code>aggregation</code> with the existent gauge aggregations.
     *
     * @param aggregation The {@link com.statful.client.domain.api.Aggregation} to merge
     */
    public final void mergeGaugeAggregation(final Aggregation aggregation) {
        this.gaugeAggregations.put(aggregation);
    }

    /**
     * Merges the tag defined by <code>type</code> and <code>value</code> with the existent application tags.
     *
     * @param type The type of the tag to merge
     * @param value The value of the tag to merge
     */
    public final void mergeApplicationTag(final String type, final String value) {
        applicationTags.putTag(type, value);
        mergeTagIntoMethods(type, value);
    }

    private void mergeTagIntoMethods(final String type, final String value) {
        timerTags.putTag(type, value);
        getSafeCounterTags().putTag(type, value);
        getSafeGaugeTags().putTag(type, value);
    }

    private Tags getSafeCounterTags() {
        if (counterTags == null) {
            counterTags = new Tags();
        }
        return counterTags;
    }

    private Tags getSafeGaugeTags() {
        if (gaugeTags == null) {
            gaugeTags = new Tags();
        }
        return gaugeTags;
    }
}
