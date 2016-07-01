package com.statful.client.core.transport;

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
     * Shutdowns the transport sender, which means typically to release resources, like sockets.
     */
    void shutdown();
}
