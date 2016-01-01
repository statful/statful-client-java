package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.transport.TransportSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BufferedMetricsSenderTest {

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private TransportSender transportSender;

    private BufferedMetricsSender subject;

    @Before
    public void setUp() {
        initMocks(this);

        when(configuration.getFlushSize()).thenReturn(3);
        when(configuration.getFlushIntervalMillis()).thenReturn(0L);
        when(configuration.getPrefix()).thenReturn("test_prefix");
        when(configuration.getSampleRate()).thenReturn(100);

        subject = new BufferedMetricsSender(transportSender, configuration);
    }

    @Test
    public void shouldSendRawMetricWithTags() {
        // When
        Tags tags = new Tags();
        tags.putTag("app", "test_app");
        tags.putTag("unit", "ms");

        subject.put("test_metric", "500", tags, null, 10, 100, "application", "123456789");

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
        aggregations.putAll(asList(AVG, P90, COUNT, COUNT_PS));

        subject.put("test_metric", "500", null, aggregations, 10, 100, "application", "123456789");

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric 500 123456789 avg,p90,count,count_ps,10", buffer.get(0));
    }

    @Test
    public void shouldSendSimpleRawMetric() {
        // When
        subject.put("test_metric", "500", null, null, 10, 100, "application", "123456789");

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
        aggregations.putAll(asList(AVG, P90, COUNT, COUNT_PS));

        subject.put("test_metric", "500", tags, aggregations, 10, 100, "application", "123456789");

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should not be empty", 1, buffer.size());

        assertThat("Should buffer timer metric", buffer.get(0), anyOf(
                is("test_prefix.application.test_metric,app=test_app,unit=ms 500 123456789 avg,p90,count,count_ps,10"),
                is("test_prefix.application.test_metric,unit=ms,app=test_app 500 123456789 avg,p90,count,count_ps,10")));
    }

    @Test
    public void shouldBufferMetrics() {
        // When
        subject.put("test_metric0", "100", null, null, 10, 100, "application", "123456789");
        subject.put("test_metric1", "101", null, null, 10, 100, "application", "123456790");

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric0 100 123456789", buffer.get(0));
        assertEquals("Should buffer timer metric", "test_prefix.application.test_metric1 101 123456790", buffer.get(1));
    }

    @Test
    public void shouldFlushMetricsBySize() {
        // When
        subject.put("test_metric0", "100", null, null, 10, 100, "application", "123456789");
        subject.put("test_metric1", "101", null, null, 10, 100, "application", "123456790");
        subject.put("test_metric2", "102", null, null, 10, 100, "application", "123456791");
        subject.put("test_metric3", "103", null, null, 10, 100, "application", "123456792");

        // Then
        List<String> buffer = subject.getBuffer();
        assertEquals("Buffer should have 1 metric", 1, buffer.size());
    }

    @Test
    public void shouldFlushMetricsByTime() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        subject = new BufferedMetricsSender(transportSender, configuration);

        // When
        subject.put("test_metric0", "100", null, null, 10, 100, "application", "123456789");
        subject.put("test_metric1", "101", null, null, 10, 100, "application", "123456790");

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
        when(configuration.getFlushSize()).thenReturn(999);
        when(configuration.getFlushIntervalMillis()).thenReturn(200L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration);

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // When
        for (int i = 0; i < 1000; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    subject.put("test_metric", "100", null, null, 10, 100, "application", "123456789");
                }
            });
        }

        executorService.awaitTermination(150, TimeUnit.MILLISECONDS);

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
        subject.put("test_metric0", "100", null, null, 10, 0, "application", "123456789");
        subject.put("test_metric0", "100", null, null, 10, 50, "application", "123456789");
        subject.put("test_metric0", "100", null, null, 10, 100, "application", "123456789");

        // Then
        int size = subject.getBuffer().size();
        assertTrue("Buffer should have at least 1 metric and at most 2 metrics", size > 0 && size <= 2);
    }

    @Test
    public void shouldSendMetricWhenSampleRateIsAbove100() {
        // When
        subject.put("test_metric0", "100", null, null, 10, 101, "application", "123456789");

        // Then
        int size = subject.getBuffer().size();
        assertEquals("Buffer should have 1 metric", 1, size);
    }

    @Test
    public void shouldNotSendMetricWhenSampleRateIsBellow0() {
        // When
        subject.put("test_metric0", "100", null, null, 10, -1, "application", "123456789");

        // Then
        int size = subject.getBuffer().size();
        assertEquals("Buffer should have 0 metrics", 0, size);
    }
}