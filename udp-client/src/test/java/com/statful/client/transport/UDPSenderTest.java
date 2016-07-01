package com.statful.client.transport;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.*;

public class UDPSenderTest {

    private static ExecutorService executorService;

    @BeforeClass
    public static void setUp() {
        executorService = Executors.newFixedThreadPool(1);
    }

    @AfterClass
    public static void tearDown() {
        executorService.shutdownNow();
    }

    @Test
    public void shouldInstantiateIfSocketCreationFails() {
        UDPSender subject = new UDPSender(null, 1000);

        assertNotNull("UDP sender should always instantiate", subject);
    }

    @Test
    public void shouldSendUDPMessage() throws Exception {
        Future<String> response = executorService.submit(new UdpEcho(2015));

        UDPSender subject = new UDPSender("127.0.0.1", 2015);
        subject.send("Hello world");

        assertEquals("Should send UDP message", "Hello world", response.get());
    }

    @Test
    public void shouldRecoverFromClosedSocket() throws Exception {
        Future<String> response = executorService.submit(new UdpEcho(2015));

        UDPSender subject = new UDPSender("127.0.0.1", 2015);
        subject.shutdown();

        subject.send("Hello world");
        assertEquals("Should send UDP message", "Hello world", response.get());
    }

    @Test
    public void shouldNotThrowExceptionWhileSendingToUnknownHost() {
        UDPSender subject = new UDPSender("batatas", 2013);
        subject.send("Hello world");
    }

    @Test
    public void shouldNotThrowExceptionIfSocketWasNotOpened() throws Exception {
        // Given
        DatagramSocket socket = mock(DatagramSocket.class);
        doThrow(new SocketException()).when(socket).send(any(DatagramPacket.class));

        UDPSender subject = new UDPSender("127.0.0.1", 2015);
        subject.setSocket(socket);

        // When
        subject.send("Hello world");
    }

    @Test
    public void shouldResendAfterError() throws Exception {
        Future<String> response = executorService.submit(new UdpEcho(2015));

        // Given
        DatagramSocket socket = mock(DatagramSocket.class);
        when(socket.isClosed()).thenReturn(true);

        doThrow(new IOException()).when(socket).send(any(DatagramPacket.class));

        UDPSender subject = new UDPSender("127.0.0.1", 2015);
        subject.setSocket(socket);

        // When
        subject.send("Hello world");

        // Then
        assertEquals("Should send UDP message", "Hello world", response.get());
    }

    @Test
    public void shouldNotThrowAfterIOException() throws Exception {
        // Given
        DatagramSocket socket = mock(DatagramSocket.class);
        doThrow(new IOException()).when(socket).send(any(DatagramPacket.class));

        UDPSender subject = new UDPSender("127.0.0.1", 2015);
        subject.setSocket(socket);

        // When
        subject.send("Hello world");
    }
}