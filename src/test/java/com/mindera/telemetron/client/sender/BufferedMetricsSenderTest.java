package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.transport.TransportSender;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.AggregationFreq.FREQ_10;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BufferedMetricsSenderTest {

    private ScheduledExecutorService executorService;

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private TransportSender transportSender;

    private BufferedMetricsSender subject;

    @Before
    public void setUp() {
        initMocks(this);

        when(configuration.getFlushSize()).thenReturn(3);
        when(configuration.getFlushIntervalSeconds()).thenReturn(0);
        when(configuration.getPrefix()).thenReturn("test_prefix");
        when(configuration.getSampleRate()).thenReturn(100);

        executorService = Executors.newScheduledThreadPool(1);
        subject = new BufferedMetricsSender(transportSender, configuration, executorService, TimeUnit.MILLISECONDS);
    }

    @After
    public void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void shouldSendRawMetricWithTags() {
        // When
        Tags tags = new Tags();
        tags.putTag("app", "test_app");
        tags.putTag("unit", "ms");

        subject.put("test_metric", "500", tags, null, FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());

        assertThat("Should buffer timer metric", buffer.get(0), anyOf(
                is("test_prefix.application.test_metric,unit=ms,app=test_app 500 123456789"),
                is("test_prefix.application.test_metric,app=test_app,unit=ms 500 123456789")));
    }

    @Test
    public void shouldSendRawMetricWithAggregations() {
        // When
        Aggregations aggregations = new Aggregations();
        aggregations.putAll(asList(AVG, P90, COUNT));

        subject.put("test_metric", "500", null, aggregations, FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric 500 123456789 avg,p90,count,10", buffer.get(0));
    }

    @Test
    public void shouldSendSimpleRawMetric() {
        // When
        subject.put("test_metric", "500", null, null, FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric 500 123456789", buffer.get(0));
    }

    @Test
    public void shouldSendRawMetricWithTagsAndAggregations() {
        // When
        Tags tags = new Tags();
        tags.putTag("app", "test_app");
        tags.putTag("unit", "ms");

        Aggregations aggregations = new Aggregations();
        aggregations.putAll(asList(AVG, P90, COUNT));

        subject.put("test_metric", "500", tags, aggregations, FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());

        assertThat("Should buffer timer metric", buffer.get(0), anyOf(
                is("test_prefix.application.test_metric,app=test_app,unit=ms 500 123456789 avg,p90,count,10"),
                is("test_prefix.application.test_metric,unit=ms,app=test_app 500 123456789 avg,p90,count,10")));
    }

    @Test
    public void shouldBufferMetrics() {
        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, FREQ_10, 100, "application", 123456790);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric0 100 123456789", buffer.get(0));
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric1 101 123456790", buffer.get(1));
    }

    @Test
    public void shouldFlushMetricsBySize() {
        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, FREQ_10, 100, "application", 123456790);
        subject.put("test_metric2", "102", null, null, FREQ_10, 100, "application", 123456791);
        subject.put("test_metric3", "103", null, null, FREQ_10, 100, "application", 123456792);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have 1 metric", 1, buffer.size());
    }

    @Test
    public void shouldFlushMetricsByTime() throws Exception {
        // Given
        when(configuration.getFlushIntervalSeconds()).thenReturn(100);

        subject = new BufferedMetricsSender(transportSender, configuration, executorService, TimeUnit.MILLISECONDS);

        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, FREQ_10, 100, "application", 123456790);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric0 100 123456789", buffer.get(0));
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric1 101 123456790", buffer.get(1));

        // And then
        Thread.sleep(150);

        buffer = subject.getBuffer();
        assertTrue("Buffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldPutMetricsConcurrently() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(19);
        when(configuration.getFlushIntervalSeconds()).thenReturn(100);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService, TimeUnit.MILLISECONDS);

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // When
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    subject.put("test_metric", "100", null, null, FREQ_10, 100, "application", 123456789);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Then
        verify(transportSender, times(1)).send(anyString());

        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have metrics", 1, buffer.size());

        Thread.sleep(100);

        // And then
        buffer = subject.getBuffer();
        assertEquals("Buffer should not have metrics", 0, buffer.size());

        verify(transportSender, times(2)).send(anyString());
    }

    @Test
    public void shouldSendWithSampleRate() {
        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, 50, "application", 123456789);
        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);

        // Then
        int size = subject.getBuffer().size();
        assertTrue("Buffer should have at least 1 metric and at most 2 metrics", size > 0 && size <= 2);
    }

    @Test
    public void shouldSendMetricWhenSampleRateIsAbove100() {
        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, 101, "application", 123456789);

        // Then
        int size = subject.getBuffer().size();
        assertEquals("Buffer should have 1 metric", 1, size);
    }

    @Test
    public void shouldNotSendMetricWhenSampleRateIsBellow0() {
        // When
        subject.put("test_metric0", "100", null, null, FREQ_10, -1, "application", 123456789);

        // Then
        int size = subject.getBuffer().size();
        assertEquals("Buffer should have 0 metrics", 0, size);
    }

    @Test
    public void shouldShutdownAndClearMetrics() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(2);
        when(configuration.getFlushIntervalSeconds()).thenReturn(100);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService, TimeUnit.MILLISECONDS);

        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);

        // When
        subject.shutdown();

        Thread.sleep(500);

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not have metrics", 0, buffer.size());
    }

    @Test
    public void shouldNotSendWhenRunIsDry() {
        // Given
        when(configuration.getFlushSize()).thenReturn(1);
        when(configuration.isDryRun()).thenReturn(true);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService, TimeUnit.MILLISECONDS);

        subject.put("test_metric0", "100", null, null, FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "100", null, null, FREQ_10, 100, "application", 123456790);

        verify(transportSender, times(0)).send(anyString());
    }

    @Test
    public void shouldBeAbleToIngest4999MetricsInLessThan100msWithContention() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(5000);
        doAnswer(mockedTransportResponse).when(transportSender).send(anyString());

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // Adding a lot of contention
        ExecutorService executorService = Executors.newFixedThreadPool(200);

        // When
        final AtomicInteger counter = new AtomicInteger();

        long start = System.currentTimeMillis();

        for (int i = 0; i < 4999; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    subject.put("test_metric", "100", null, null, FREQ_10, 100, "application", 123456789);
                    counter.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        long end = System.currentTimeMillis();

        // Then
        assertEquals(4999, counter.get());

        assertTrue("Should ingest in less that 1 second", (end - start) < 100);

        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have all metrics in the buffer", 4999, buffer.size());

    }

    private Answer<String> mockedTransportResponse = new Answer<String>() {
        @Override
        public String answer(InvocationOnMock invocationOnMock) throws Throwable {
            // Simulates transport latency
            Thread.sleep(5);
            return null;
        }
    };
}