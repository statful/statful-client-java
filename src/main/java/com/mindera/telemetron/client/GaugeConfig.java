package com.mindera.telemetron.client;

/**
 * Gauge configuration class.</br>
 * Defines the specific settings for registering Gauge metrics.
 */
public class GaugeConfig extends MetricsConfig {

    @Override
    public String getMetricType() {
        return TelemetronConfig.METRIC_GAUGE;
    }

    @Override
    protected void setDefaultValues() {
        this.setAggregation(new String[]{ AGGREGATION_LAST });
    }

    @Override
    protected MetricsConfig createNewInstance() {
        return new GaugeConfig();
    }
}
