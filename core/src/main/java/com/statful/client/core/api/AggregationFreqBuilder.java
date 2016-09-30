package com.statful.client.core.api;

import com.statful.client.domain.api.AggregationFreq;

/**
 * This is an holder for the {@link com.statful.client.domain.api.AggregationFreq} enum to aid building a metric.
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

    /**
     * Instantiates a new {@link AggregationFreqBuilder}.
     * <p>
     * Example: <code>timer(aggFreq(FREQ_120)).</code>
     *
     * @param aggregationFreq The {@link com.statful.client.domain.api.AggregationFreq} to use
     * @return An instance of {@link AggregationFreqBuilder}
     */
    public static AggregationFreqBuilder aggFreq(final AggregationFreq aggregationFreq) {
        return new AggregationFreqBuilder(aggregationFreq);
    }
}
