package com.mindera.telemetron.client.api;

public final class AggregationFreqBuilder {
    private final Integer aggrFreq;

    AggregationFreqBuilder(Integer aggrFreq) {
        this.aggrFreq = aggrFreq;
    }

    Integer getAggrFreq() {
        return aggrFreq;
    }
}
