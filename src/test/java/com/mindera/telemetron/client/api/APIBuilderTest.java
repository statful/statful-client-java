package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.DefaultClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class APIBuilderTest {

    @Mock
    private MetricsSender metricsSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotSendWhenNameIsInvalid() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.value("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldNotSendWhenValueIsInvalid() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.metricName("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldIgnoreMetricNameWhenEmpty() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.metricName("");

        assertNull(builder.getMetricName());
    }

    @Test
    public void shouldIgnoreValueWhenEmpty() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.value("");

        assertNull(builder.getValue());
    }

    @Test
    public void shouldIgnoreConfigurationWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.configuration(null);
        // Doesn't throw anything
    }

    @Test
    public void shouldIgnoreSampleRateWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);

        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setNamespace("namespace");

        builder.configuration(config);

        assertEquals("Should have default sample rate", Integer.valueOf(100), builder.getSampleRate());
    }

    @Test
    public void shouldIgnoreNamespaceWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);
        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setSampleRate(20);

        builder.configuration(config);

        assertEquals("Should have default namespace", "application", builder.getNamespace());
    }

    @Test
    public void shouldIgnoreAggregationFreqWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.aggFreq(null);

        assertNull(builder.getAggregationFreq());
    }

    @Test
    public void shouldIgnoreAggregationsArrayWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.aggregations((Aggregation[]) null);
        // Doesn't throw anything
    }

    @Test
    public void shouldIgnoreAggregationsWhenNull() {
        APIBuilder builder = new APIBuilder(metricsSender);
        builder.aggregations((Aggregations)null);
        // Doesn't throw anything
    }

    @Test
    public void shouldNotThrowWhenSendingHasException() {
        APIBuilder builder = new APIBuilder(null);
        builder.metricName("namespace").value("value");

        builder.send();
        // Doesn't throw anything
    }
}