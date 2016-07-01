package com.statful.client.transport;

import java.net.*;
import java.util.concurrent.Callable;

public class UdpEcho implements Callable<String> {
    private final DatagramSocket socket;

    public UdpEcho(int port) throws SocketException, UnknownHostException {
        this.socket = new DatagramSocket(port, InetAddress.getByName("127.0.0.1"));
    }

    @Override
    public String call() throws Exception {
        try {
            byte[] buffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(buffer, buffer.length);
            socket.receive(receivePacket);
            return new String(receivePacket.getData()).trim();
        } finally {
            socket.disconnect();
            socket.close();
        }
    }
}
