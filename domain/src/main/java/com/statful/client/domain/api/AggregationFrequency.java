package com.statful.client.domain.api;

/**
 * Supported aggregation frequencies.
 */
public enum AggregationFrequency {

    FREQ_10(10),
    FREQ_30(30),
    FREQ_60(60),
    FREQ_120(120),
    FREQ_180(180),
    FREQ_300(300);

    private int frequency;

    AggregationFrequency(final int frequency) {
        this.frequency = frequency;
    }

    /**
     * Returns the value of the aggregation frequency.
     *
     * @return The value of the aggregation frequency as an integer
     */
    public int getValue() {
        return this.frequency;
    }
}
