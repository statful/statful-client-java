package com.mindera.telemetron.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UDPSender extends AbstractSender implements Sender {

    private String host;
    private int port;
    private InetAddress address;
    private DatagramSocket dsocket;

    public UDPSender(String host, String port) {
        this.host = host;
        this.port = Integer.parseInt(port);
        try {
            this.address = InetAddress.getByName(host);
            this.dsocket = new DatagramSocket();
        } catch (Exception e) {
            throw new TelemetronClientException("Error connecting UDPSender to server with host " + this.host +
                                                " and port " + this.port , e);
        }
    }

    /**
     * Send a message via UDP connection.
     * @param message
     */
    @Override
    public synchronized void send(byte[] message) {
        //TODO detect connection lost
        //TODO implement socket recovery on connection lost

        System.out.println("UDP SENDER : " + this);
        try {
            // Initialize a datagram packet with data and address
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);
            dsocket.send(packet);
        } catch (Exception e) {
            throw new TelemetronClientException("Error sending messages to server", e);
        }
    }
}
