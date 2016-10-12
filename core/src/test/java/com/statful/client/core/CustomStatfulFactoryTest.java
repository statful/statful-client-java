package com.statful.client.core;

import com.statful.client.core.api.StatfulClientBuilder;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.StatfulClient;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static com.statful.client.domain.api.Transport.OTHER;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CustomStatfulFactoryTest {

    @Mock
    private TransportSender transportSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBuildACustomStatfulClientFactory() throws Exception {
        // Given
        StatfulClientBuilder clientBuilder = new MyStatfulClientFactory().buildClient();

        // When
        StatfulClient client = clientBuilder.build();
        client.put("a metric", 1).send();
        client.forceSyncFlush();

        // Then
        verify(transportSender, times(1)).send(anyString());
    }

    private class MyStatfulClientFactory extends CustomStatfulFactory {

        protected MyStatfulClientFactory() {
            super(OTHER);
        }

        @Override
        protected TransportSender buildTransportSender(ClientConfiguration configuration) {
            return transportSender;
        }
    }
}