package com.statful.client.core.api;

import com.statful.client.core.config.DefaultClientConfiguration;
import com.statful.client.domain.api.*;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class AggregatedMetricsSenderAPITest {

    @Mock
    private MetricsSender metricsSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenSettingSingleAggregation() {
        AggregatedMetricsSenderAPI builder = new AggregatedMetricsSenderAPI(metricsSender);
        builder.aggregations(Aggregation.AVG);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void shouldThrowUnsupportedOperationExceptionWhenSettingAggregations() {
        AggregatedMetricsSenderAPI builder = new AggregatedMetricsSenderAPI(metricsSender);
        builder.aggregations(Aggregations.from(Aggregation.AVG));
    }

    @Test
    public void shouldNotSendWhenNameIsInvalid() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.value("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldNotSendWhenValueIsInvalid() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.metricName("something");

        builder.send();

        verify(metricsSender, times(0)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFreq.class), anyInt(), anyString(), anyLong());
    }

    @Test
    public void shouldIgnoreMetricNameWhenEmpty() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.metricName("");

        assertNull(builder.getMetricName());
    }

    @Test
    public void shouldIgnoreValueWhenEmpty() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.value("");

        assertNull(builder.getValue());
    }

    @Test
    public void shouldIgnoreConfigurationWhenNull() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.configuration(null);
        // Doesn't throw anything
    }

    @Test
    public void shouldIgnoreSampleRateWhenNull() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);

        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setNamespace("namespace");

        builder.configuration(config);

        assertEquals("Should have default sample rate", Integer.valueOf(100), builder.getSampleRate());
    }

    @Test
    public void shouldIgnoreNamespaceWhenNull() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setSampleRate(20);

        builder.configuration(config);

        assertEquals("Should have default namespace", "application", builder.getNamespace());
    }

    @Test
    public void shouldIgnoreAggregationFreqWhenNull() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(metricsSender);
        builder.aggFreq(null);

        assertNull(builder.getAggregationFreq());
    }

    @Test
    public void shouldNotThrowWhenSendingHasException() {
        AggregatedMetricsSenderAPI builder =  new AggregatedMetricsSenderAPI(null);
        builder.metricName("namespace").value("value");

        builder.send();
        // Doesn't throw anything
    }

    @Test
    public void shouldSendIfValid() {
        DefaultClientConfiguration config = new DefaultClientConfiguration();
        config.setNamespace("namespace");
        config.setSampleRate(50);

        AggregatedMetricsSenderAPI builder = new AggregatedMetricsSenderAPI(metricsSender);
        builder.configuration(config);

        builder.metricName("test")
                .aggregation(Aggregation.AVG)
                .aggFreq(AggregationFreq.FREQ_10)
                .sampleRate(75)
                .tag("tagKey", "tagValue")
                .value(String.valueOf(1000));

        builder.send();

        verify(metricsSender, times(1)).putAggregated(eq("test"),
                eq(String.valueOf(1000)),
                any(Tags.class),
                eq(Aggregation.AVG),
                eq(AggregationFreq.FREQ_10),
                eq(75),
                eq("namespace"),
                anyLong());
    }
}