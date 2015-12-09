package com.mindera.telemetron.client;

import static org.junit.Assert.*;

public class TelemetronClientTest {

    @org.junit.Before
    public void setUp() throws Exception {

    }

    private TelemetronClient createSimpleClient() {
        TelemetronConfig config = new TelemetronConfig();
        config.setPrefix("TEST_PREFIX");
        return new TelemetronClient(config);
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddTimerWithoutName() throws Exception {
        TelemetronClient client = createSimpleClient();
        client.addTimer(null, 12, new TimerConfig());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddCounterWithoutName() throws Exception {
        TelemetronClient client = createSimpleClient();
        client.addCounter(null, 12, new CounterConfig());
    }

    @org.junit.Test(expected = IllegalArgumentException.class)
    public void testAddGaugeWithoutName() throws Exception {
        TelemetronClient client = createSimpleClient();
        client.addGauge(null, 12, new GaugeConfig());
    }
}