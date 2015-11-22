package com.mindera.telemetron.client;

import java.util.HashMap;
import java.util.logging.Logger;

/**
 * Created by hugocosta on 15/11/15.
 */
public class TelemetronConfig {

    public static final String HOST_CONFIG = "host";
    public static final String PORT_CONFIG = "port";
    public static final String PREFIX_CONFIG = "prefix";
    public static final String TRANSPORT_CONFIG = "transport";
    public static final String SECURE_CONFIG = "secure";
    public static final String TIMEOUT_CONFIG = "timeout";
    public static final String TOKEN_CONFIG = "token";
    public static final String APP_CONFIG = "app";
    public static final String DRYRUN_CONFIG = "dryrun";
    public static final String LOGGER_CONFIG = "logger";
    public static final String TAGS_CONFIG = "tags";
    public static final String SAMPLE_RATE_CONFIG = "sampleRate";
    public static final String FLUSH_SIZE_CONFIG = "flushSize";
    public static final String METRICS_DEFAULTS_CONFIG = "metricsDefault";
    public static final String SYSTEM_METRICS_CONFIG = "systemMetrics";

    public static final int METRIC_COUNTER = 0;
    public static final int METRIC_TIMER = 1;
    public static final int METRIC_GAUGE = 2;

    public static final String TRANSPORT_TCP = "TCP";
    public static final String TRANSPORT_UDP = "UDP";
    public static final String TRANSPORT_HTTP = "HTTP";
    public static final String[] TRANSPORT_OPTIONS = {"TCP", "UDP", "HTTP"};

    private String host;
    private String port;
    private String prefix;
    private String transport;
    private boolean secure;
    private int timeout;
    private String token;
    private String app;
    private boolean dryrun;
    private Logger logger;
    private HashMap<String, String> tags = new HashMap<String, String>();
    private int sampleRate;
    private int flushSize;
    private HashMap<String, MetricsConfig> metricsDefaults;
    private boolean systemMetrics;

    /**
     * Default constructor.
     * Initializes the configuration parameters with the respective default values.
     */
    public TelemetronConfig() {
        this.setHost("127.0.0.1");
        this.setPort("2013");
        this.setTransport(TRANSPORT_HTTP);
        this.setSecure(true);
        this.setTimeout(2000);
        this.setDryrun(false);
        HashMap<String, String> defaultTags = new HashMap<String, String>();
        defaultTags.put("telemetron_client", "java");
        this.setTags(defaultTags);
        this.setSampleRate(100);
        this.setFlushSize(10);
        this.setSystemMetrics(false);
    }

    //TODO implement validation logic for the telemetron client configurations values
    public boolean isValid() {
        return true;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public boolean isSecure() {
        return secure;
    }

    public void setSecure(boolean secure) {
        this.secure = secure;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public boolean isDryrun() {
        return dryrun;
    }

    public void setDryrun(boolean dryrun) {
        this.dryrun = dryrun;
    }

    public Logger getLogger() {
        return logger;
    }

    public void setLogger(Logger logger) {
        this.logger = logger;
    }

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public int getSampleRate() {
        return sampleRate;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getFlushSize() {
        return flushSize;
    }

    public void setFlushSize(int flushSize) {
        this.flushSize = flushSize;
    }

    public HashMap<String, MetricsConfig> getMetricsDefaults() {
        return metricsDefaults;
    }

    public void setMetricsDefaults(HashMap<String, MetricsConfig> metricsDefaults) {
        this.metricsDefaults = metricsDefaults;
    }

    public boolean isSystemMetrics() {
        return systemMetrics;
    }

    public void setSystemMetrics(boolean systemMetrics) {
        this.systemMetrics = systemMetrics;
    }

}
