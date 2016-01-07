package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.api.AggregationFreq;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.transport.TransportSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.mindera.telemetron.client.message.MessageBuilder.newBuilder;
import static java.util.Arrays.asList;

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
        if (!dryRun && shouldPutMetric(sampleRate)) {
            String rawMessage = newBuilder()
                    .withPrefix(metricPrefix)
                    .withName(name)
                    .withValue(value)
                    .withTags(tags)
                    .withAggregations(aggregations)
                    .withAggregationFreq(aggregationFreq)
                    .withNamespace(namespace)
                    .withTimestamp(timestamp)
                    .build();

            this.putRaw(rawMessage);
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
            LOGGER.warning("The buffer is full, sending metric to Telemetron now.");
            sendMetric(metric);
        }
        if (isTimeToFlush()) {
            flush();
        }
    }

    private boolean isTimeToFlush() {
        return flushSize <= buffer.size();
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

    final List<String> getBuffer() {
        return asList(buffer.toArray(new String[buffer.size()]));
    }
}
