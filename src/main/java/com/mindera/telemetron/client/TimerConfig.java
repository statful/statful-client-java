package com.mindera.telemetron.client;

/**
 * Timer configuration class.</br>
 * Defines the specific settings for registering Timer metrics.
 */
public class TimerConfig extends MetricsConfig {

    @Override
    public String getMetricType() {
        return TelemetronConfig.METRIC_TIMER;
    }

    @Override
    protected void setDefaultValues() {
        this.getTags().put("unit", "ms");
        this.setAggregation(new String[]{ AGGREGATION_AVG, AGGREGATION_P90, AGGREGATION_COUNT, AGGREGATION_COUNT_PS });
    }

    @Override
    protected MetricsConfig createNewInstance() {
        return new TimerConfig();
    }
}
