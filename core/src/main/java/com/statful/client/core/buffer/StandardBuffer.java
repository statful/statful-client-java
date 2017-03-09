package com.statful.client.core.buffer;

import com.statful.client.domain.api.MetricsBuffer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.NoSuchElementException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.logging.Logger;

/**
 * Buffer to store metrics.
 */
public class StandardBuffer implements MetricsBuffer {

    private static final Logger LOGGER = Logger.getLogger(StandardBuffer.class.getName());
    private ArrayBlockingQueue<String> buffer;
    private int maxBufferSize;
    private int flushSize;

    /**
     * Constructor.
     * @param maxBufferSize A {@link Integer} representing the max buffer size
     * @param flushSize A {@link Integer} representing the flush size
     */
    public StandardBuffer(final int maxBufferSize, final int flushSize) {
        this.maxBufferSize = maxBufferSize;
        this.flushSize = flushSize;
        this.buffer = new ArrayBlockingQueue<String>(this.maxBufferSize);
    }

    /**
     * Get the current buffer.
     * @return The {@link ArrayBlockingQueue} current buffer.
     */
    public final ArrayBlockingQueue<String> getBuffer() {
        return buffer;
    }

    /**
     * Adds an aggregated metric to the buffer.
     * @param metric The {@link String} metric name
     * @return A {@link Boolean} with the success of the operation
     */
    public final boolean addToBuffer(final String metric) {
        boolean inserted = buffer.offer(metric);
        if (!inserted) {
            try {
                LOGGER.fine("Buffer is full, trying to discard oldest metric.");
                buffer.remove();
            } catch (NoSuchElementException e) {
                LOGGER.warning("Buffer has been emptied before trying to discard oldest entry.");
            } finally {
                inserted = buffer.offer(metric);
            }
        }

        return inserted;
    }

    /**
     * Reads the buffer contents.
     * @return A {@link String} with all the metrics
     */
    public final String readBuffer() {
        Collection<String> messages = new ArrayList<String>();
        buffer.drainTo(messages, flushSize);

        StringBuilder sb = new StringBuilder();
        for (String metric : messages) {
            sb.append(metric).append("\n");
        }
        return sb.toString();
    }

    @Override
    public final boolean isTimeToFlush() {
        int bufferSize = buffer.size();
        return bufferSize > 0 && flushSize <= bufferSize;
    }
}
