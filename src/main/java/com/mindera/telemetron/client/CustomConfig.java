package com.mindera.telemetron.client;

/**
 * Defines settings for registering custom metrics.
 */
public class CustomConfig extends MetricsConfig {

    @Override
    public String getMetricType() {
        return null;
    }

    @Override
    protected void setDefaultValues() {
        this.setAggregation(new String[]{ });
    }

    @Override
    protected MetricsConfig createNewInstance() {
        return new CustomConfig();
    }
}
