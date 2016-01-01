//package com.mindera.telemetron.client;
//
//import java.io.IOException;
//import java.net.Socket;
//
//public class TCPSender extends AbstractSender implements Sender {
//
//    private String host;
//    private int port;
//    private Socket socket;
//
//    public TCPSender(String host, String port) {
//        this.host = host;
//        this.port = Integer.parseInt(port);
//        try {
//            this.socket = new Socket(host, this.port);
//        }
//        catch (IOException e) {
//            throw new TelemetronClientException("Error connecting TCPSender to server with host " + this.host +
//                                                " and port " + this.port , e);
//        }
//    }
//
//    /**
//     * Send a message via TCP connection.
//     * @param message
//     */
//    @Override
//    public synchronized void send(byte[] message) {
//        //TODO detect connection lost
//        //TODO implement socket recovery on connection lost
//
//        try {
//            socket.getOutputStream().write(message);
//            socket.getOutputStream().flush();
//        }
//        catch (IOException e) {
//            throw new TelemetronClientException("Error sending messages to server", e);
//        }
//    }
//
//}
