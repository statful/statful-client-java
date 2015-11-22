package com.mindera.telemetron.client;

import static org.junit.Assert.*;

/**
 * Created by hugocosta on 16/11/15.
 */
public class TelemetronClientTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    @org.junit.Test
    public void testAddGauge() throws Exception {
        TelemetronClient client = new TelemetronClient();
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
        client.addGauge("price", 12, null);
    }
}