package com.statful.client.core.transport;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFreq;

/**
 * Transport sender interface. A sender is responsible for handle protocol-level communications
 */
public interface TransportSender {

    /**
     * Send messages using the underlying transport protocol.
     *
     * @param message The message to send
     */
    void send(String message);

    /**
     * Send aggregated messages using the underlying transport protocol.
     *
     * @param message The message to send
     * @param aggregation The aggregation applied
     * @param aggregationFreq The aggregation frequency
     * @throws UnsupportedOperationException when not supported by the transport (ex. UDP)
     */
    void sendAggregated(String message,
                        Aggregation aggregation,
                        AggregationFreq aggregationFreq) throws UnsupportedOperationException;

    /**
     * Shutdowns the transport sender, which means typically to release resources, like sockets.
     */
    void shutdown();
}
