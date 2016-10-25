package com.statful.client.core.api;

import com.statful.client.domain.api.AggregationFrequency;

/**
 * This is an holder for the {@link AggregationFrequency} enum to aid building a metric.
 */
public final class AggregationFreqBuilder {

    private final AggregationFrequency aggFreq;

    /**
     * Default constructor.
     *
     * @param aggFreq The aggregation frequency to hold.
     */
    AggregationFreqBuilder(final AggregationFrequency aggFreq) {
        this.aggFreq = aggFreq;
    }

    /**
     * Gets the aggregation.
     *
     * @return The {@link AggregationFrequency}
     */
    AggregationFrequency getAggFreq() {
        return aggFreq;
    }

    /**
     * Instantiates a new {@link AggregationFreqBuilder}.
     * <p>
     * Example: <code>timer(aggregationFrequency(FREQ_120)).</code>
     *
     * @param aggregationFrequency The {@link AggregationFrequency} to use
     * @return An instance of {@link AggregationFreqBuilder}
     */
    public static AggregationFreqBuilder aggFreq(final AggregationFrequency aggregationFrequency) {
        return new AggregationFreqBuilder(aggregationFrequency);
    }
}
