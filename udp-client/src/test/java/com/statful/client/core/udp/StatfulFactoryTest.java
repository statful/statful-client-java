package com.statful.client.core.udp;

import com.statful.client.domain.api.StatfulClient;
import com.statful.client.transport.UdpEcho;
import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class StatfulFactoryTest {

    @Test
    public void shouldCreateUDPClient() throws Exception {
        // Given
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        Future<String> response = executorService.submit(new UdpEcho(2013));

        // When
        StatfulClient client = StatfulFactory.buildUDPClient().with()
                .host("localhost")
                .port(2013)
                .flushSize(1)
                .build();
        client.counter("test_counter").send();

        // Then
        String responseString = response.get();
        assertTrue("Should receive message", responseString.startsWith("application.counter.test_counter 1"));
        executorService.shutdown();
    }

    @Test
    public void shouldCreateUDPClientWithoutOptionalConfigurations() {
        StatfulClient client = StatfulFactory.buildUDPClient()
                .with()
                .host("localhost")
                .port(2013)
                .build();

        assertNotNull(client);
    }
}