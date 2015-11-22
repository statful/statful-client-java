package com.mindera.telemetron.client;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Created by hugocosta on 15/11/15.
 */
public class UDPSender extends AbstractSender implements Sender {

    private String host;
    private int port;

    public UDPSender(String host, String port) {
        //TODO validate params
        this.host = host;
        this.port = Integer.parseInt(port);
    }

    /**
     * Send a message via UDP connection.
     * @param message
     */
    public void send(byte[] message) {
        //TODO validate params
        try {
            // Get the internet address of the specified host
            InetAddress address = InetAddress.getByName(host);

            // Initialize a datagram packet with data and address
            DatagramPacket packet = new DatagramPacket(message, message.length, address, port);

            // Create a datagram socket, send the packet through it, close it.
            DatagramSocket dsocket = new DatagramSocket();
            dsocket.send(packet);
            dsocket.close();
        } catch (Exception e) {
            //TODO Properly handle this exception
            System.err.println(e);
        }
    }
}
