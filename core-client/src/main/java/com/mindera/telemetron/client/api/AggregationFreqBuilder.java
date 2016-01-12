package com.mindera.telemetron.client.api;

public final class AggregationFreqBuilder {

    private final AggregationFreq aggFreq;

    AggregationFreqBuilder(final AggregationFreq aggFreq) {
        this.aggFreq = aggFreq;
    }

    AggregationFreq getAggFreq() {
        return aggFreq;
    }
}
