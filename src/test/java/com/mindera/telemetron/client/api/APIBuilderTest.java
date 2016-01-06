package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.sender.MetricsSender;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class APIBuilderTest {

    @Mock
    private MetricsSender metricsSender;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldIgnoreMetricNameWhenEmpty() {

    }

    @Test
    public void shouldIgnoreMetricNameWhenNull() {

    }

    @Test
    public void shouldIgnoreConfigurationWhenNull() {

    }

    @Test
    public void shouldIgnoreSampleRateWhenNull() {

    }

    @Test
    public void shouldIgnoreAggregationsArrayWhenNull() {

    }

    @Test
    public void shouldIgnoreAggregationsWhenNull() {

    }

    @Test
    public void shouldNotThrowWhenSendingHasException() {

    }
}