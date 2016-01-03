package com.mindera.telemetron.client.api;

import org.junit.Test;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.AggregationFreq.*;
import static com.mindera.telemetron.client.api.MetricBuilder.*;
import static org.junit.Assert.assertEquals;

public class MetricBuilderTest {

    @Test
    public void shouldBuildWithAggregation() throws Exception {
        assertEquals("Should build with aggregation", P90, agg(P90).getAggregation());
    }

    @Test
    public void shouldBuildWithAggregationFrequency() throws Exception {
        assertEquals("Should build with aggregation frequency", FREQ_10, aggFreq(FREQ_10).getAggFreq());
    }

    @Test
    public void shouldBuildWithTag() throws Exception {
        TagBuilder tagBuilder = tag("host", "localhost");

        assertEquals("Should build with tag", "host", tagBuilder.getType());
        assertEquals("Should build with tag", "localhost", tagBuilder.getValue());
    }
}