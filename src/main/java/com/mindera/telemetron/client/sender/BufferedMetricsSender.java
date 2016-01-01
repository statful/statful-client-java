package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.transport.TransportSender;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static com.mindera.telemetron.client.message.MessageBuilder.newBuilder;
import static java.util.Arrays.asList;

public class BufferedMetricsSender implements MetricsSender {

    private final TransportSender transportSender;
    private final boolean dryRun;
    private final String metricPrefix;
    private final int flushSize;
    private final ArrayBlockingQueue<String> buffer;

    public BufferedMetricsSender(TransportSender transportSender, ClientConfiguration configuration) {
        this.transportSender = transportSender;
        this.dryRun = configuration.isDryRun();
        this.metricPrefix = configuration.getPrefix();
        this.flushSize = configuration.getFlushSize();
        this.buffer = new ArrayBlockingQueue<>(flushSize);

        startFlushInterval(configuration.getFlushIntervalMillis());
    }

    private void startFlushInterval(long flushInterval) {
        if (flushInterval >= 100) {
            ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);
            executorService.scheduleAtFixedRate(this::flush, flushInterval, flushInterval, TimeUnit.MILLISECONDS);
        }
    }

    @Override
    public void put(String name, String value, Tags tags, Aggregations aggregations, Integer aggregationFreq, Integer sampleRate, String namespace, String timestamp) {
        if (shouldPutMetric(sampleRate)) {
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

    private boolean shouldPutMetric(int sampleRate) {
        if (sampleRate < 0) {
            // TODO log
            sampleRate = 0;
        }

        if (sampleRate > 100) {
            // TODO - warn
            sampleRate = 100;
        }
        
        return Math.random() <= (double) sampleRate / 100;
    }

    private void putRaw(String metric) {
        if (isTimeToFlush()) {
            flush();
        }

        boolean inserted = buffer.offer(metric);
        if (!inserted) {
            transportSender.send(metric);
            // TODO - log
        }
    }

    private boolean isTimeToFlush() {
        return !dryRun && flushSize <= buffer.size();
    }

    private void flush() {
        String message = getMessageBuffer();
        if (!message.isEmpty()) {
            transportSender.send(message);
        }
    }

    private String getMessageBuffer() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Collection<String> messages = new ArrayList<>();
        buffer.drainTo(messages);

        StringBuilder sb = new StringBuilder();
        for (String metric : messages) {
            sb.append(metric).append("\n");
        }
        return sb.toString();
    }

    List<String> getBuffer() {
        return asList(buffer.toArray(new String[buffer.size()]));
    }
}
