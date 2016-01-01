package com.mindera.telemetron.client.api;

public class MetricBuilder {

    public static AggregationBuilder agg(Aggregation aggregation) {
        return new AggregationBuilder(aggregation);
    }

    public static AggregationFreqBuilder aggrFreq(Integer aggregationFreq) {
        return new AggregationFreqBuilder(aggregationFreq);
    }

    public static NamespaceBuilder namespace(String namespace) {
        return new NamespaceBuilder(namespace);
    }

    public static TagBuilder tag(String type, String value) {
        return new TagBuilder(type, value);
    }
}
