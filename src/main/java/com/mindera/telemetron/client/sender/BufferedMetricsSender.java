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
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import static com.mindera.telemetron.client.message.MessageBuilder.newBuilder;
import static java.util.Arrays.asList;

public class BufferedMetricsSender implements MetricsSender {

    private static Logger LOGGER = Logger.getLogger(BufferedMetricsSender.class.getName());

    private final TransportSender transportSender;
    private final boolean dryRun;
    private final String metricPrefix;
    private final int flushSize;
    private final ArrayBlockingQueue<String> buffer;

    private ScheduledExecutorService executorService;

    public BufferedMetricsSender(TransportSender transportSender, ClientConfiguration configuration) {
        this.transportSender = transportSender;
        this.dryRun = configuration.isDryRun();
        this.metricPrefix = configuration.getPrefix();
        this.flushSize = configuration.getFlushSize();
        this.buffer = new ArrayBlockingQueue<String>(flushSize);

        startFlushInterval(configuration.getFlushIntervalMillis());
    }

    private void startFlushInterval(long flushInterval) {
        if (flushInterval >= 100) {
            executorService = Executors.newScheduledThreadPool(1);
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
    public void put(String name, String value, Tags tags, Aggregations aggregations, AggregationFreq aggregationFreq, Integer sampleRate, String namespace, String timestamp) {
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
    public void shutdown() {
        transportSender.shutdown();
        if (executorService != null) {
            executorService.shutdown();
        }
    }

    private boolean shouldPutMetric(int sampleRate) {
        sampleRate = sanitizeSampleRate(sampleRate);
        
        return Math.random() <= (double) sampleRate / 100;
    }

    private int sanitizeSampleRate(int sampleRate) {
        if (sampleRate < 1) {
            sampleRate = 1;
            LOGGER.warning("The configured sample rate is bellow 1, assuming 1.");
        } else if (sampleRate > 100) {
            sampleRate = 100;
            LOGGER.warning("The configured sample rate is above 100, assuming 100.");
        }

        return sampleRate;
    }

    private void putRaw(String metric) {
        if (isTimeToFlush()) {
            flush();
        }

        boolean inserted = buffer.offer(metric);
        if (!inserted) {
            LOGGER.warning("The buffer is full, sending metric to Telemetron now.");
            sendMetric(metric);
        }
    }

    private boolean isTimeToFlush() {
        return  flushSize <= buffer.size();
    }

    private void flush() {
        String message = getMessageBuffer();
        if (!message.isEmpty()) {
            sendMetric(message);
        }
    }

    private void sendMetric(String metric) {
        transportSender.send(metric);
    }

    private String getMessageBuffer() {
        @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
        Collection<String> messages = new ArrayList<String>();
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
