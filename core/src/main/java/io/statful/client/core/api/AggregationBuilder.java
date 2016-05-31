package io.statful.client.core.api;

import io.statful.client.domain.api.Aggregation;

/**
 * This is an holder for the {@link io.statful.client.domain.api.Aggregation} enum to aid building a metric.
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
     * @return The {@link Aggregation}
     */
    Aggregation getAggregation() {
        return aggregation;
    }
}
