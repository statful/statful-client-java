package com.mindera.telemetron.client.api;

/**
 * This class serves only for syntax sugar for building metrics. The static methods can be statically imported for
 * more compact code.
 */
public final class MetricBuilder {

    private MetricBuilder() { }

    /**
     * Instantiates a new aggregation builder. Example: timer(agg(AVG)).
     *
     * @param aggregation The aggregation to use
     * @return An aggregation builder
     */
    public static AggregationBuilder agg(final Aggregation aggregation) {
        return new AggregationBuilder(aggregation);
    }

    /**
     * Instantiates a new aggregation frequency builder. Example: timer(aggFreq(FREQ_120)).
     *
     * @param aggregationFreq The aggregation frequency to use
     * @return An aggregation frequency builder
     */
    public static AggregationFreqBuilder aggFreq(final AggregationFreq aggregationFreq) {
        return new AggregationFreqBuilder(aggregationFreq);
    }

    /**
     * Instantiates a new tag builder. Example: timer(tag("cluster", "production"))
     *
     * @param type The tag type to use
     * @param value The tag value to use
     * @return A tag builder
     */
    public static TagBuilder tag(final String type, final String value) {
        return new TagBuilder(type, value);
    }
}
