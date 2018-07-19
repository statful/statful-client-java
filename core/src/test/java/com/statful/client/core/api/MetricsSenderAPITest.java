package com.statful.client.core.api;

import com.statful.client.core.config.DefaultClientConfiguration;
import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.Tags;
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
    private com.statful.client.domain.api.MetricsSender metricsSender;

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
                any(AggregationFrequency.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldNotSendWhenValueIsInvalid() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.name("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFrequency.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldNotSendWhenAggregatedMetricIsInvalid() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender, true, false);
        builder.name("something").value("something").aggregations(Aggregations.from(Aggregation.AVG, Aggregation.COUNT));

        builder.send();

        verify(metricsSender, times(0)).aggregatedPut(anyString(), anyString(), any(Tags.class), any(Aggregation.class),
                any(AggregationFrequency.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldIgnoreMetricNameWhenEmpty() {
        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.name("");

        assertNull(builder.getName());
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
        builder.aggregationFrequency(null);

        assertNull(builder.getAggregationFrequency());
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
        builder.name("namespace").value("value");

        builder.send();
        // Doesn't throw anything
    }

    @Test
    public void shouldSendIfValid() {
        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setNamespace("namespace");
        config.setSampleRate(50);

        MetricsSenderAPI builder = new MetricsSenderAPI(metricsSender);
        builder.configuration(config);

        builder.name("test")
                .aggregations(Aggregation.AVG)
                .aggregationFrequency(AggregationFrequency.FREQ_10)
                .sampleRate(75)
                .tag("tagKey", "tagValue")
                .value(String.valueOf(1000));

        builder.send();

        verify(metricsSender, times(1)).put(eq("test"),
                eq(String.valueOf(1000)),
                any(Tags.class),
                any(Aggregations.class),
                eq(AggregationFrequency.FREQ_10),
                eq(75),
                eq("namespace"),
                anyLong());
    }
}