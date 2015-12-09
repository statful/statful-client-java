package com.mindera.telemetron.client;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Factory class to create {@link Sender} implementations.
 */
public class SenderFactory {

    private static ConcurrentHashMap<String, Sender> senders = new ConcurrentHashMap<String, Sender>();

    /**
     * Obtain {@link Sender} instance according with the transportType.
     * @param transportType Transport type. One of {@link com.mindera.telemetron.client.TelemetronConfig.TRANSPORT_TYPE}
     * @param host host ip to send the data to
     * @param port port number to establish the connection
     * @return sender instance
     */
    public static Sender getInstance(String transportType, String host, String port) {
        if (transportType == null) {
            throw new IllegalArgumentException("transportType is required");
        }

        Sender sender;
        switch (TelemetronConfig.TRANSPORT_TYPE.valueOf(transportType)) {
            case UDP:
                sender = senders.get(TelemetronConfig.TRANSPORT_TYPE.UDP.name());
                if (sender == null) {
                    sender = new UDPSender(host, port);
                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.UDP.name(), sender);
                }
                return sender;
            case TCP:
                sender = senders.get(TelemetronConfig.TRANSPORT_TYPE.TCP.name());
                if (sender == null) {
                    sender = new TCPSender(host, port);
                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.TCP.name(), sender);
                }
                return sender;
            case HTTP:
                sender = senders.get(TelemetronConfig.TRANSPORT_TYPE.HTTP.name());
                if (sender == null) {
                    sender = new HTTPSender(host, port);
                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.HTTP.name(), sender);
                }
                return sender;
            default:
                throw new IllegalArgumentException("transport type not valid.");
        }
    }
}
