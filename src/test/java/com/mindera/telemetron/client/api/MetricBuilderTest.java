package com.mindera.telemetron.client.api;

import org.junit.Test;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.AggregationFreq.*;
import static com.mindera.telemetron.client.api.MetricBuilder.*;
import static org.junit.Assert.assertEquals;

public class MetricBuilderTest {

    @Test
    public void shouldBuildWithAggregation() throws Exception {
        assertEquals("Should build withTags aggregation", P90, agg(P90).getAggregation());
    }

    @Test
    public void shouldBuildWithAggregationFrequency() throws Exception {
        assertEquals("Should build withTags aggregation frequency", FREQ_10, aggFreq(FREQ_10).getAggFreq());
    }

    @Test
    public void shouldBuildWithTag() throws Exception {
        TagBuilder tagBuilder = tag("host", "localhost");

        assertEquals("Should build withTags tag", "host", tagBuilder.getType());
        assertEquals("Should build withTags tag", "localhost", tagBuilder.getValue());
    }
}