package com.mindera.telemetron.client.api;

public enum Aggregation {
    AVG("avg"),
    P90("p90"),
    COUNT("count"),
    COUNT_PS("count_ps"),
    SUM("sum"),
    LAST("last");

    private String name;

    Aggregation(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
