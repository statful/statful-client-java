package com.mindera.telemetron.client;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by hugocosta on 15/11/15.
 */
public class MetricsConfig {

    public static final String AGGREGATION_AVG = "avg";
    public static final String AGGREGATION_P90 = "p90";
    public static final String AGGREGATION_COUNT = "count";
    public static final String AGGREGATION_COUNT_PS = "count_ps";

    private HashMap<String, String> tags;
    private String[] aggregation;
    private int[] aggregationFreq;
    private String namespace;
    private long timestamp;

    public MetricsConfig() {
        HashMap<String, String> defaultTags = new HashMap<String, String>();
        defaultTags.put("unit", "ms");
        this.setTags(defaultTags);
        this.setAggregation(new String[]{ AGGREGATION_AVG, AGGREGATION_P90, AGGREGATION_COUNT, AGGREGATION_COUNT_PS });
        this.setAggregationFreq(new int[]{10});
        this.setNamespace("application");
        this.setTimestamp(new Date().getTime());
    }

    public MetricsConfig merge(MetricsConfig config) {
        if (config != null) {
            if (config.getAggregation() != null) {
                this.setAggregation(config.getAggregation());
            }
            if (config.getAggregationFreq() != null) {
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

    public MetricsConfig mergeToNewInstance(MetricsConfig config) {
        MetricsConfig clone = new MetricsConfig();
        clone.merge(this);
        clone.merge(config);

        return clone;
    }

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

    public int[] getAggregationFreq() {
        return aggregationFreq;
    }

    public void setAggregationFreq(int[] aggregationFreq) {
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
