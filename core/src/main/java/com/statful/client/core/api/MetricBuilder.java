package com.statful.client.core.api;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;

/**
 * This class serves only for syntax sugar for building metrics. The static methods can be statically imported for
 * more compact code.
 */
@Deprecated
public final class MetricBuilder {

    /**
     * Default constructor.
     */
    private MetricBuilder() { }

    /**
     * Instantiates a new {@link AggregationBuilder}.
     * <p>
     * Example: <code>timer(agg(AVG)).</code>
     *
     * @param aggregation The {@link com.statful.client.domain.api.Aggregation} to use
     * @return An instance of {@link AggregationBuilder}
     */
    public static AggregationBuilder agg(final Aggregation aggregation) {
        return new AggregationBuilder(aggregation);
    }

    /**
     * Instantiates a new {@link AggregationFreqBuilder}.
     * <p>
     * Example: <code>timer(aggregationFrequency(FREQ_120)).</code>
     *
     * @param aggregationFrequency The {@link AggregationFrequency} to use
     * @return An instance of {@link AggregationFreqBuilder}
     */
    public static AggregationFreqBuilder aggFreq(final AggregationFrequency aggregationFrequency) {
        return new AggregationFreqBuilder(aggregationFrequency);
    }

    /**
     * Instantiates a new {@link TagBuilder}.
     * <p>
     * Example: <code>timer(tag("cluster", "production"))</code>
     *
     * @param type The tag type to use
     * @param value The tag value to use
     * @return An instance of {@link TagBuilder}
     */
    public static TagBuilder tag(final String type, final String value) {
        return new TagBuilder(type, value);
    }
}
