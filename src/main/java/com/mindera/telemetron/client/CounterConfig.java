package com.mindera.telemetron.client;

/**
 * Counter configuration class.</br>
 * Defines the specific settings for registering Counter metrics.
 */
public class CounterConfig extends MetricsConfig {

    @Override
    public String getMetricType() {
        return TelemetronConfig.METRIC_COUNTER;
    }

    @Override
    protected void setDefaultValues() {
        this.setAggregation(new String[]{ AGGREGATION_AVG, AGGREGATION_P90, AGGREGATION_COUNT_PS });
    }

    @Override
    protected MetricsConfig createNewInstance() {
        return new CounterConfig();
    }
}
