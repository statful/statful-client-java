package com.mindera.telemetron.client.api;

final class GaugeConfigBuilder extends MetricConfigBuilder {

    GaugeConfigBuilder(TagBuilder... tagsBuilders) {
        super(tagsBuilders);
    }

    GaugeConfigBuilder(AggregationBuilder... aggsBuilders) {
        super(aggsBuilders);
    }

    GaugeConfigBuilder(Aggregation... aggregations) {
        super(aggregations);
    }

    GaugeConfigBuilder(AggregationFreqBuilder aggrFreqBuilder) {
        super(aggrFreqBuilder);
    }
}
