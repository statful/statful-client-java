package com.statful.client.transport;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class HTTPUriFactoryTest {

    @Test
    public void shouldBuildHttpUri() {
        // When
        String uri = HTTPUriFactory.buildUri(false, "127.0.0.1", 80);

        assertEquals("http://127.0.0.1:80/tel/v2.0/metrics", uri);
    }

    @Test
    public void shouldBuildHttpUriWithoutPort() {
        // When
        String uri = HTTPUriFactory.buildUri(false, "127.0.0.1", null);

        assertEquals("http://127.0.0.1/tel/v2.0/metrics", uri);
    }

    @Test
    public void shouldBuildHttpsUri() {
        // When
        String uri = HTTPUriFactory.buildUri(true, "127.0.0.1", null);

        assertEquals("https://127.0.0.1/tel/v2.0/metrics", uri);
    }
}