package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.DefaultClientConfiguration;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class MetricsSenderAPITest {

    @Mock
    private MetricsSender metricsSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldNotSendWhenNameIsInvalid() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.value("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldNotSendWhenValueIsInvalid() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.metricName("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldIgnoreMetricNameWhenEmpty() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.metricName("");

        assertNull(builder.getMetricName());
    }

    @Test
    public void shouldIgnoreValueWhenEmpty() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.value("");

        assertNull(builder.getValue());
    }

    @Test
    public void shouldIgnoreConfigurationWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.configuration(null);
        // Doesn't throw anything
    }

    @Test
    public void shouldIgnoreSampleRateWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);

        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setNamespace("namespace");

        builder.configuration(config);

        assertEquals("Should have default sample rate", Integer.valueOf(100), builder.getSampleRate());
    }

    @Test
    public void shouldIgnoreNamespaceWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setSampleRate(20);

        builder.configuration(config);

        assertEquals("Should have default namespace", "application", builder.getNamespace());
    }

    @Test
    public void shouldIgnoreAggregationFreqWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.aggFreq(null);

        assertNull(builder.getAggregationFreq());
    }

    @Test
    public void shouldIgnoreAggregationsArrayWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.aggregations((Aggregation[]) null);
        // Doesn't throw anything
    }

    @Test
    public void shouldIgnoreAggregationsWhenNull() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.aggregations((Aggregations)null);
        // Doesn't throw anything
    }

    @Test
    public void shouldNotThrowWhenSendingHasException() {
        MetricsSenderAPI builder = new MetricsSenderAPI(null);
        builder.metricName("namespace").value("value");

        builder.send();
        // Doesn't throw anything
    }
}