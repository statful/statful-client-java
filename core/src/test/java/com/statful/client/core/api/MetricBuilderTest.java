package com.statful.client.core.api;

import org.junit.Test;

import static com.statful.client.core.api.MetricBuilder.*;
import static com.statful.client.domain.api.Aggregation.P90;
import static com.statful.client.domain.api.AggregationFreq.FREQ_10;
import static org.junit.Assert.assertEquals;

public class MetricBuilderTest {

    @Test
    public void shouldBuildWithAggregation() throws Exception {
        assertEquals("Should build tags aggregation", P90, agg(P90).getAggregation());
    }

    @Test
    public void shouldBuildWithAggregationFrequency() throws Exception {
        assertEquals("Should build tags aggregation frequency", FREQ_10, aggFreq(FREQ_10).getAggFreq());
    }

    @Test
    public void shouldBuildWithTag() throws Exception {
        TagBuilder tagBuilder = tag("host", "localhost");

        assertEquals("Should build tags tag", "host", tagBuilder.getType());
        assertEquals("Should build tags tag", "localhost", tagBuilder.getValue());
    }
}