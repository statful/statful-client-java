package com.mindera.telemetron.client.config;

import com.mindera.telemetron.client.api.Aggregation;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.api.Transport;

public class DefaultClientConfiguration implements ClientConfiguration {

    private String host = DEFAULT_HOST;
    private int port = DEFAULT_PORT;
    private int sampleRate = DEFAULT_SAMPLE_RATE;
    private String namespace = DEFAULT_NAMESPACE;
    private int flushSize = DEFAULT_FLUSH_SIZE;

    private String prefix;
    private boolean isDryRun;
    private long flushIntervalMillis;
    private Transport transport;
    private String token;
    private String app;

    private Tags applicationTags;
    private Tags timerTags = Tags.from(DEFAULT_TIMER_TAGS);
    private Tags counterTags;
    private Tags gaugeTags;

    private Aggregations timerAggregations = Aggregations.from(DEFAULT_TIMER_AGREGATIONS);
    private Aggregations counterAggregations = Aggregations.from(DEFAULT_COUNTER_AGGREGATIONS);
    private Aggregations gaugeAggregations = Aggregations.from(DEFAULT_GAUGE_AGGREGATIONS);

    private int timerAggregationFreq = DEFAULT_AGGREGATION_FREQ;
    private int counterAggregationFreq = DEFAULT_AGGREGATION_FREQ;
    private int gaugeAggregationFreq = DEFAULT_AGGREGATION_FREQ;

    @Override
    public boolean isValid() {
        return prefix != null && transport != null;
    }

    @Override
    public Tags getApplicationTags() {
        return applicationTags;
    }

    @Override
    public int getSampleRate() {
        return sampleRate;
    }

    @Override
    public String getNamespace() {
        return namespace;
    }

    @Override
    public String getPrefix() {
        return prefix;
    }

    @Override
    public int getFlushSize() {
        return flushSize;
    }

    @Override
    public long getFlushIntervalMillis() {
        return flushIntervalMillis;
    }

    @Override
    public boolean isDryRun() {
        return isDryRun;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public Transport getTransport() {
        return transport;
    }

    @Override
    public String getToken() {
        return token;
    }

    @Override
    public String getApp() {
        return app;
    }

    @Override
    public Tags getTimerTags() {
        return timerTags;
    }

    @Override
    public Aggregations getTimerAggregations() {
        return timerAggregations;
    }

    @Override
    public int getTimerAggregationFreq() {
        return timerAggregationFreq;
    }

    @Override
    public Tags getCounterTags() {
        return counterTags;
    }

    @Override
    public Aggregations getCounterAggregations() {
        return counterAggregations;
    }

    @Override
    public int getCounterAggregationFreq() {
        return counterAggregationFreq;
    }

    @Override
    public Tags getGaugeTags() {
        return gaugeTags;
    }

    @Override
    public Aggregations getGaugeAggregations() {
        return gaugeAggregations;
    }

    @Override
    public int getGaugeAggregationFreq() {
        return gaugeAggregationFreq;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public void setFlushSize(int flushSize) {
        this.flushSize = flushSize;
    }

    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    public void setDryRun(boolean isDryRun) {
        this.isDryRun = isDryRun;
    }

    public void setFlushIntervalMillis(long flushIntervalMillis) {
        this.flushIntervalMillis = flushIntervalMillis;
    }

    public void setTransport(Transport transport) {
        this.transport = transport;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setApp(String app) {
        this.app = app;
        mergeTagIntoMethods("app", app);
    }

    public void setTimerAggregationFreq(int timerAggregationFreq) {
        this.timerAggregationFreq = timerAggregationFreq;
    }

    public void mergeTimerTag(String type, String value) {
        this.timerTags.putTag(type, value);
    }

    public void mergeTimerAggregation(Aggregation aggregation) {
        this.timerAggregations.put(aggregation);
    }

    public void setCounterAggregationFreq(int counterAggregationFreq) {
        this.counterAggregationFreq = counterAggregationFreq;
    }

    public void mergeCounterTag(String type, String value) {
        getSafeCounterTags().putTag(type, value);
    }

    public void mergeCounterAggregation(Aggregation aggregation) {
        this.counterAggregations.put(aggregation);
    }

    public void setGaugeAggregationFreq(int gaugeAggregationFreq) {
        this.gaugeAggregationFreq = gaugeAggregationFreq;
    }

    public void mergeGaugeTag(String type, String value) {
        getSafeGaugeTags().putTag(type, value);
    }

    public void mergeGaugeAggregation(Aggregation aggregation) {
        this.gaugeAggregations.put(aggregation);
    }

    public void mergeApplicationTag(String type, String value) {
        getSafeApplicationTags().putTag(type, value);
        mergeTagIntoMethods(type, value);
    }

    private void mergeTagIntoMethods(String type, String value) {
        timerTags.putTag(type, value);
        getSafeCounterTags().putTag(type, value);
        getSafeGaugeTags().putTag(type, value);
    }

    private Tags getSafeApplicationTags() {
        if (applicationTags == null) {
            applicationTags = new Tags();
        }
        return applicationTags;
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
