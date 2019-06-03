package com.statful.client.domain.api;

import org.junit.Test;

import static org.junit.Assert.*;

public class TransportTest {

    @Test
    public void defaultUdpHost() {
        assertEquals("127.0.0.1", Transport.UDP.getDefaultHost());
    }

    @Test
    public void defaultUdpPort() {
        assertEquals(2013, Transport.UDP.getDefaultPort());
    }

    @Test
    public void defaultHttpHost() {
        assertEquals("api.statful.com", Transport.HTTP.getDefaultHost());
    }

    @Test
    public void defaultHttpPort() {
        assertEquals(443, Transport.HTTP.getDefaultPort());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultOtherHost() {
        Transport.OTHER.getDefaultHost();
    }

    @Test(expected = UnsupportedOperationException.class)
    public void defaultOtherPort() {
        Transport.OTHER.getDefaultPort();
    }
}