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
     * Send messages to a particular uri using the underlying transport protocol.
     * Might not be implemented in case the underlying transport doesn't support it.
     *
     * @param message The message to send
     * @param uri The uri to send messages to
     */
    void send(String message, String uri);

    /**
     * Shutdowns the transport sender, which means typically to release resources, like sockets.
     */
    void shutdown();
}
