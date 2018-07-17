package com.statful.client.core;

import com.statful.client.domain.api.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Collection;

import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class StatfulClientImplTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private MetricsSender metricsSender;

    private StatfulClientImpl subject;

    @Before
    public void setUp() {
        initMocks(this);

        when(configuration.getNamespace()).thenReturn("application");
        when(configuration.getSampleRate()).thenReturn(10);
        when(configuration.getTimerTags()).thenReturn(Tags.from("unit", "ms"));
        when(configuration.getTimerAggregations()).thenReturn(Aggregations.from(Aggregation.AVG, Aggregation.P90, Aggregation.COUNT));
        when(configuration.getCounterAggregations()).thenReturn(Aggregations.from(Aggregation.AVG, Aggregation.P90));
        when(configuration.getGaugeAggregations()).thenReturn(Aggregations.from(Aggregation.LAST));
        when(configuration.getTimerAggregationFrequency()).thenReturn(AggregationFrequency.FREQ_10);
        when(configuration.getCounterAggregationFrequency()).thenReturn(AggregationFrequency.FREQ_10);
        when(configuration.getGaugeAggregationFrequency()).thenReturn(AggregationFrequency.FREQ_10);
        when(configuration.getDefaultAggregationFreq()).thenReturn(AggregationFrequency.FREQ_10);

        subject = new StatfulClientImpl(metricsSender, configuration);
    }

    @Test
    public void shouldSendTimerMetric() {
        // When
        subject.timer("response_time", 1000).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());

        // Then it should have aggregations
        shouldContainDefaultTimerAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendAggregatedTimerMetric() {
        // When
        subject.aggregatedTimer("response_time", 1000, Aggregation.AVG, AggregationFrequency.FREQ_300).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("timer.response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_300), eq(10), eq("application"), anyLong());

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());
    }

    @Test
    public void shouldSendSampledTimerMetric() {
        // When
        subject.sampledTimer("response_time", 1000, Integer.MAX_VALUE).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).putSampled(eq("timer.response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());

        // Then it should have aggregations
        shouldContainDefaultTimerAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendSampledAggregatedTimerMetric() {
        // When
        subject.sampledAggregatedTimer("response_time", 1000, Aggregation.AVG, AggregationFrequency.FREQ_300, Integer.MAX_VALUE).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedSampledPut(eq("timer.response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_300), eq(Integer.MAX_VALUE), eq("application"), anyLong());

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());
    }

    @Test
    public void shouldSendTimerWithTags() {
        // When
        subject.timer("response_time", 1000).with().tag("host", "localhost").tag("cluster", "prod").send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 3 tag", 3, tags.getTags().size());
        assertEquals("Should contain host tag", "prod", tags.getTagValue("cluster"));
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should contain unit tag", "ms", tags.getTagValue("unit"));
    }

    @Test
    public void shouldSendTimerWithAggregations() {
        // When
        subject.timer("response_time", 1000).with().aggregations(Aggregation.LAST).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 5 aggregations", 4, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(Aggregation.AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(Aggregation.P90));
        assertTrue("Should contain COUNT aggregation", aggregations.contains(Aggregation.COUNT));
        assertTrue("Should contain LAST aggregation", aggregations.contains(Aggregation.LAST));
    }

    @Test
    public void shouldSendTimerWithAggregationFrequency() {
        // When
        subject.timer("response_time", 1000).with().aggregationFrequency(AggregationFrequency.FREQ_120).send();

        // Then
        ArgumentCaptor<AggregationFrequency> aggrFreqArg = ArgumentCaptor.forClass(AggregationFrequency.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyLong());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        Assert.assertEquals("Aggregation frequency should be 5", AggregationFrequency.FREQ_120, aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendTimerWithNameSpace() {
        // When
        subject.timer("response_time", 1000).with().namespace("client").send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), namespaceArg.capture(), anyLong());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldSendCounterMetricWithDefaultValue() {
        // When
        subject.counter("transactions").send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("1"), isNull(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        shouldContainDefaultCounterAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendCounterWithTags() {
        // When
        subject.counter("transactions").with().tag("host", "localhost").send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("1"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldSendCounterWithAggregations() {
        // When
        subject.counter("transactions").with().aggregations(Aggregation.AVG, Aggregation.LAST).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("1"), any(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 4 aggregations", 3, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(Aggregation.AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(Aggregation.P90));
        assertTrue("Should contain LAST aggregation", aggregations.contains(Aggregation.LAST));
    }

    @Test
    public void shouldSendCounterWithAggregationFrequency() {
        // When
        subject.counter("transactions").with().aggregationFrequency(AggregationFrequency.FREQ_120).send();

        // Then
        ArgumentCaptor<AggregationFrequency> aggrFreqArg = ArgumentCaptor.forClass(AggregationFrequency.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("1"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyLong());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        Assert.assertEquals("Aggregation frequency should be 5", AggregationFrequency.FREQ_120, aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendCounterWithNamespace() {
        // When
        subject.counter("transactions").with().namespace("client").send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("1"), isNull(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), namespaceArg.capture(), anyLong());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldSendCounter() {
        // When
        subject.counter("transactions", 2).send();

        // Then
        verify(metricsSender).put(eq("counter.transactions"), eq("2"), any(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendAggregatedCounter() {
        // When
        subject.aggregatedCounter("transactions", 2, Aggregation.SUM, AggregationFrequency.FREQ_120).send();

        // Then
        verify(metricsSender).aggregatedPut(eq("counter.transactions"), eq("2"), any(Tags.class), eq(Aggregation.SUM), eq(AggregationFrequency.FREQ_120), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSampledSendCounter() {
        // When
        subject.sampledCounter("transactions", 2, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("counter.transactions"), eq("2"), any(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendIntegerGaugeMetric() {
        // When
        subject.gauge("current_sessions", 2).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        shouldContainDefaultGaugeAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendIntegerAggregatedGaugeMetric() {
        // When
        subject.aggregatedGauge("current_sessions", 2, Aggregation.FIRST, AggregationFrequency.FREQ_10).send();

        // Then
        verify(metricsSender).aggregatedPut(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendIntegerSampledGaugeMetric() {
        // When
        subject.sampledGauge("current_sessions", 2, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendIntegerSampledAggregatedGaugeMetric() {
        // When
        subject.sampledAggregatedGauge("current_sessions", 2, Aggregation.FIRST, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).aggregatedSampledPut(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendLongGaugeMetric() {
        // When
        subject.gauge("current_sessions", 2L).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        shouldContainDefaultGaugeAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendLongAggregatedGaugeMetric() {
        // When
        subject.aggregatedGauge("current_sessions", 2L, Aggregation.FIRST, AggregationFrequency.FREQ_10).send();

        // Then
        verify(metricsSender).aggregatedPut(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendLongSampledGaugeMetric() {
        // When
        subject.sampledGauge("current_sessions", 2L, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendLongSampledAggregatedGaugeMetric() {
        // When
        subject.sampledAggregatedGauge("current_sessions", 2L, Aggregation.FIRST, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).aggregatedSampledPut(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendDoubleGaugeMetric() {
        // When
        subject.gauge("current_sessions", 2.2).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2.2"), isNull(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        shouldContainDefaultGaugeAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendDoubleAggregatedGaugeMetric() {
        // When
        subject.aggregatedGauge("current_sessions", 2.2, Aggregation.FIRST, AggregationFrequency.FREQ_10).send();

        // Then
        verify(metricsSender).aggregatedPut(eq("gauge.current_sessions"), eq("2.2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSampledDoubleGaugeMetric() {

        // When
        subject.sampledGauge("current_sessions", 2.2, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("gauge.current_sessions"), eq("2.2"), isNull(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendDoubleSampledAggregatedGaugeMetric() {
        // When
        subject.sampledAggregatedGauge("current_sessions", 2.2, Aggregation.FIRST, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).aggregatedSampledPut(eq("gauge.current_sessions"), eq("2.2"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendFloatGaugeMetric() {
        // When
        subject.gauge("current_sessions", Float.valueOf("2.3")).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2.3"), isNull(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        shouldContainDefaultGaugeAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendFloatAggregatedGaugeMetric() {
        // When
        subject.aggregatedGauge("current_sessions", Float.valueOf("2.3"), Aggregation.FIRST, AggregationFrequency.FREQ_10).send();

        // Then
        verify(metricsSender).aggregatedPut(eq("gauge.current_sessions"), eq("2.3"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendFloatSampledGaugeMetric() {
        // When
        subject.sampledGauge("current_sessions", Float.valueOf("2.3"), Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("gauge.current_sessions"), eq("2.3"), isNull(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendFloatSampledAggregatedGaugeMetric() {
        // When
        subject.sampledAggregatedGauge("current_sessions", Float.valueOf("2.3"), Aggregation.FIRST, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).aggregatedSampledPut(eq("gauge.current_sessions"), eq("2.3"), isNull(Tags.class), eq(Aggregation.FIRST), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendGaugeWithTags() {
        // When
        subject.gauge("current_sessions", 2).with().tag("host", "localhost").send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldSendGaugeWithAggregations() {
        // When
        subject.gauge("current_sessions", 2).with().aggregations(Aggregation.AVG, Aggregation.P90).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), any(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 2 aggregations", 3, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(Aggregation.AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(Aggregation.P90));
        assertTrue("Should contain LAST aggregation", aggregations.contains(Aggregation.LAST));
    }

    @Test
    public void shouldSendGaugeWithAggregationFrequency() {
        // When
        subject.gauge("current_sessions", 2).with().aggregationFrequency(AggregationFrequency.FREQ_120).send();

        // Then
        ArgumentCaptor<AggregationFrequency> aggrFreqArg = ArgumentCaptor.forClass(AggregationFrequency.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyLong());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        Assert.assertEquals("Aggregation frequency should be 5", AggregationFrequency.FREQ_120, aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendGaugeWithNamespace() {
        // When
        subject.gauge("current_sessions", 2).with().namespace("client").send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), namespaceArg.capture(), anyLong());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldSendSimpleIntegerAggregatedMetric() {
        // When
        subject.aggregatedPut("response_time", 1000, Aggregation.AVG, AggregationFrequency.FREQ_10).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleIntegerSampledMetric() {
        // When
        subject.sampledPut("response_time", 1000, Integer.MAX_VALUE).send();


        verify(metricsSender).putSampled(eq("response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleIntegerSampledAggregatedMetric() {
        // When
        subject.sampledAggregatedPut("response_time", 1000, Aggregation.AVG, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedSampledPut(eq("response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleLongAggregatedMetric() {
        // When
        subject.aggregatedPut("response_time", 1000L, Aggregation.AVG, AggregationFrequency.FREQ_10).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleLongSampledAggregatedMetric() {
        // When
        subject.sampledAggregatedPut("response_time", 1000L, Aggregation.AVG, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedSampledPut(eq("response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleFloatAggregatedMetric() {
        // When
        subject.aggregatedPut("response_time", 1000F, Aggregation.AVG, AggregationFrequency.FREQ_10).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("response_time"), eq("1000.0"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleFloatSampledMetric() {
        // When
        subject.sampledPut("response_time", 1000F, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).putSampled(eq("response_time"), eq("1000.0"), any(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleFloatSampledAggregatedMetric() {
        // When
        subject.sampledAggregatedPut("response_time", 1000F,Aggregation.AVG, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        verify(metricsSender).aggregatedSampledPut(eq("response_time"), eq("1000.0"), any(Tags.class), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleDoubleAggregatedMetric() {
        // When
        subject.aggregatedPut("response_time", 1000D, Aggregation.AVG, AggregationFrequency.FREQ_10).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("response_time"), eq("1000.0"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleDoubleSampledAggregatedMetric() {
        // When
        subject.sampledAggregatedPut("response_time", 1000D, Aggregation.AVG, AggregationFrequency.FREQ_10, Integer.MAX_VALUE).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedSampledPut(eq("response_time"), eq("1000.0"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleIntegerMetric() {
        // When
        subject.put("response_time", 1000).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleLongMetric() {
        // When
        subject.put("response_time", 1000L).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleFloatMetric() {
        // When
        subject.put("response_time", 1000F).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("response_time"), eq("1000.0"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleDoubleMetric() {
        // When
        subject.put("response_time", 1000D).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("response_time"), eq("1000.0"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());
    }

    @Test
    public void shouldSendSimpleMetricWithTags() {
        // When
        subject.put("response_time", 1000).with().tag("host", "localhost").tag("cluster", "prod").send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 2 tag", 2, tags.getTags().size());
        assertEquals("Should contain host tag", "prod", tags.getTagValue("cluster"));
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldSendSimpleMetricWithAggregations() {
        // When
        subject.put("response_time", 1000).with().aggregations(Aggregation.LAST).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), any(Tags.class), aggrArg.capture(), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 1 aggregations", 1, aggregations.size());
        assertTrue("Should contain LAST aggregation", aggregations.contains(Aggregation.LAST));
    }

    @Test
    public void shouldSendSimpleMetricWithAggregationFrequency() {
        // When
        subject.put("response_time", 1000).with().aggregationFrequency(AggregationFrequency.FREQ_120).send();

        // Then
        ArgumentCaptor<AggregationFrequency> aggrFreqArg = ArgumentCaptor.forClass(AggregationFrequency.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyLong());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        Assert.assertEquals("Aggregation frequency should be 5", AggregationFrequency.FREQ_120, aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendSimpleMetricWithNameSpace() {
        // When
        subject.put("response_time", 1000).with().namespace("client").send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), namespaceArg.capture(), anyLong());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }


    @Test
    public void shouldNeverThrowExceptionWhenRegisteringTimer() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), anyInt(), anyString(), anyLong());

        // When
        subject.timer("response_time", 1000).with()
                .aggregations((Aggregation) null)
                .tag(null, null)
                .aggregationFrequency(null)
                .namespace(null)
                .send();
    }

    @Test
    public void shouldNeverThrowExceptionWhenRegisteringGauge() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), anyInt(), anyString(), anyLong());

        // When
        subject.gauge("current_sessions", 2).with()
                .aggregations((Aggregation) null)
                .tag(null, null)
                .aggregationFrequency(null)
                .namespace(null)
                .send();
    }

    @Test
    public void shouldNeverThrowExceptionWhenRegisteringCounter() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), any(AggregationFrequency.class), anyInt(), anyString(), anyLong());

        // When
        subject.counter("transactions").send();
        subject.counter("transactions", 2).with()
                .aggregations((Aggregation) null)
                .tag(null, null)
                .aggregationFrequency(null)
                .namespace(null)
                .send();
    }

    @Test
    public void shouldMergeApplicationTags() {
        Tags defaultTimerTags = new Tags();
        defaultTimerTags.putTag("host", "localhost");
        defaultTimerTags.putTag("unit", "ms");

        when(configuration.getTimerTags()).thenReturn(defaultTimerTags);

        StatfulClientImpl subject = new StatfulClientImpl(metricsSender, configuration);

        // When
        subject.timer("response_time", 1000).with().tag("cluster", "prod").send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), anyLong());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 3 tag", 3, tags.getTags().size());
        assertEquals("Should contain unit tag", "ms", tags.getTagValue("unit"));
        assertEquals("Should contain unit tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should contain unit tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldPutRawMetrics() {
        // When
        Tags tags = new Tags();
        tags.putTag("unit", "ms");

        Aggregations aggregations = new Aggregations();
        aggregations.putAll(asList(Aggregation.AVG, Aggregation.P90, Aggregation.COUNT));

        subject.put("timer.response_time", "1000", tags, aggregations, AggregationFrequency.FREQ_120, 10, "application", 100000);

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(AggregationFrequency.FREQ_120), eq(10), eq("application"), eq(100000L));

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());

        // Then it should have aggregations
        shouldContainDefaultTimerAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldPutRawAggregatedMetrics() {
        // When
        Tags tags = new Tags();
        tags.putTag("unit", "ms");

        subject.aggregatedPut("timer.response_time", "1000", tags, Aggregation.AVG, AggregationFrequency.FREQ_10, 10, "application", 100000);

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).aggregatedPut(eq("timer.response_time"), eq("1000"), tagsArg.capture(), eq(Aggregation.AVG), eq(AggregationFrequency.FREQ_10), eq(10), eq("application"), eq(100000L));

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());
    }

    @Test
    public void shouldPutRawSampledMetrics() {
        // When
        Tags tags = new Tags();
        tags.putTag("unit", "ms");

        subject.putSampled("timer.response_time", "1000", tags, Aggregations.from(Aggregation.AVG), AggregationFrequency.FREQ_10, Integer.MAX_VALUE, "application", 100000);

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).putSampled(eq("timer.response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(AggregationFrequency.FREQ_10), eq(Integer.MAX_VALUE), eq("application"), eq(100000L));

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());
    }

    @Test
    public void shouldShutdownClient() {
        // Given
        StatfulClientImpl subject = new StatfulClientImpl(metricsSender, configuration);

        // When
        subject.shutdown();

        // Then
        verify(metricsSender, times(1)).shutdown();
    }

    @Test
    public void shouldDisableAndDisableStatfulClient() {
        // When
        subject.timer("response_time", 1000).send();
        subject.disable();
        subject.timer("response_time", 1000).send();
        subject.enable();
        subject.timer("response_time", 1000).send();

        // Then
        verify(metricsSender, times(2)).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class),
                any(AggregationFrequency.class), anyInt(), anyString(), anyLong());
    }

    private void shouldContainDefaultTimerTags(Tags tags) {
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain unit tag", "ms", tags.getTagValue("unit"));
    }

    private void shouldContainDefaultTimerAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 4 aggregations", 3, aggregationsValue.size());
        assertTrue("Should contain AVG aggregation", aggregationsValue.contains(Aggregation.AVG));
        assertTrue("Should contain P90 aggregation", aggregationsValue.contains(Aggregation.P90));
        assertTrue("Should contain COUNT aggregation", aggregationsValue.contains(Aggregation.COUNT));
    }

    private void shouldContainDefaultCounterAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 3 aggregations", 2, aggregationsValue.size());
        assertTrue("Should contain SUM aggregation", aggregationsValue.contains(Aggregation.AVG));
        assertTrue("Should contain COUNT aggregation", aggregationsValue.contains(Aggregation.P90));
    }

    private void shouldContainDefaultGaugeAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 3 aggregations", 1, aggregationsValue.size());
        assertTrue("Should contain LAST aggregation", aggregationsValue.contains(Aggregation.LAST));
    }
}