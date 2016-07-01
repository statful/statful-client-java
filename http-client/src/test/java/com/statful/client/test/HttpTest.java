package com.statful.client.test;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.mockserver.integration.ClientAndServer;

import java.io.IOException;
import java.net.ServerSocket;

public abstract class HttpTest {

    protected static ClientAndServer mockClientAndServer;
    protected static int mockServerPort;

    @BeforeClass
    public static void setUpClass() throws IOException {
        // Try port
        ServerSocket s = new ServerSocket(0);
        mockServerPort = s.getLocalPort();
        s.close();

        mockClientAndServer = ClientAndServer.startClientAndServer(mockServerPort);
    }

    @AfterClass
    public static void tearDownClass() {
        mockClientAndServer.stop();
    }

    @After
    public void tearDown() {
        mockClientAndServer.reset();
    }
}
