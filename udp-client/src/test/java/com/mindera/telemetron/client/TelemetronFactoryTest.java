package com.mindera.telemetron.client;

import com.mindera.telemetron.client.api.TelemetronClient;
import com.mindera.telemetron.client.transport.UdpEcho;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class TelemetronFactoryTest {

    @Test
    public void shouldCreateUdpClient() throws Exception {
        // Given
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> response = executorService.submit(new UdpEcho(2013));

        // When
        TelemetronClient client = TelemetronFactory.buildClient("test_prefix").with()
                .flushSize(1)
                .build();
        client.counter("test_counter").send();

        // Then
        String responseString = response.get();
        assertTrue("Should receive message", responseString.startsWith("test_prefix.application.counter.test_counter 1"));
        executorService.shutdown();
    }

    @Test
    public void shouldCreateUdpClientWithoutOptionalConfigurations() throws Exception {
        TelemetronClient client = TelemetronFactory.buildClient("test_prefix").build();

        assertNotNull(client);
    }
}