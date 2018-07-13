package com.statful.client.core.sender;

import com.statful.client.core.buffer.AggregatedBuffer;
import com.statful.client.core.buffer.StandardBuffer;
import com.statful.client.core.message.MessageBuilder;
import com.statful.client.core.transport.ApiUriFactory;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.*;

import java.util.*;
import java.util.concurrent.*;
import java.util.logging.Logger;

import static java.util.Arrays.asList;

/**
 * This class sends the metrics to Statful using a {@link com.statful.client.core.transport.TransportSender}.
 * And buffers the metrics before sending them, according with
 * {@link com.statful.client.domain.api.ClientConfiguration}.
 * <p>
 * It sends the metrics using {@link com.statful.client.core.transport.TransportSender} in both a periodic way and
 * by checking the number of metrics in the buffer according with the
 * {@link com.statful.client.domain.api.ClientConfiguration} passed in the constructor. The periodic flushes are
 * handled by a {@link java.util.concurrent.ScheduledExecutorService} passed in the constructor.
 * <p>
 * The flushes are execute asynchronously by the passed {@link java.util.concurrent.ScheduledExecutorService}, which
 * can be handled by a single thread in the majority of the cases.
 * <p>
 * Instances of this class are thread-safe.
 */
public class BufferedMetricsSender implements MetricsSender {

    private static final Logger LOGGER = Logger.getLogger(BufferedMetricsSender.class.getName());

    private static final int MAX_BUFFER_SIZE = 5000;
    private static final int MIN_SAMPLE_RATE = 1;
    private static final int MAX_SAMPLE_RATE = 100;
    private static final int SAMPLE_RATE_DIVIDER = 100;
    private static final int MIN_FLUSH_INTERVAL = 50;

    private static final int FUTURE_QUEUE_MAX_SIZE = 5000;

    private final ClientConfiguration configuration;
    private final TransportSender transportSender;
    private final ScheduledThreadPoolExecutor executorService;
    private final boolean dryRun;
    private final StandardBuffer standardBuffer;
    private final AggregatedBuffer aggregatedBuffer;
    private final ArrayBlockingQueue<Future<?>> futuresQueue;

    private final ScheduledExecutorService helperExecutorService = Executors.newSingleThreadScheduledExecutor();

    /**
     * Default constructor.
     *
     * @param transportSender The {@link com.statful.client.core.transport.TransportSender} to send metrics
     * @param configuration   The {@link com.statful.client.domain.api.ClientConfiguration}
     * @param executorService The {@link java.util.concurrent.ScheduledExecutorService} to handle flushes
     */
    public BufferedMetricsSender(
            final TransportSender transportSender,
            final ClientConfiguration configuration,
            final ScheduledThreadPoolExecutor executorService
    ) {
        this.configuration = configuration;
        this.transportSender = transportSender;
        this.executorService = executorService;
        this.dryRun = configuration.isDryRun();
        this.standardBuffer = new StandardBuffer(MAX_BUFFER_SIZE, configuration.getFlushSize());
        this.aggregatedBuffer = new AggregatedBuffer(MAX_BUFFER_SIZE, configuration.getFlushSize());

        this.futuresQueue = new ArrayBlockingQueue<Future<?>>(FUTURE_QUEUE_MAX_SIZE);

        executorService.setRemoveOnCancelPolicy(true);
        startFlushInterval(configuration.getFlushIntervalMillis());
        startKillerInterval();
    }

    @Override
    public final void put(
            final String name, final String value, final Tags tags, final Aggregations aggregations,
            final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace,
            final long timestamp
    ) {
        if (!this.isValidSampleRate(sampleRate)) {
            LOGGER.warning("Invalid sample rate supplied. Discarding metric.");
            return;
        }
        if (shouldPutMetric(sampleRate)) {
            putMetric(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace, timestamp);
        }
    }

    @Override
    public final void putSampled(final String name, final String value, final Tags tags, final Aggregations aggregations,
                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace, final long timestamp) {
        if (!this.isValidSampleRate(sampleRate)) {
            LOGGER.warning("Invalid sample rate supplied. Discarding metric.");
            return;
        }

        putMetric(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace, timestamp);
    }

    private void putMetric(final String name, final String value, final Tags tags, final Aggregations aggregations,
                           final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace, final long timestamp) {
        String rawMessage = MessageBuilder.newBuilder()
                .withName(name)
                .withValue(value)
                .withTags(tags)
                .withAggregations(aggregations)
                .withAggregationFreq(aggregationFrequency)
                .withNamespace(namespace)
                .withTimestamp(timestamp)
                .withSampleRate(sampleRate)
                .build();

        if (!dryRun) {
            this.putRaw(rawMessage);
        } else {
            LOGGER.fine("Dry metric: " + rawMessage);
        }
    }

    @Override
    public final void aggregatedPut(final String name, final String value, final Tags tags, final Aggregation aggregation,
                                    final AggregationFrequency aggregationFrequency, final Integer sampleRate,
                                    final String namespace, final long timestamp) {
        if (!this.isValidSampleRate(sampleRate)) {
            LOGGER.warning("Invalid sample rate supplied. Discarding metric.");
            return;
        }
        if (shouldPutMetric(sampleRate)) {
            String rawMessage = MessageBuilder.newBuilder()
                    .withName(name)
                    .withValue(value)
                    .withTags(tags)
                    .withNamespace(namespace)
                    .withTimestamp(timestamp)
                    .withSampleRate(sampleRate)
                    .build();

            if (!dryRun) {
                this.putAggregatedRaw(rawMessage, aggregation, aggregationFrequency);
            } else {
                LOGGER.fine("Dry metric: " + rawMessage
                        + " Aggregation: " + aggregation
                        + " Frequency: " + aggregationFrequency);
            }
        }
    }

    @Override
    public final void aggregatedSampledPut(final String name, final String value, final Tags tags, final Aggregation aggregation,
                                           final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace,
                                           final long timestamp) {
        if (!this.isValidSampleRate(sampleRate)) {
            LOGGER.warning("Invalid sample rate supplied. Discarding metric.");
            return;
        }
            String rawMessage = MessageBuilder.newBuilder()
                    .withName(name)
                    .withValue(value)
                    .withTags(tags)
                    .withNamespace(namespace)
                    .withTimestamp(timestamp)
                    .withSampleRate(sampleRate)
                    .build();

            if (!dryRun) {
                this.putAggregatedRaw(rawMessage, aggregation, aggregationFrequency);
            } else {
                LOGGER.fine("Dry metric: " + rawMessage
                        + " Aggregation: " + aggregation
                        + " Frequency: " + aggregationFrequency);
            }
    }

    @Override
    public final void shutdown() {
        transportSender.shutdown();
    }

    @Override
    public final void forceSyncFlush() {
        String message = standardBuffer.readBuffer();
        if (!message.isEmpty()) {
            sendMetricSynchronously(message);
        }

        Set<Aggregation> aggregations = aggregatedBuffer.getAggregations();
        for (Aggregation aggregation : aggregations) {
            Set<AggregationFrequency> aggregationFrequencies = aggregatedBuffer.getAggregationFrequencies(aggregation);
            for (AggregationFrequency aggregationFrequency : aggregationFrequencies) {
                String aggregatedMessages = aggregatedBuffer.readBuffer(aggregation, aggregationFrequency);

                if (!aggregatedMessages.isEmpty()) {
                    sendAggregatedMetricSynchronously(aggregatedMessages, aggregation, aggregationFrequency);
                }
            }
        }
    }

    private void startFlushInterval(final long flushInterval) {
        if (flushInterval >= MIN_FLUSH_INTERVAL) {
            helperExecutorService.scheduleAtFixedRate(flusher(), flushInterval, flushInterval, TimeUnit.MILLISECONDS);
        }
    }

    private void startKillerInterval() {
        long killerInterval = configuration.getWorkerTaskKillerInterval();
        helperExecutorService.scheduleAtFixedRate(tasksKiller(), killerInterval, killerInterval, TimeUnit.MILLISECONDS);
    }

    private Runnable flusher() {
        return new Runnable() {
            @Override
            public void run() {
                flush();
            }
        };
    }

    private boolean shouldPutMetric(final int sampleRate) {
        return Math.random() <= (double) sampleRate / SAMPLE_RATE_DIVIDER;
    }

    private boolean isValidSampleRate(final Integer sampleRate) {
        return sampleRate != null && sampleRate >= MIN_SAMPLE_RATE && sampleRate <= MAX_SAMPLE_RATE;
    }

    private void putRaw(final String metric) {
        boolean inserted = standardBuffer.addToBuffer(metric);
        if (!inserted) {
            // We should discard older metrics instead
            LOGGER.warning("The buffer is full, metric ignored!.");
        }

        if (standardBuffer.isTimeToFlush()) {
            flush();
        }
    }

    private void putAggregatedRaw(final String metric, final Aggregation aggregation, final AggregationFrequency aggregationFrequency) {
        boolean inserted = aggregatedBuffer.addToBuffer(metric, aggregation, aggregationFrequency);
        if (!inserted) {
            // We should discard older metrics instead
            LOGGER.warning("The buffer is full, metric ignored!.");
        }

        if (aggregatedBuffer.isTimeToFlush()) {
            flush();
        }
    }

    private void flush() {
        String standardMessages = standardBuffer.readBuffer();
        if (!standardMessages.isEmpty()) {
            sendMetric(standardMessages);
        }

        Set<Aggregation> aggregations = aggregatedBuffer.getAggregations();

        for (Aggregation aggregation : aggregations) {
            Set<AggregationFrequency> aggregationFrequencies = aggregatedBuffer.getAggregationFrequencies(aggregation);
            for (AggregationFrequency aggregationFrequency : aggregationFrequencies) {
                String aggregatedMessages = aggregatedBuffer.readBuffer(aggregation, aggregationFrequency);

                if (!aggregatedMessages.isEmpty()) {
                    sendAggregatedMetric(aggregatedMessages, aggregation, aggregationFrequency);
                }
            }
        }
    }

    private void sendMetric(final String metric) {
        scheduleTask(new Runnable() {
            @Override
            public void run() {
                transportSender.send(metric);
            }
        });
    }

    private void sendAggregatedMetric(final String metric,
                                      final Aggregation aggregation,
                                      final AggregationFrequency aggregationFrequency) {
        scheduleTask(new Runnable() {
            @Override
            public void run() {
                transportSender.send(metric, buildAggregatedUri(aggregation, aggregationFrequency));
            }
        });
    }

    private void sendMetricSynchronously(final String metric) {
        transportSender.send(metric);
    }

    private void sendAggregatedMetricSynchronously(final String metric,
                                                   final Aggregation aggregation,
                                                   final AggregationFrequency aggregationFrequency) {
        transportSender.send(metric, buildAggregatedUri(aggregation, aggregationFrequency));
    }

    private String buildAggregatedUri(final Aggregation aggregation, final AggregationFrequency aggregationFrequency) {
        String baseAggregatedUri = ApiUriFactory.buildAggregatedUri(configuration.isSecure(),
                configuration.getHost(), configuration.getPort(), configuration.getPath());

        return baseAggregatedUri
                .replace("{aggregation}", aggregation.getName())
                .replace("{frequency}", Integer.toString(aggregationFrequency.getValue()));
    }

    /**
     * Returns a copy of the representation of the standard buffer as a {@link java.util.List}.
     * <p>
     * This method returns a new copy of the buffer every time it's called. Caution is advised.
     *
     * @return A {@link java.util.List} containing the messages of the buffer
     */
    final List<String> getStandardBuffer() {
        ArrayBlockingQueue<String> buffer = standardBuffer.getBuffer();
        return asList(buffer.toArray(new String[buffer.size()]));
    }

    /**
     * Returns a copy of the representation of all the aggregated buffers as a {@link java.util.List}.
     * <p>
     * This method returns a copies of the buffers every time it's called. Caution is advised.
     *
     * @return A {@link java.util.List} containing the messages of the buffer
     */
    final Map<Aggregation, Map<AggregationFrequency, List<String>>> getAggregatedBuffer() {
        Map<Aggregation, Map<AggregationFrequency, List<String>>> buffersAsList =
                new HashMap<Aggregation, Map<AggregationFrequency, List<String>>>();

        Set<Aggregation> aggregations = aggregatedBuffer.getAggregations();
        for (Aggregation aggregation : aggregations) {
            Set<AggregationFrequency> aggregationFrequencies = aggregatedBuffer.getAggregationFrequencies(aggregation);
            for (AggregationFrequency aggregationFrequency : aggregationFrequencies) {

                // Current buffer content
                ArrayBlockingQueue<String> currentAggregationFreqQueue =
                        aggregatedBuffer.getBuffer().get(aggregation.toString()).get(aggregationFrequency.toString());
                List<String> currentAggregationFreqList =
                        asList(currentAggregationFreqQueue.toArray(new String[currentAggregationFreqQueue.size()]));

                Map<AggregationFrequency, List<String>> aggregationFreqMap = buffersAsList.get(aggregation);
                if (aggregationFreqMap != null) {
                    List<String> aggregationFreqList = aggregationFreqMap.get(aggregationFrequency);

                    if (aggregationFreqList != null) {
                        currentAggregationFreqList.addAll(aggregationFreqList);
                    }
                } else {
                    aggregationFreqMap = new HashMap<AggregationFrequency, List<String>>();
                }

                aggregationFreqMap.put(aggregationFrequency, currentAggregationFreqList);

                buffersAsList.put(aggregation, aggregationFreqMap);
            }
        }

        return buffersAsList;
    }

    private void scheduleTask(final Runnable runnable) {
        boolean wasScheduled = false;
        if (executorService.getQueue().size() < configuration.getMaxWorkerTasksQueueSize()) {
            Future<?> futureTask = executorService.schedule(runnable, 0, TimeUnit.MILLISECONDS);

            wasScheduled = futuresQueue.offer(futureTask);
        }

        if (!wasScheduled) {
            LOGGER.warning("Unable to send metric! The tasks queue is full. Please lower your timeouts or increase the worker thread pool.");
            cancelAllWaitingTasks();
        }
    }

    private void cancelAllWaitingTasks() {
        helperExecutorService.schedule(tasksKiller(), 0, TimeUnit.MILLISECONDS);
    }

    private Runnable tasksKiller() {
        return new Runnable() {
            @Override
            public void run() {
                Collection<Future<?>> tasks = new LinkedList<Future<?>>();
                futuresQueue.drainTo(tasks);

                for (Future<?> task : tasks) {
                    task.cancel(false);
                }

                executorService.purge();
            }
        };
    }
}
