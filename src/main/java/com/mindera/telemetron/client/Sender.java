package com.mindera.telemetron.client;

/**
 * Sender interface. A sender is responsible for handle the communication with Telemetron Servers.
 */
public interface Sender {

    void send(String message);
    void send(byte[] message);

}
