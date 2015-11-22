package com.mindera.telemetron.client;

/**
 * Created by hugocosta on 22/11/15.
 */
public class SenderFactory {

    public static Sender getInstance(String transportType, String host, String port) {
        if (TelemetronConfig.TRANSPORT_UDP.equalsIgnoreCase(transportType)) {
            return new UDPSender(host, port);
        } else if (TelemetronConfig.TRANSPORT_TCP.equalsIgnoreCase(transportType)) {
            return new TCPSender(host, port);
        } else if (TelemetronConfig.TRANSPORT_HTTP.equalsIgnoreCase(transportType)) {
            return new HTTPSender(host, port);
        } else {
            throw new IllegalArgumentException("The provided transport type is not valid.");
        }
    }
}
