package com.mindera.telemetron.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public abstract class MetricsConfig {

    public static final String AGGREGATION_AVG = "avg";
    public static final String AGGREGATION_P90 = "p90";
    public static final String AGGREGATION_COUNT = "count";
    public static final String AGGREGATION_COUNT_PS = "count_ps";
    public static final String AGGREGATION_LAST = "last";

    private HashMap<String, String> tags = new HashMap<String, String>();
    private String[] aggregation;
    private int aggregationFreq = 10;
    private String namespace = "application";
    private long timestamp = new Date().getTime();

    {
        setDefaultValues();
    }

    protected MetricsConfig() {}

    protected abstract void setDefaultValues();
    protected abstract MetricsConfig createNewInstance();

    /**
     * Internal method to merge configurations of a given {@link MetricsConfig}
     * with the existing configurations in this instance
     * @param config values to merge
     * @return this instance with values updated according to received configs
     */
    MetricsConfig merge(MetricsConfig config) {
        //TODO review this method
        if (config != null) {
            if (config.getAggregation() != null) {
                this.setAggregation(config.getAggregation());
            }
            if (config.getAggregationFreq() != 0) {
                this.setAggregationFreq(config.getAggregationFreq());
            }
            if (config.getNamespace() != null) {
                this.setNamespace(config.getNamespace());
            }
            if (config.getTimestamp() != 0) {
                this.setTimestamp(config.getTimestamp());
            }
            if(config.getTags() != null) {
                this.tags.putAll(config.getTags());
            }
        }

        return this;
    }

    /**
     * Internal method to merge configurations of a given {@link MetricsConfig} with this instance values and
     * put them in a new instance.</br>
     * <br/>
     * <b>Use case: </b>
     * When it is required to preserved the existing configurations (ex. Client default configs for a metric)
     * @param config values to merge
     * @return new instance with merged values
     */
    protected MetricsConfig mergeToNewInstance(MetricsConfig config) {
        MetricsConfig newInstance = this.createNewInstance();
        newInstance.merge(this);
        newInstance.merge(config);
        return newInstance;
    }

    /**
     * Obtains the type of metric. It is a static value, that depends on the type of metric that was created.
     * @return metric type
     */
    public abstract String getMetricType();

    public HashMap<String, String> getTags() {
        return tags;
    }

    public void setTags(HashMap<String, String> tags) {
        this.tags = tags;
    }

    public String[] getAggregation() {
        return aggregation;
    }

    public void setAggregation(String[] aggregation) {
        this.aggregation = aggregation;
    }

    public int getAggregationFreq() {
        return aggregationFreq;
    }

    public void setAggregationFreq(int aggregationFreq) {
        this.aggregationFreq = aggregationFreq;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
