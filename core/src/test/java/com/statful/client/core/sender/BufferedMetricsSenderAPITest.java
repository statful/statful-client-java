package com.statful.client.core.sender;

import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import java.util.List;
import java.util.Map;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Logger;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class BufferedMetricsSenderAPITest {

    private static final Logger LOGGER = Logger.getLogger(BufferedMetricsSenderAPITest.class.getName());

    private ScheduledThreadPoolExecutor executorService;

    @Mock
    private ClientConfiguration configuration;

    @Mock
    private TransportSender transportSender;

    private BufferedMetricsSender subject;

    @Before
    public void setUp() {
        initMocks(this);

        when(configuration.getMaxWorkerTasksQueueSize()).thenReturn(100);
        when(configuration.getWorkerTaskKillerInterval()).thenReturn(30000L);
        when(configuration.getFlushSize()).thenReturn(3);
        when(configuration.getFlushIntervalMillis()).thenReturn(0L);
        when(configuration.getSampleRate()).thenReturn(100);

        executorService = new ScheduledThreadPoolExecutor(1);
        subject = new BufferedMetricsSender(transportSender, configuration, executorService);
    }

    @After
    public void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void shouldDiscardIfStandardBufferIsFull() {
        // Given
        when(configuration.getFlushSize()).thenReturn(10000);

        BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        for (int i = 0; i < 5000; i++) {
            subject.put("test_metric", "500", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        }

        // When
        subject.put("test_metric_overflow", "500", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have 5000 metrics", 5000, buffer.size());
    }

    @Test
    public void shouldDiscardIfAggregatedBufferIsFull() {
        // Given
        when(configuration.getFlushSize()).thenReturn(10000);

        BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        for (int i = 0; i < 5000; i++) {
            subject.aggregatedPut("test_metric", "500", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        }

        // When
        subject.aggregatedPut("test_metric_overflow", "500", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        Map<Aggregation, Map<AggregationFrequency, List<String>>> buffer = subject.getAggregatedBuffer();
        assertEquals("MetricsBuffer should have 5000 metrics", 5000, buffer.get(Aggregation.AVG).get(AggregationFrequency.FREQ_10).size());
    }

    @Test
    public void shouldSendRawMetricWithTags() {
        // When
        Tags tags = new Tags();
        tags.putTag("app", "test_app");
        tags.putTag("unit", "ms");

        subject.put("test_metric", "500", tags, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not be empty", 1, buffer.size());

        assertThat("Should buffer timer metric", buffer.get(0), anyOf(
                is("application.test_metric,unit=ms,app=test_app 500 123456789 100"),
                is("application.test_metric,app=test_app,unit=ms 500 123456789 100")));
    }

    @Test
    public void shouldSendRawMetricWithAggregations() {
        // When
        Aggregations aggregations = new Aggregations();
        aggregations.putAll(asList(Aggregation.AVG, Aggregation.P90, Aggregation.COUNT));

        subject.put("test_metric", "500", null, aggregations, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric 500 123456789 avg,p90,count,10 100", buffer.get(0));
    }

    @Test
    public void shouldSendSimpleRawMetric() {
        // When
        subject.put("test_metric", "500", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric 500 123456789 100", buffer.get(0));
    }

    @Test
    public void shouldSendSimpleRawAggregatedMetric() {
        // When
        subject.aggregatedPut("test_metric", "500", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should not be empty", 1, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric 500 123456789 100", buffer.get(0));
    }

    @Test
    public void shouldSendRawMetricWithTagsAndAggregations() {
        // When
        Tags tags = new Tags();
        tags.putTag("app", "test_app");
        tags.putTag("unit", "ms");

        Aggregations aggregations = new Aggregations();
        aggregations.putAll(asList(Aggregation.AVG, Aggregation.P90, Aggregation.COUNT));

        subject.put("test_metric", "500", tags, aggregations, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not be empty", 1, buffer.size());

        assertThat("Should buffer timer metric", buffer.get(0), anyOf(
                is("application.test_metric,app=test_app,unit=ms 500 123456789 avg,p90,count,10 100"),
                is("application.test_metric,unit=ms,app=test_app 500 123456789 avg,p90,count,10 100")));
    }

    @Test
    public void shouldBufferMetrics() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456790);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric0 100 123456789 100", buffer.get(0));
        assertEquals("Should buffer timer metric", "application.test_metric1 101 123456790 100", buffer.get(1));
    }

    @Test
    public void shouldBufferAggregatedMetrics() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456790);

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric0 100 123456789 100", buffer.get(0));
        assertEquals("Should buffer timer metric", "application.test_metric1 101 123456790 100", buffer.get(1));
    }

    @Test
    public void shouldFlushMetricsBySize() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.put("test_metric2", "102", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456791);
        subject.put("test_metric3", "103", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456792);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have 1 metric", 1, buffer.size());
    }

    @Test
    public void shouldFlushMetricsByTime() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "101", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456790);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have 2 metrics", 2, buffer.size());
        assertEquals("Should buffer timer metric", "application.test_metric0 100 123456789 100", buffer.get(0));
        assertEquals("Should buffer timer metric", "application.test_metric1 101 123456790 100", buffer.get(1));

        // And then
        Thread.sleep(150);

        buffer = subject.getStandardBuffer();
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldFlushMetricsBySizeIfAnyBufferTypeGoesOverMaxSize() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric2", "102", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456791);
        subject.aggregatedPut("test_metric3", "103", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456792);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());

        // Then
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should have 1 metric", 1, buffer.size());
    }

    @Test
    public void shouldFlushMetricsBySizeIfAnyBufferSubTypeGoesOverMaxSize() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_120, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_120, 100, "application", 123456790);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric2", "102", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456791);
        subject.aggregatedPut("test_metric3", "103", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456792);

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should have 1 metric", 1, buffer.size());
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_120);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldFlushMetricsBySizeIfAnyAggregationBufferTypeGoesOverMaxSize() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.COUNT, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.COUNT, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric2", "102", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456791);
        subject.aggregatedPut("test_metric3", "103", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456792);

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.COUNT).get(AggregationFrequency.FREQ_10);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should have 1 metric", 1, buffer.size());
    }

    @Test
    public void shouldPutMetricsConcurrently() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(19);
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        ExecutorService executorService = Executors.newFixedThreadPool(20);

        // When
        for (int i = 0; i < 20; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    subject.put("test_metric", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have metrics", 1, buffer.size());

        Thread.sleep(500);

        // And then
        buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not have metrics", 0, buffer.size());
    }

    @Test
    public void shouldSendWithSampleRate() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 50, "application", 123456789);
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        int size = subject.getStandardBuffer().size();
        assertTrue("MetricsBuffer should have at least 1 metric and at most 2 metrics", size > 0 && size <= 2);
    }

    @Test
    public void shouldSendAggregatedWithSampleRate() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 50, "application", 123456789);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        int size = subject.getAggregatedBuffer().size();
        assertTrue("MetricsBuffer should have at least 1 metric and at most 2 metrics", size > 0 && size <= 2);
    }

    @Test
    public void shouldNotSendMetricWhenSampleRateIsNull() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, null, "application", 123456789);

        // Then
        int size = subject.getStandardBuffer().size();
        assertEquals("MetricsBuffer should have 0 metrics", 0, size);
    }

    @Test
    public void shouldNotSendMetricWhenSampleRateIsAbove100() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 101, "application", 123456789);

        // Then
        int size = subject.getStandardBuffer().size();
        assertEquals("MetricsBuffer should have 0 metrics", 0, size);
    }

    @Test
    public void shouldNotSendAggregatedMetricWhenSampleRateIsAbove100() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 101, "application", 123456789);
        // Then
        int size = subject.getAggregatedBuffer().size();
        assertEquals("MetricsBuffer should have 0 metrics", 0, size);
    }

    @Test
    public void shouldNotSendMetricWhenSampleRateIsBellow0() {
        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, -1, "application", 123456789);

        // Then
        int size = subject.getStandardBuffer().size();
        assertEquals("MetricsBuffer should have 0 metrics", 0, size);
    }


    @Test
    public void shouldNotSendAggregatedMetricWhenSampleRateIsBellow0() {
        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, -1, "application", 123456789);

        // Then
        int size = subject.getAggregatedBuffer().size();
        assertEquals("MetricsBuffer should have 0 metrics", 0, size);
    }

    @Test
    public void shouldShutdownAndClearMetrics() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(2);
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // When
        subject.shutdown();

        Thread.sleep(500);

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should not have metrics", 0, buffer.size());
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertEquals("MetricsBuffer should not have metrics", 0, buffer.size());
    }

    @Test
    public void shouldNotSendWhenRunIsDry() {
        // Given
        when(configuration.getFlushSize()).thenReturn(1);
        when(configuration.isDryRun()).thenReturn(true);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456790);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric1", "101", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456790);

        verify(transportSender, times(0)).send(anyString());
    }

    @Test
    public void shouldBeAbleToIngest4999MetricsInLessWithContention() throws Exception {
        // Given
        when(configuration.getFlushSize()).thenReturn(5000);
        doAnswer(mockedTransportResponse).when(transportSender).send(anyString());

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // Adding some contention
        int numberOfCores = Runtime.getRuntime().availableProcessors();
        LOGGER.info("CPUs: " + numberOfCores);
        ExecutorService executorService = Executors.newFixedThreadPool(numberOfCores);

        // When
        final AtomicInteger counter = new AtomicInteger();

        for (int i = 0; i < 4999; i++) {
            executorService.execute(new Runnable() {
                @Override
                public void run() {
                    subject.put("test_metric", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
                    counter.incrementAndGet();
                }
            });
        }

        executorService.shutdown();
        executorService.awaitTermination(500, TimeUnit.MILLISECONDS);

        // Then
        assertEquals(4999, counter.get());

        List<String> buffer = subject.getStandardBuffer();
        assertEquals("MetricsBuffer should have all metrics in the buffer", 4999, buffer.size());
    }

    @Test
    public void shouldFlushBothMetricBuffersSynchronously() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.forceSyncFlush();

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldFlushStandardMetricBuffersSynchronously() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.forceSyncFlush();

        // Then
        List<String> buffer = subject.getStandardBuffer();
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    @Ignore
    public void shouldFlushAggregatedMetricBuffersSynchronously() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.forceSyncFlush();

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldHandleFlushAggregatedMetricBuffersSynchronouslyWithEmptyBuffers() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.forceSyncFlush();
        subject.aggregatedPut("test_metric0", "100", null, Aggregation.AVG, AggregationFrequency.FREQ_120, 100, "application", 123456789);
        subject.forceSyncFlush();

        // Then
        List<String> buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_10);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
        buffer = subject.getAggregatedBuffer().get(Aggregation.AVG).get(AggregationFrequency.FREQ_120);
        assertTrue("MetricsBuffer should be empty", buffer.isEmpty());
    }

    @Test
    public void shouldNotCallSenderWhenFlushingSynchronouslyWithEmptyBuffer() throws Exception {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(100L);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, executorService);

        // When
        subject.forceSyncFlush();

        // Then
        verify(transportSender, times(0)).send(anyString());
    }

    @Test
    public void shouldReturnEmptyListOnEmptyOrMissingStandardBuffer() {
        // When
        List<String> bufferList = subject.getStandardBuffer();

        // Then
        assertTrue("Buffer list should be empty", bufferList.isEmpty());
    }

    @Test
    public void shouldReturnEmptyListOnEmptyOrMissingAggregatedBuffers() {
        // When
        Map<Aggregation, Map<AggregationFrequency, List<String>>> buffersMap = subject.getAggregatedBuffer();

        // Then
        for (Aggregation aggregation : buffersMap.keySet()) {
            for (AggregationFrequency aggregationFrequency : buffersMap.get(aggregation).keySet()) {
                assertTrue("Buffer list should be empty", buffersMap.get(aggregation).get(aggregationFrequency).isEmpty());
            }
        }
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldDropMetricsWhenWorkerTasksQueueIsFull() {
        // Given
        when(configuration.getFlushIntervalMillis()).thenReturn(5000L);
        when(configuration.getFlushSize()).thenReturn(1);
        when(configuration.getMaxWorkerTasksQueueSize()).thenReturn(1);


        final ScheduledFuture mockFuture = mock(ScheduledFuture.class);

        BlockingQueue<Runnable> mockQueue = mock(BlockingQueue.class);
        ScheduledThreadPoolExecutor mockExecutor = mock(ScheduledThreadPoolExecutor.class);
        when(mockExecutor.schedule(any(Runnable.class), anyInt(), any(TimeUnit.class))).thenReturn(mockFuture);
        when(mockExecutor.getQueue()).thenReturn(mockQueue);

        when(mockQueue.size()).thenReturn(0, 1);

        final BufferedMetricsSender subject = new BufferedMetricsSender(transportSender, configuration, mockExecutor);

        // When
        subject.put("test_metric0", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);
        subject.put("test_metric1", "100", null, null, AggregationFrequency.FREQ_10, 100, "application", 123456789);

        // Then
        verify(mockExecutor, times(1)).schedule(any(Runnable.class), anyInt(), any(TimeUnit.class));
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