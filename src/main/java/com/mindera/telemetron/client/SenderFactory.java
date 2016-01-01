//package com.mindera.telemetron.client;
//
//import com.mindera.telemetron.client.transport.TransportSender;
//
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * Factory class to create {@link com.mindera.telemetron.client.transport.TransportSender} implementations.
// */
//public class SenderFactory {
//
//    private static ConcurrentHashMap<String, TransportSender> senders = new ConcurrentHashMap<String, TransportSender>();
//
//    /**
//     * Obtain {@link com.mindera.telemetron.client.transport.TransportSender} instance according with the transportType.
//     * @param transportType Transport type. One of {@link com.mindera.telemetron.client.TelemetronConfig.TRANSPORT_TYPE}
//     * @param host host ip to send the data to
//     * @param port port number to establish the connection
//     * @return sender instance
//     */
//    public static TransportSender getInstance(String transportType, String host, String port) {
//        if (transportType == null) {
//            throw new IllegalArgumentException("transportType is required");
//        }
//
//        TransportSender transportSender;
//        switch (TelemetronConfig.TRANSPORT_TYPE.valueOf(transportType)) {
//            case UDP:
//                transportSender = senders.get(TelemetronConfig.TRANSPORT_TYPE.UDP.name());
//                if (transportSender == null) {
//                    transportSender = new UDPSender(host, port);
//                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.UDP.name(), transportSender);
//                }
//                return transportSender;
//            case TCP:
//                transportSender = senders.get(TelemetronConfig.TRANSPORT_TYPE.TCP.name());
//                if (transportSender == null) {
//                    transportSender = new TCPSender(host, port);
//                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.TCP.name(), transportSender);
//                }
//                return transportSender;
//            case HTTP:
//                transportSender = senders.get(TelemetronConfig.TRANSPORT_TYPE.HTTP.name());
//                if (transportSender == null) {
//                    transportSender = new HTTPSender(host, port);
//                    senders.putIfAbsent(TelemetronConfig.TRANSPORT_TYPE.HTTP.name(), transportSender);
//                }
//                return transportSender;
//            default:
//                throw new IllegalArgumentException("transport type not valid.");
//        }
//    }
//}
