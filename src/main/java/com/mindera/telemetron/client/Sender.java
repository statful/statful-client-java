package com.mindera.telemetron.client;

/**
 * Created by hugocosta on 15/11/15.
 */
public interface Sender {

    void send(String message);
    void send(byte[] message);

}
