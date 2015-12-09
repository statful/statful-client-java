package com.mindera.telemetron.client;

import java.util.HashMap;
import java.util.logging.Logger;

public class TelemetronClient {

    private TelemetronConfig config;
    private MetricsConfig counterConfig = new CounterConfig();
    private MetricsConfig timerConfig = new TimerConfig();
    private MetricsConfig gaugeConfig = new GaugeConfig();

    private Logger logger;

    private String[] buffer;
    private int bufferPos = 0;

    private MessageBuilder msgBuilder;

    public TelemetronClient() {
        this(new TelemetronConfig());
    }

    /**
     * Telemetron client constructor. Receives a configuration object as parameter to step the client settings.
     * @param config configuration object with all attributes required to bootstrap the TelemetronClient
     * @throws IllegalArgumentException when the configuration is null
     * @throws TelemetronConfigException when the configuration is not valid
     */
    public TelemetronClient(TelemetronConfig config) {
        config.validate();

        if (config.getMetricsDefaults() != null) {
            this.counterConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_COUNTER));
            this.timerConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_TIMER));
            this.gaugeConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_GAUGE));
        }

        this.config = config;

        this.buffer = new String[this.config.getFlushSize()];
        this.logger = config.getLogger() != null ?
                config.getLogger() :
                Logger.getLogger(TelemetronClient.class.getName());

        this.msgBuilder = new MessageBuilder();
    }

    /**
     * Register a Timer metric
     * @param name name of the timer
     * @param time time to be registered as value of the metric
     */
    public void addTimer(String name, long time) {
        this.addTimer(name, time);
    }

    /**
     * Register a Timer metric
     * @param name name of the timer
     * @param time time to be registered as value of the metric
     * @param config the configurations to apply to the metric
     */
    public void addTimer(String name, long time, MetricsConfig config) {
        if (name == null) {
            throw new IllegalArgumentException("name is required");
        }

        MetricsConfig finalConfig = timerConfig.mergeToNewInstance(config);
        this.putRaw(msgBuilder.buildMessage(name, time, this.config, finalConfig));
    }

    /**
     * Register a Gauge metric
     * @param name name of the gauge
     * @param value value of the metric
     */
    public void addGauge(String name, long value) {
        this.addGauge(name, value);
    }

    /**
     * Register a Gauge metric
     * @param name name of the gauge
     * @param value value of the metric
     * @param config the configurations to apply to the metric
     */
    public void addGauge(String name, long value, MetricsConfig config) {
        if (name == null) {
            throw new IllegalArgumentException("name is required");
        }

        MetricsConfig finalConfig = gaugeConfig.mergeToNewInstance(config);
        this.putRaw(msgBuilder.buildMessage(name, value, this.config, finalConfig));
    }

    /**
     * Register a Counter metric
     * @param name name of the counter
     * @param value value of the metric
     */
    public void addCounter(String name, long value) {
        this.addCounter(name, value, null);
    }

    /**
     * Register a Counter metric
     * @param name name of the counter
     * @param value value of the metric
     * @param config the configurations to apply to the metric
     */
    public void addCounter(String name, long value, MetricsConfig config) {
        if (name == null) {
            throw new IllegalArgumentException("name is required");
        }

        MetricsConfig finalConfig = counterConfig.mergeToNewInstance(config);
        this.putRaw(msgBuilder.buildMessage(name, value, this.config, finalConfig));
    }

    /**
     * Add metric without using any specific metric type.
     * @param name name of the metric
     * @param value value of the metric
     * @param tags tags to classify the metric
     * @param aggr aggregations to use
     * @param aggrFreq aggregation frequency to apply to the aggregations
     * @param sampleRate sample rate
     * @param namespace metric namespace
     * @param timestamp metric timestamp
     */
    public void put(String name, long value, HashMap<String, String> tags, String[] aggr, Integer aggrFreq,
                    Integer sampleRate, String namespace, long timestamp) {
        MetricsConfig config = new CustomConfig();
        config.setNamespace(namespace);
        config.setTags(tags);
        config.setAggregationFreq(aggrFreq);
        config.setAggregation(aggr);
        config.setTimestamp(timestamp);

        this.putRaw(msgBuilder.buildMessage(name, value, this.config, config));
    }

    /**
     * Flushes the recorded metrics, sending them to the server and resetting the buffer to its empty state.
     */
    public void flushMetrics() {
        this.flush();
    }

    private void putRaw(String metric) {
        addToBuffer(metric);
        if (isTimeToFlush()) {
            flush();
        }
    }

    private void addToBuffer(String metric) {
        this.buffer[bufferPos] = metric;
        bufferPos++;
    }

    private boolean isTimeToFlush() {
        return bufferPos == this.config.getFlushSize();
    }

    private void flush() {
        logger.info("Flush!!!");

        if (!this.config.isDryrun()) {
            String message = this.concatMessages();

            SenderFactory.getInstance(config.getTransport(), config.getHost(), config.getPort()).send(message);
        }
    }

    private String concatMessages() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<buffer.length; i++) {
            sb.append(buffer[i]).append("\n");
            buffer[i] = null;
        }
        bufferPos = 0;
        return sb.toString();
    }
}
