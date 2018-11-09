package com.statful.client.domain.api;

/**
 * Supported aggregations.
 */
public enum Aggregation {
    AVG("avg"),
    P90("p90"),
    COUNT("count"),
    LAST("last"),
    SUM("sum"),
    FIRST("first"),
    P95("p95"),
    P99("p99"),
    MIN("min"),
    MAX("max");

    private String name;

    Aggregation(final String name) {
        this.name = name;
    }

    /**
     * Returns the name of the aggregation.
     *
     * @return The name of the aggregation
     */
    public String getName() {
        return name;
    }
}
