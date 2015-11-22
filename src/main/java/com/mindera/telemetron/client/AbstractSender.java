package com.mindera.telemetron.client;

/**
 * Created by hugocosta on 22/11/15.
 */
public abstract class AbstractSender implements Sender {

    /**
     * Converts the message to an array of bytes and sends the metrics via {@link Sender#send(byte[])}.
     * @param message
     */
    public void send(String message) {
        //TODO validate params
        this.send(message.getBytes());
    }

}
