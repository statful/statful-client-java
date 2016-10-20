package com.statful.client.core.transport;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ApiUriFactoryTest {

    @Test
    public void shouldBuildHttpUri() {
        // When
        String uri = ApiUriFactory.buildUri(false, "127.0.0.1", 80);

        assertEquals("http://127.0.0.1:80/tel/v2.0/metrics", uri);
    }

    @Test
    public void shouldBuildHttpAggregatedUri() {
        // When
        String uri = ApiUriFactory.buildAggregatedUri(false, "127.0.0.1", 80);

        assertEquals("http://127.0.0.1:80/tel/v2.0/metrics/aggregation/{aggregation}/frequency/{frequency}", uri);
    }

    @Test
    public void shouldBuildHttpUriWithoutPort() {
        // When
        String uri = ApiUriFactory.buildUri(false, "127.0.0.1", null);

        assertEquals("http://127.0.0.1/tel/v2.0/metrics", uri);
    }

    @Test
    public void shouldBuildHttpsUri() {
        // When
        String uri = ApiUriFactory.buildUri(true, "127.0.0.1", null);

        assertEquals("https://127.0.0.1/tel/v2.0/metrics", uri);
    }

    @Test
    public void shouldBuildHttpsAggregatedUri() {
        // When
        String uri = ApiUriFactory.buildAggregatedUri(true, "127.0.0.1", null);

        assertEquals("https://127.0.0.1/tel/v2.0/metrics/aggregation/{aggregation}/frequency/{frequency}", uri);
    }
}