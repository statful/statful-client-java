package com.mindera.telemetron.client.api;

final class TimerConfigBuilder extends MetricConfigBuilder{

    TimerConfigBuilder(TagBuilder... tagsBuilders) {
        super(tagsBuilders);
    }

    TimerConfigBuilder(AggregationBuilder... aggsBuilders) {
        super(aggsBuilders);
    }

    TimerConfigBuilder(Aggregation... aggregations) {
        super(aggregations);
    }

    TimerConfigBuilder(AggregationFreqBuilder aggrFreqBuilder) {
        super(aggrFreqBuilder);
    }
}
