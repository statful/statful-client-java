package com.mindera.telemetron.client;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * HTTP Sender<br/>
 * If the TelemetronClient is configured to use secure communications, this class uses HTTPS.
 */
public class HTTPSender extends AbstractSender implements Sender {

    private String host;
    private int port;

    public HTTPSender(String host, String port) {
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    @Override
    public void send(byte[] message) {
        //TODO add implementation
        throw new UnsupportedOperationException("not yet... we are working on that!!");
    }

}
