package com.mindera.telemetron.client.transport;

import java.io.IOException;
import java.net.*;
import java.nio.charset.Charset;
import java.util.logging.Logger;

public class UDPSender implements TransportSender {

    private static final Logger LOGGER = Logger.getLogger(UDPSender.class.getName());

    private final int port;
    private final String host;

    private InetAddress address;
    private DatagramSocket socket;

    public UDPSender(String host, int port) {
        this.port = port;
        this.host = host;

        try {
            createSocket();
        } catch (Exception e) {
            LOGGER.warning("Unable to open UDP socket: " + e.toString());
        }
    }

    @Override
    public void send(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
            LOGGER.warning("I/O exception while sending message. Trying again...");
            retrySendMessage(message);
        }
    }

    private void retrySendMessage(String message) {
        try {
            sendMessage(message);
        } catch (IOException e) {
            LOGGER.warning("Unable to send metric: " + e.toString());
            socket.close();
        }
    }

    private void sendMessage(String message) throws IOException {
        try {
            createSocketIfClosed();
            socket.send(createPacket(message));
        } catch (SocketException e) {
            LOGGER.warning("Unable to open UDP socket: " + e.toString());
        } catch (UnknownHostException e) {
            LOGGER.warning("Unable to open UDP socket: " + e.toString());
        }
    }

    @Override
    public void shutdown() {
        socket.disconnect();
        socket.close();
    }

    private void createSocketIfClosed() throws SocketException, UnknownHostException {
        if (socket == null || socket.isClosed()) {
            createSocket();
        }
    }

    private void createSocket() throws UnknownHostException, SocketException {
        address = InetAddress.getByName(host);
        socket = new DatagramSocket();
    }

    private DatagramPacket createPacket(String message) {
        byte[] byteMessage = message.getBytes(Charset.forName("UTF-8"));
        return new DatagramPacket(byteMessage, byteMessage.length, address, port);
    }

    void setMockedSocket(DatagramSocket socket) {
        this.socket = socket;
    }
}
