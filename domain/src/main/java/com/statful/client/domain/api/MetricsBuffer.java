package com.statful.client.domain.api;

/**
 * Interface that represents a metrics buffer.
 */
public interface MetricsBuffer {

    /**
     * Validates if the buffer is considered ready to be flushed.
     * @return A {@link Boolean} stating if the buffer should be flushed.
     */
    boolean isTimeToFlush();
}
