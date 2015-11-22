package com.mindera.telemetron.client;

import java.util.logging.Logger;

/**
 * Created by hugocosta on 14/11/15.
 */
public class TelemetronClient {

    private TelemetronConfig config;
    private MetricsConfig counterConfig;
    private MetricsConfig timerConfig;
    private MetricsConfig gaugeConfig;

    private Logger logger;

    private String[] buffer;
    private int bufferPos = 0;

    public TelemetronClient() {
        this(new TelemetronConfig());
    }

    public TelemetronClient(TelemetronConfig config) {
        config.isValid();

        this.counterConfig = new MetricsConfig();
        this.timerConfig = new MetricsConfig();
        this.gaugeConfig = new MetricsConfig();
        if (config.getMetricsDefaults() != null) {
            this.counterConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_COUNTER));
            this.timerConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_TIMER));
            this.gaugeConfig.merge(config.getMetricsDefaults().get(TelemetronConfig.METRIC_GAUGE));
        }

        this.config = config;

        this.buffer = new String[this.config.getFlushSize()];
        this.logger = config.getLogger();
    }

    public void addTimer(String name, long time, MetricsConfig config) {
        MetricsConfig finalConfig = timerConfig.mergeToNewInstance(config);
        this.put(name, time, finalConfig);
    }

    public void addGauge(String name, long value, MetricsConfig config) {
        MetricsConfig finalConfig = gaugeConfig.mergeToNewInstance(config);
        this.put(name, value, finalConfig);
    }

    public void addCounter(String name, long value, MetricsConfig config) {
        MetricsConfig finalConfig = counterConfig.mergeToNewInstance(config);
        this.put(name, value, finalConfig);
    }

    public void put(String name, long value, MetricsConfig config) {
        this.putRaw(this.buildMessage(name, value, config));
    }

    private String buildMessage(String name, long value, MetricsConfig config) {
        StringBuilder sb = new StringBuilder();
        sb.append(this.config.getPrefix()).append(".").append(config.getNamespace()).append(".").append(name);

        //TODO build the metric with all the fields.

        return sb.toString();
    }

    public void flushMetrics() {
        this.flush();
    }

    private void putRaw(String metric) {
        this.buffer[bufferPos] = metric;
        bufferPos++;
        if (bufferPos == this.config.getFlushSize()) {
            flush();
        }
    }

    private void flush() {
        StringBuilder sb = new StringBuilder();
        for (int i=0; i<buffer.length; i++) {
            sb.append(buffer[i]).append("\n");
            buffer[i] = null;
        }
        bufferPos = 0;

        //TODO remove this or use logger
        System.out.println("Flush!!!");

        //TODO improve to promote instance reuse
        SenderFactory.getInstance(config.getTransport(), config.getHost(), config.getPort()).send(sb.toString());
    }
}
