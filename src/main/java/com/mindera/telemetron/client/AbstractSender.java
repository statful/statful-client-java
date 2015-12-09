package com.mindera.telemetron.client;

/**
 * Abstract {@link Sender} implementing common functionality for all senders
 */
public abstract class AbstractSender implements Sender {

    /**
     * Converts the message to an array of bytes and sends the metrics via {@link Sender#send(byte[])}.
     * @param message
     * @throws IllegalArgumentException if the message is null
     */
    public void send(String message) {
        if (message == null) {
            throw new IllegalArgumentException("message is required");
        }
        this.send(message.getBytes());
    }

}
