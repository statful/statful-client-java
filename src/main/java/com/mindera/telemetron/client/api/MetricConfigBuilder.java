package com.mindera.telemetron.client.api;

class MetricConfigBuilder {
    private Tags tags;
    private Aggregations aggregations;
    private Integer aggregationFreq;

    protected MetricConfigBuilder(TagBuilder... tagsBuilders) {
        tags = new Tags();
        for (TagBuilder tagBuilder : tagsBuilders) {
            tags.putTag(tagBuilder.getType(), tagBuilder.getValue());
        }
    }

    protected MetricConfigBuilder(Aggregation... aggregations) {
        this.aggregations = new Aggregations();
        for (Aggregation aggregation : aggregations) {
            this.aggregations.put(aggregation);
        }
    }

    protected MetricConfigBuilder(AggregationBuilder... aggsBuilders) {
        this.aggregations = new Aggregations();
        for (AggregationBuilder aggregationBuilder : aggsBuilders) {
            this.aggregations.put(aggregationBuilder.getAggregation());
        }
    }

    protected MetricConfigBuilder(AggregationFreqBuilder aggrFreqBuilder) {
        this.aggregationFreq = aggrFreqBuilder.getAggrFreq();
    }

    Tags getTags() {
        return tags;
    }

    Aggregations getAggregations() {
        return aggregations;
    }

    Integer getAggregationFreq() {
        return aggregationFreq;
    }
}
