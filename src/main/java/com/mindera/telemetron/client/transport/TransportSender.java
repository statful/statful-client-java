package com.mindera.telemetron.client.transport;

/**
 * Sender interface. A sender is responsible for handle the communication with Telemetron Servers.
 */
public interface TransportSender {
    void send(String message);
}
