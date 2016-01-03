package com.mindera.telemetron.client.api;

public final class AggregationBuilder {
    private final Aggregation aggregation;

    AggregationBuilder(final Aggregation aggregation) {
        this.aggregation = aggregation;
    }

    Aggregation getAggregation() {
        return aggregation;
    }
}
