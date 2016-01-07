package com.mindera.telemetron.client.api;

public enum AggregationFreq {

    FREQ_10(10),
    FREQ_30(30),
    FREQ_60(60),
    FREQ_120(120),
    FREQ_180(180),
    FREQ_300(300);

    private int frequency;

    AggregationFreq(final int frequency) {
        this.frequency = frequency;
    }

    public int getValue() {
        return this.frequency;
    }
}
