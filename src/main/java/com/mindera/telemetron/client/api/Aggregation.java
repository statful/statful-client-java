package com.mindera.telemetron.client.api;

public enum Aggregation {
    AVG("avg"),
    P90("p90"),
    COUNT("count"),
    COUNT_PS("count_ps"),
    LAST("last"),
    SUM("sum"),
    FIRST("first"),
    P95("p95"),
    MIN("min"),
    MAX("max"),
    DERIVATIVE("derivative");

    private String name;

    Aggregation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
