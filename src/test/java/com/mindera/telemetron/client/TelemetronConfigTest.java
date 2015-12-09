package com.mindera.telemetron.client;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class TelemetronConfigTest {

    @Test
    public void testIsValid() throws Exception {

    }

    @Test
    public void testSetAppWithValue() throws Exception {
        TelemetronConfig config = new TelemetronConfig();
        String appName = "Test1";
        config.setApp(appName);

        Assert.assertEquals(config.getTags().get("app"), appName);
        Assert.assertEquals(config.getApp(), appName);
    }

    @Test
    public void testSetAppWithNull() throws Exception {
        TelemetronConfig config = new TelemetronConfig();
        config.setApp(null);

        Assert.assertFalse(config.getTags().containsKey("app"));
        Assert.assertNull(config.getApp());
    }

    @Test
    public void testSetAppWithNullAfterValue() throws Exception {
        TelemetronConfig config = new TelemetronConfig();
        config.setApp("Test1");
        config.setApp(null);

        Assert.assertFalse(config.getTags().containsKey("app"));
        Assert.assertNull(config.getApp());
    }
}