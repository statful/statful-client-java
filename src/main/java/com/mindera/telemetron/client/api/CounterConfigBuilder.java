package com.mindera.telemetron.client.api;

final class CounterConfigBuilder extends MetricConfigBuilder {

    CounterConfigBuilder(TagBuilder... tagsBuilders) {
        super(tagsBuilders);
    }

    CounterConfigBuilder(AggregationBuilder... aggsBuilders) {
        super(aggsBuilders);
    }

    CounterConfigBuilder(Aggregation... aggregations) {
        super(aggregations);
    }

    CounterConfigBuilder(AggregationFreqBuilder aggrFreqBuilder) {
        super(aggrFreqBuilder);
    }
}
