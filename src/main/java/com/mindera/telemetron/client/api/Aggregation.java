package com.mindera.telemetron.client.api;

public enum Aggregation {
    AVG("avg"),
    P90("p90"),
    COUNT("count"),
    LAST("last"),
    SUM("sum"),
    FIRST("first"),
    P95("p95"),
    MIN("min"),
    MAX("max");

    private String name;

    Aggregation(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
