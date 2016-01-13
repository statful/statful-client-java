package com.mindera.telemetron.client.api;

/**
 * This is an holder for the {@link com.mindera.telemetron.client.api.Aggregation} enum to aid building a metric.
 */
public final class AggregationBuilder {
    private final Aggregation aggregation;

    /**
     * Default constructor.
     *
     * @param aggregation The aggregation to hold
     */
    AggregationBuilder(final Aggregation aggregation) {
        this.aggregation = aggregation;
    }

    /**
     * Gets the aggregation.
     *
     * @return The {@link com.mindera.telemetron.client.api.Aggregation}
     */
    Aggregation getAggregation() {
        return aggregation;
    }
}
