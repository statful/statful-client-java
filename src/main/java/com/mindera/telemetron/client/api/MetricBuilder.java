package com.mindera.telemetron.client.api;

public final class MetricBuilder {

    private MetricBuilder() {}

    public static AggregationBuilder agg(Aggregation aggregation) {
        return new AggregationBuilder(aggregation);
    }

    public static AggregationFreqBuilder aggrFreq(Integer aggregationFreq) {
        return new AggregationFreqBuilder(aggregationFreq);
    }

    public static TagBuilder tag(String type, String value) {
        return new TagBuilder(type, value);
    }
}
