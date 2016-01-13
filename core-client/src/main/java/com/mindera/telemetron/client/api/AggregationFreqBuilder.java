package com.mindera.telemetron.client.api;

/**
 * This is an holder for the {@link com.mindera.telemetron.client.api.AggregationFreq} enum to aid building a metric.
 */
public final class AggregationFreqBuilder {

    private final AggregationFreq aggFreq;

    /**
     * Default constructor.
     *
     * @param aggFreq The aggregation frequency to hold.
     */
    AggregationFreqBuilder(final AggregationFreq aggFreq) {
        this.aggFreq = aggFreq;
    }

    /**
     * Gets the aggregation.
     *
     * @return The {@link com.mindera.telemetron.client.api.AggregationFreq}
     */
    AggregationFreq getAggFreq() {
        return aggFreq;
    }
}
