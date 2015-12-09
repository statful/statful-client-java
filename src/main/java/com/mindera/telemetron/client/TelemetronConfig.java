package com.mindera.telemetron.client;

import java.util.HashMap;
import java.util.logging.Logger;

public class TelemetronConfig {

    public enum TRANSPORT_TYPE {
        TCP,
        UDP,
        HTTP
    }

    public enum CONFIGS {
        HOST("host"),
        PORT("port"),
        PREFIX("prefix"),
        TRANSPORT("transport"),
        SECURE("secure"),
        TIMEOUT("timeout"),
        TOKEN("token"),
        APP("app"),
        DRYRUN("dryrun"),
        LOGGER("logger"),
        TAGS("tags"),
        SAMPLE_RATE("sampleRate"),
        FLUSH_SIZE("flushSize"),
        METRICS_DEFAULTS("metricsDefault"),
        SYSTEM_METRICS("systemMetrics");

        private String propertyName;

        CONFIGS(String propertyName) {
            this.propertyName = propertyName;
        }

        public String getPropertyName() {
            return propertyName;
        }
    }

    public static final String METRIC_COUNTER = "counter";
    public static final String METRIC_TIMER = "timer";
    public static final String METRIC_GAUGE = "gauge";

    private String host = "127.0.0.1";
    private String port = "2013";
    private String prefix;
    private String transport = TRANSPORT_TYPE.HTTP.name();
    private boolean secure = true;
    private int timeout = 2000;
    private String token;
    private String app;
    private boolean dryrun;
    private Logger logger;
    private HashMap<String, String> tags = new HashMap<String, String>();
    private int sampleRate = 100;
    private String namespace = "application";
    private int flushSize = 10;
    private HashMap<String, MetricsConfig> metricsDefaults;
    private boolean systemMetrics;

    {
        tags.put("telemetron_client", "java");
    }

    /**
     * Default constructor.
     * Initializes the configuration parameters with default values.
     */
    public TelemetronConfig() {}

    /**
     * Perform validations for all TelemetronClient configurations.
     * @return true if all configs are valid
     */
    public boolean validate() {

        if (transport == null) {
            throw new TelemetronConfigException("transport is required");
        }

        if (prefix == null) {
            throw new TelemetronConfigException("prefix is required");
        }

        if (sampleRate < 0 || sampleRate > 100) {
            throw new TelemetronConfigException("sample rate ["+sampleRate+"] out of allowed range");
        }

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
        if (app == null) {
            this.tags.remove("app");
        } else {
            this.tags.put("app", app);
        }

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

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
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
