package com.statful.client.core.buffer;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.domain.api.MetricsBuffer;

import java.util.*;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Buffer to store aggregated metrics.
 */
public class AggregatedBuffer implements MetricsBuffer {

    private static final Logger LOGGER = Logger.getLogger(AggregatedBuffer.class.getName());
    private Map<String, Map<String, ArrayBlockingQueue<String>>> buffer;
    private int maxBufferSize;
    private int flushSize;

    /**
     * Constructor.
     * @param maxBufferSize A {@link Integer} representing the max buffer size
     * @param flushSize A {@link Integer} representing the flush size
     */
    public AggregatedBuffer(final int maxBufferSize, final int flushSize) {
        this.buffer = new ConcurrentHashMap<String, Map<String, ArrayBlockingQueue<String>>>();
        this.maxBufferSize = maxBufferSize;
        this.flushSize = flushSize;
    }

    /**
     * Get the current buffer.
     * @return The {@link Map} current buffer.
     */
    public final Map<String, Map<String, ArrayBlockingQueue<String>>> getBuffer() {
        return buffer;
    }

    /**
     * Adds an aggregated metric to the buffer.
     * @param metric The {@link String} metric name
     * @param aggregation The {@link com.statful.client.domain.api.Aggregation} aggregation of the metric
     * @param aggregationFrequency The {@link AggregationFrequency} aggregation freq of the metric
     * @return A {@link Boolean} with the success of the operation
     */
    public final boolean addToBuffer(final String metric, final Aggregation aggregation, final AggregationFrequency aggregationFrequency) {
        Map<String, ArrayBlockingQueue<String>> aggregatedBuffer = buffer.get(aggregation.toString());

        ArrayBlockingQueue<String> aggregatedFreqBuffer;

        if (aggregatedBuffer == null) {
            aggregatedBuffer = new ConcurrentHashMap<String, ArrayBlockingQueue<String>>();

            aggregatedFreqBuffer = new ArrayBlockingQueue<String>(this.maxBufferSize);
        } else {
            aggregatedFreqBuffer = aggregatedBuffer.get(aggregationFrequency.toString());

            if (aggregatedFreqBuffer == null) {
                aggregatedFreqBuffer = new ArrayBlockingQueue<String>(this.maxBufferSize);
            }
        }

        aggregatedBuffer.put(aggregationFrequency.toString(), aggregatedFreqBuffer);
        buffer.put(aggregation.toString(), aggregatedBuffer);

        boolean inserted = aggregatedFreqBuffer.offer(metric);
        if (!inserted) {
            try {
                LOGGER.fine("Buffer is full, trying to discard oldest metric.");
                aggregatedFreqBuffer.remove();
            } catch (NoSuchElementException e) {
                LOGGER.warning("Buffer has been emptied before trying to discard oldest entry.");
            } finally {
                inserted = aggregatedFreqBuffer.offer(metric);
            }
        }

        return inserted;
    }

    /**
     * Reads the buffer contents for a particular aggregation.
     * @param aggregation The {@link Aggregation} aggregation to inspect the buffer
     * @param aggregationFrequency The {@link AggregationFrequency} aggregation frequency to inspect the buffer
     * @return A {@link String} with all the metrics for a particular aggregation
     */
    public final String readBuffer(final Aggregation aggregation, final AggregationFrequency aggregationFrequency) {
        Map<String, ArrayBlockingQueue<String>> aggregatedBuffer = buffer.get(aggregation.toString());
        ArrayBlockingQueue<String> aggregatedFreqBuffer;

        if (aggregatedBuffer != null) {
            aggregatedFreqBuffer = aggregatedBuffer.get(aggregationFrequency.toString());

            if (aggregatedFreqBuffer != null) {
                Collection<String> messages = new ArrayList<String>();
                aggregatedFreqBuffer.drainTo(messages, flushSize);

                StringBuilder sb = new StringBuilder();
                for (String metric : messages) {
                    sb.append(metric).append("\n");
                }

                return sb.toString();
            }
        }

        return "";
    }

    /**
     * Return the current aggregations buffers.
     * @return A {@link Set} set with the current aggregations buffers
     */
    public final Set<Aggregation> getAggregations() {
        Set<String> aggregationNames = buffer.keySet();
        Set<Aggregation> aggregations = new HashSet<Aggregation>();

        for (String aggregation : aggregationNames) {
            aggregations.add(Aggregation.valueOf(aggregation));
        }

        return aggregations;
    }

    /**
     * Return the current aggregation frequencies buffers.
     * @param aggregation The {@link Aggregation} aggregation to inspect the buffer
     * @return A {@link Set} set with the current aggregations frequencies buffers
     */
    public final Set<AggregationFrequency> getAggregationFrequencies(final Aggregation aggregation) {
        Map<String, ArrayBlockingQueue<String>> aggregationFreqBuffer = buffer.get(aggregation.toString());

        Set<AggregationFrequency> aggregationFrequencies = new HashSet<AggregationFrequency>();

        if (aggregationFreqBuffer != null) {
            for (String aggregationFreq : aggregationFreqBuffer.keySet()) {
                aggregationFrequencies.add(AggregationFrequency.valueOf(aggregationFreq));
            }
        }

        return aggregationFrequencies;
    }

    @Override
    public final boolean isTimeToFlush() {
        Set<Aggregation> aggregations = getAggregations();

        for (Aggregation aggregation : aggregations) {
            Set<AggregationFrequency> aggregationFrequencies = getAggregationFrequencies(aggregation);

            for (AggregationFrequency aggregationFrequency : aggregationFrequencies) {
                if (isTimeToFlushAggregation(aggregation.toString(), aggregationFrequency.toString())) {
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Validates if the buffer for a particular aggregation is considered ready to be flushed.
     * @param aggregation An {@link Aggregation} aggregation
     * @param aggregationFreq An {@link AggregationFrequency} aggregation frequency
     * @return A {@link Boolean} stating if a particular aggregation buffer should be flushed
     */
    private boolean isTimeToFlushAggregation(final String aggregation, final String aggregationFreq) {
        Map<String, ArrayBlockingQueue<String>> aggregatedBuffer = buffer.get(aggregation);

        if (aggregatedBuffer != null) {
            ArrayBlockingQueue<String> aggregatedFreqBuffer = aggregatedBuffer.get(aggregationFreq);

            if (aggregatedFreqBuffer != null) {
                int bufferSize = aggregatedFreqBuffer.size();

                if (bufferSize > 0 && flushSize <= bufferSize) {
                    return true;
                }
            }
        }

        return false;
    }
}
