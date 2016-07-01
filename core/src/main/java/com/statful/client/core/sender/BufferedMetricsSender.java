package com.statful.client.core.sender;

import com.statful.client.core.message.MessageBuilder;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.AggregationFreq;
import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.MetricsSender;
import com.statful.client.domain.api.Tags;
import com.statful.client.domain.api.ClientConfiguration;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
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

    private final TransportSender transportSender;
    private final ScheduledExecutorService executorService;
    private final boolean dryRun;
    private final String metricPrefix;
    private final int flushSize;
    private final ArrayBlockingQueue<String> buffer;

    /**
     * Default constructor.
     *
     * @param transportSender The {@link com.statful.client.core.transport.TransportSender} to send metrics
     * @param configuration The {@link com.statful.client.domain.api.ClientConfiguration}
     * @param executorService The {@link java.util.concurrent.ScheduledExecutorService} to handle flushes
     */
    public BufferedMetricsSender(
            final TransportSender transportSender,
            final ClientConfiguration configuration,
            final ScheduledExecutorService executorService
    ) {
        this.transportSender = transportSender;
        this.executorService = executorService;
        this.dryRun = configuration.isDryRun();
        this.metricPrefix = configuration.getPrefix();
        this.flushSize = configuration.getFlushSize();
        this.buffer = new ArrayBlockingQueue<String>(MAX_BUFFER_SIZE);

        startFlushInterval(configuration.getFlushIntervalMillis());
    }

    private void startFlushInterval(final long flushInterval) {
        if (flushInterval >= MIN_FLUSH_INTERVAL) {
            executorService.scheduleAtFixedRate(flusher(), flushInterval, flushInterval, TimeUnit.MILLISECONDS);
        }
    }

    private Runnable flusher() {
        return new Runnable() {
            @Override
            public void run() {
                flush();
            }
        };
    }

    @Override
     public final void put(
            final String name, final String value, final Tags tags, final Aggregations aggregations,
            final AggregationFreq aggregationFreq, final Integer sampleRate, final String namespace,
            final long timestamp
    ) {
        if (shouldPutMetric(sampleRate)) {
            String rawMessage = MessageBuilder.newBuilder()
                    .withPrefix(metricPrefix)
                    .withName(name)
                    .withValue(value)
                    .withTags(tags)
                    .withAggregations(aggregations)
                    .withAggregationFreq(aggregationFreq)
                    .withNamespace(namespace)
                    .withTimestamp(timestamp)
                    .build();

            if (!dryRun) {
                this.putRaw(rawMessage);
            } else {
                LOGGER.info("Dry metric: " + rawMessage);
            }
        }
    }

    @Override
    public final void shutdown() {
        transportSender.shutdown();
    }

    private boolean shouldPutMetric(final int sampleRate) {
        int newSampleRate = sanitizeSampleRate(sampleRate);
        return Math.random() <= (double) newSampleRate / SAMPLE_RATE_DIVIDER;
    }

    private int sanitizeSampleRate(final int sampleRate) {
        int newSampleRate = MAX_SAMPLE_RATE;
        if (sampleRate < MIN_SAMPLE_RATE) {
            newSampleRate = MIN_SAMPLE_RATE;
            LOGGER.warning("The configured sample rate is bellow 1, assuming 1.");
        } else if (sampleRate > MAX_SAMPLE_RATE) {
            newSampleRate = MAX_SAMPLE_RATE;
            LOGGER.warning("The configured sample rate is above 100, assuming 100.");
        }

        return newSampleRate;
    }

    private void putRaw(final String metric) {

        boolean inserted = buffer.offer(metric);
        if (!inserted) {
            LOGGER.warning("The buffer is full, metric ignored!.");
        }
        if (isTimeToFlush()) {
            flush();
        }
    }

    private boolean isTimeToFlush() {
        int bufferSize = buffer.size();
        return bufferSize > 0 && flushSize <= bufferSize;
    }

    private void flush() {
        String message = getMessageBuffer();
        if (!message.isEmpty()) {
            sendMetric(message);
        }
    }

    private void sendMetric(final String metric) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                transportSender.send(metric);
            }
        });
    }

    private String getMessageBuffer() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Collection<String> messages = new ArrayList<String>();
        buffer.drainTo(messages, flushSize);

        StringBuilder sb = new StringBuilder();
        for (String metric : messages) {
            sb.append(metric).append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns a copy of the representation of the buffer as a {@link java.util.List}.
     * <p>
     * This method returns a new copy of the buffer every time it's called. Caution is advised.
     *
     * @return A {@link java.util.List} containing the messages of the buffer
     */
    final List<String> getBuffer() {
        return asList(buffer.toArray(new String[buffer.size()]));
    }
}
