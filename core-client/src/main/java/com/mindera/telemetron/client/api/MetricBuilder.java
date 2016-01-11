package com.mindera.telemetron.client.api;

/**
 * This class serves only for syntax sugar for building metrics. The static methods can be statically imported for
 * more compact code.
 */
public final class MetricBuilder {

    /**
     * Default constructor.
     */
    private MetricBuilder() { }

    /**
     * Instantiates a new {@link com.mindera.telemetron.client.api.AggregationBuilder}.
     * <p>
     * Example: <code>timer(agg(AVG)).</code>
     *
     * @param aggregation The {@link com.mindera.telemetron.client.api.Aggregation} to use
     * @return An instance of {@link com.mindera.telemetron.client.api.AggregationBuilder}
     */
    public static AggregationBuilder agg(final Aggregation aggregation) {
        return new AggregationBuilder(aggregation);
    }

    /**
     * Instantiates a new {@link com.mindera.telemetron.client.api.AggregationFreqBuilder}.
     * <p>
     * Example: <code>timer(aggFreq(FREQ_120)).</code>
     *
     * @param aggregationFreq The {@link com.mindera.telemetron.client.api.AggregationFreq} to use
     * @return An instance of {@link com.mindera.telemetron.client.api.AggregationFreqBuilder}
     */
    public static AggregationFreqBuilder aggFreq(final AggregationFreq aggregationFreq) {
        return new AggregationFreqBuilder(aggregationFreq);
    }

    /**
     * Instantiates a new {@link com.mindera.telemetron.client.api.TagBuilder}.
     * <p>
     * Example: <code>timer(tag("cluster", "production"))</code>
     *
     * @param type The tag type to use
     * @param value The tag value to use
     * @return An instance of {@link com.mindera.telemetron.client.api.TagBuilder}
     */
    public static TagBuilder tag(final String type, final String value) {
        return new TagBuilder(type, value);
    }
}
