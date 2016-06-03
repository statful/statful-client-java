package io.statful.client.core.api;

import io.statful.client.domain.api.AggregationFreq;

/**
 * This is an holder for the {@link io.statful.client.domain.api.AggregationFreq} enum to aid building a metric.
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
     * @return The {@link AggregationFreq}
     */
    AggregationFreq getAggFreq() {
        return aggFreq;
    }
}
