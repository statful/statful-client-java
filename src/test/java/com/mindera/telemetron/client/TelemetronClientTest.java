package com.mindera.telemetron.client;

import com.mindera.telemetron.client.api.Aggregation;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.MetricsSender;
import com.mindera.telemetron.client.transport.UdpEcho;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.MetricBuilder.*;
import static com.mindera.telemetron.client.api.NullMetricBuilders.*;
import static java.util.Arrays.asList;
import static org.junit.Assert.*;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TelemetronClientTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private MetricsSender metricsSender;

    private TelemetronClient subject;

    @Before
    public void setUp() {
        initMocks(this);

        when(configuration.getNamespace()).thenReturn("application");
        when(configuration.getSampleRate()).thenReturn(10);
        when(configuration.getTimerTags()).thenReturn(Tags.from("unit", "ms"));
        when(configuration.getTimerAggregations()).thenReturn(Aggregations.from(AVG, P90, COUNT, COUNT_PS));
        when(configuration.getCounterAggregations()).thenReturn(Aggregations.from(AVG, P90, COUNT_PS));
        when(configuration.getGaugeAggregations()).thenReturn(Aggregations.from(LAST));

        subject = new TelemetronClient(metricsSender, configuration);
    }

    @Test
    public void shouldSendSimpleTimerMetric() {
        // When
        subject.timer("response_time", 1000).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());

        // Then it should have aggregations
        shouldContainDefaultTimerAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendTimerWithTags() {
        // When
        subject.timer("response_time", 1000).with(tag("host", "localhost"), tag("cluster", "prod")).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(0), eq(10), eq("application"), anyString());

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
        subject.timer("response_time", 1000).with(agg(LAST)).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 5 aggregations", 5, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(P90));
        assertTrue("Should contain COUNT aggregation", aggregations.contains(COUNT));
        assertTrue("Should contain COUNT_PS aggregation", aggregations.contains(COUNT_PS));
        assertTrue("Should contain LAST aggregation", aggregations.contains(LAST));
    }

    @Test
    public void shouldSendTimerWithAggregationFrequency() {
        // When
        subject.timer("response_time", 1000).with(aggrFreq(5)).send();

        // Then
        ArgumentCaptor<Integer> aggrFreqArg = ArgumentCaptor.forClass(Integer.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyString());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        assertEquals("Aggregation frequency should be 5", Integer.valueOf(5), aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendTimerWithNameSpace() {
        // When
        subject.timer("response_time", 1000).with(namespace("client")).send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), any(Tags.class), any(Aggregations.class), eq(0), eq(10), namespaceArg.capture(), anyString());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldSendSimpleCounterMetric() {
        // When
        subject.counter("transactions").send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("0"), isNull(Tags.class), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have aggregations
        shouldContainDefaultCounterAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendCounterWithTags() {
        // When
        subject.counter("transactions").with(tag("host", "localhost")).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("0"), tagsArg.capture(), any(Aggregations.class), eq(0), eq(10), eq("application"), anyString());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldSendCounterWithAggregations() {
        // When
        subject.counter("transactions").with(AVG, LAST).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("0"), any(Tags.class), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 4 aggregations", 4, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(P90));
        assertTrue("Should contain COUNT_PS aggregation", aggregations.contains(COUNT_PS));
        assertTrue("Should contain LAST aggregation", aggregations.contains(LAST));
    }

    @Test
    public void shouldSendCounterWithAggregationFrequency() {
        // When
        subject.counter("transactions").with(aggrFreq(5)).send();

        // Then
        ArgumentCaptor<Integer> aggrFreqArg = ArgumentCaptor.forClass(Integer.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("0"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyString());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        assertEquals("Aggregation frequency should be 5", Integer.valueOf(5), aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendCounterWithNamespace() {
        // When
        subject.counter("transactions").with(namespace("client")).send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("counter.transactions"), eq("0"), isNull(Tags.class), any(Aggregations.class), eq(0), eq(10), namespaceArg.capture(), anyString());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldSendCounterWithIncrement() {
        // When
        subject.counter("transactions", 2).send();

        // Then
        verify(metricsSender).put(eq("counter.transactions"), eq("2"), any(Tags.class), any(Aggregations.class), eq(0), eq(10), eq("application"), anyString());
    }

    @Test
    public void shouldSendSimpleGaugeMetric() {
        // When
        subject.gauge("current_sessions", "2").send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have aggregations
        shouldContainDefaultGaugeAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldSendGaugeWithTags() {
        // When
        subject.gauge("current_sessions", "2").with(tag("host", "localhost")).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), tagsArg.capture(), any(Aggregations.class), eq(0), eq(10), eq("application"), anyString());

        // Then it should have tags
        Tags tags = tagsArg.getValue();
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain host tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldSendGaugeWithAggregations() {
        // When
        subject.gauge("current_sessions", "2").with(AVG, P90).send();

        // Then
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), any(Tags.class), aggrArg.capture(), eq(0), eq(10), eq("application"), anyString());

        // Then it should have aggregations
        assertNotNull("Aggregations should not be null", aggrArg.getValue());

        Collection<Aggregation> aggregations = aggrArg.getValue().getAggregations();
        assertEquals("Should contain 2 aggregations", 3, aggregations.size());
        assertTrue("Should contain AVG aggregation", aggregations.contains(AVG));
        assertTrue("Should contain P90 aggregation", aggregations.contains(P90));
        assertTrue("Should contain LAST aggregation", aggregations.contains(LAST));
    }

    @Test
    public void shouldSendGaugeWithAggregationFrequency() {
        // When
        subject.gauge("current_sessions", "2").with(aggrFreq(5)).send();

        // Then
        ArgumentCaptor<Integer> aggrFreqArg = ArgumentCaptor.forClass(Integer.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), any(Tags.class), any(Aggregations.class), aggrFreqArg.capture(), eq(10), eq("application"), anyString());

        assertNotNull("Aggregation frequency should not be null", aggrFreqArg.getValue());
        assertEquals("Aggregation frequency should be 5", Integer.valueOf(5), aggrFreqArg.getValue());
    }

    @Test
    public void shouldSendGaugeWithNamespace() {
        // When
        subject.gauge("current_sessions", "2").with(namespace("client")).send();

        // Then
        ArgumentCaptor<String> namespaceArg = ArgumentCaptor.forClass(String.class);

        verify(metricsSender).put(eq("gauge.current_sessions"), eq("2"), isNull(Tags.class), any(Aggregations.class), eq(0), eq(10), namespaceArg.capture(), anyString());

        assertEquals("Namespace should be 'client'", "client", namespaceArg.getValue());
    }

    @Test
    public void shouldNeverThrowExceptionWhenRegisteringTimer() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), anyInt(), anyInt(), anyString(), anyString());

        // When
        subject.timer("response_time", 1000)
                .with((Aggregation)null)
                .with(nullAggBuilder())
                .with(nullAggrFreqBuilder())
                .with(nullTagBuilder())
                .with(nullNamespaceBuilder())
                .withAggrFreq(null)
                .withNamespace(null)
                .send();
    }

    @Test
    public void shouldNeverThrowExceptionWhenRegisteringGauge() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), anyInt(), anyInt(), anyString(), anyString());

        // When
        subject.gauge("current_sessions", "2")
                .with((Aggregation)null)
                .with(nullAggBuilder())
                .with(nullAggrFreqBuilder())
                .with(nullTagBuilder())
                .with(nullNamespaceBuilder())
                .withAggrFreq(null)
                .withNamespace(null)
                .send();
    }

    @Test
    public void shouldNeverThrowExceptionWhenRegisteringCounter() {
        // Given
        doThrow(new NullPointerException()).when(metricsSender).put(anyString(), anyString(), any(Tags.class), any(Aggregations.class), anyInt(), anyInt(), anyString(), anyString());

        // When
        subject.counter("transactions").send();
        subject.counter("transactions", 2)
                .with((Aggregation)null)
                .with(nullAggBuilder())
                .with(nullAggrFreqBuilder())
                .with(nullTagBuilder())
                .with(nullNamespaceBuilder())
                .withAggrFreq(null)
                .withNamespace(null)
                .send();
    }

    @Test
    public void shouldMergeApplicationTags() {
        Tags defaultTimerTags = new Tags();
        defaultTimerTags.putTag("host", "localhost");
        defaultTimerTags.putTag("unit", "ms");

        when(configuration.getTimerTags()).thenReturn(defaultTimerTags);

        TelemetronClient subject = new TelemetronClient(metricsSender, configuration);

        // When
        subject.timer("response_time", 1000).with(tag("cluster", "prod")).send();

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), any(Aggregations.class), eq(0), eq(10), eq("application"), anyString());

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
        aggregations.putAll(asList(AVG, P90, COUNT, COUNT_PS));

        subject.put("timer.response_time", "1000", tags, aggregations, 10, 10, "application", "100000");

        // Then
        ArgumentCaptor<Tags> tagsArg = ArgumentCaptor.forClass(Tags.class);
        ArgumentCaptor<Aggregations> aggrArg = ArgumentCaptor.forClass(Aggregations.class);

        verify(metricsSender).put(eq("timer.response_time"), eq("1000"), tagsArg.capture(), aggrArg.capture(), eq(10), eq(10), eq("application"), eq("100000"));

        // Then it should have tags
        shouldContainDefaultTimerTags(tagsArg.getValue());

        // Then it should have aggregations
        shouldContainDefaultTimerAggregations(aggrArg.getValue());
    }

    @Test
    public void shouldShutdownClient() {
        // Given
        TelemetronClient subject = new TelemetronClient(metricsSender, configuration);

        // When
        subject.shutdown();

        // Then
        verify(metricsSender, times(1)).shutdown();
    }

    @Test
    public void shouldCreateUdpClient() throws Exception {
        // Given
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> response = executorService.submit(new UdpEcho(2013));

        // When
        TelemetronClient client = TelemetronClient.newUDPClient("test_prefix")
                .withFlushInterval(100)
                .build();
        client.counter("test_counter").send();

        String responseString = response.get();

        // Then
        assertTrue("Should receive message", responseString.startsWith("test_prefix.application.counter.test_counter 0"));
        executorService.shutdown();
    }

    private void shouldContainDefaultTimerTags(Tags tags) {
        assertNotNull("Tags should not be null", tags);
        assertEquals("Should contain 1 tag", 1, tags.getTags().size());
        assertEquals("Should contain unit tag", "ms", tags.getTagValue("unit"));
    }

    private void shouldContainDefaultTimerAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 4 aggregations", 4, aggregationsValue.size());
        assertTrue("Should contain AVG aggregation", aggregationsValue.contains(AVG));
        assertTrue("Should contain P90 aggregation", aggregationsValue.contains(P90));
        assertTrue("Should contain COUNT aggregation", aggregationsValue.contains(COUNT));
        assertTrue("Should contain COUNT_PS aggregation", aggregationsValue.contains(COUNT_PS));
    }

    private void shouldContainDefaultCounterAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 3 aggregations", 3, aggregationsValue.size());
        assertTrue("Should contain SUM aggregation", aggregationsValue.contains(AVG));
        assertTrue("Should contain COUNT aggregation", aggregationsValue.contains(P90));
        assertTrue("Should contain COUNT_PS aggregation", aggregationsValue.contains(COUNT_PS));
    }

    private void shouldContainDefaultGaugeAggregations(Aggregations aggregations) {
        assertNotNull("Aggregations should not be null", aggregations);

        Collection<Aggregation> aggregationsValue = aggregations.getAggregations();

        assertEquals("Should contain 3 aggregations", 1, aggregationsValue.size());
        assertTrue("Should contain LAST aggregation", aggregationsValue.contains(LAST));
    }
}