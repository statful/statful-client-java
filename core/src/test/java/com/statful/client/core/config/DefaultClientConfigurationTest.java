package com.statful.client.core.config;

import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.Tags;
import com.statful.client.domain.api.Transport;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static com.statful.client.domain.api.Aggregation.*;
import static com.statful.client.domain.api.AggregationFrequency.FREQ_10;
import static com.statful.client.domain.api.AggregationFrequency.FREQ_120;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class DefaultClientConfigurationTest {

    private DefaultClientConfiguration subject;

    @Before
    public void setUp() {
        subject = new DefaultClientConfiguration();
    }

    @Test
    public void shouldValidate() {
        // Given
        subject.setTransport(Transport.UDP);

        // When
        boolean result = subject.isValid();

        // Then
        assertTrue("Should be valid", result);
    }

    @Test
    public void shouldInvalidateWithNoTransport() {
        // When
        boolean result = subject.isValid();

        // Then
        assertFalse("Should be invalid", result);
    }

    @Test
    public void shouldInvalidate() {
        // When
        boolean result = subject.isValid();

        // Then
        assertFalse("Should be invalid", result);
    }

    @Test
    public void shouldMergeApplicationTags() {
        subject.mergeApplicationTag("host", "localhost");
        subject.mergeApplicationTag("cluster", "production");
        
        Tags tags = subject.getApplicationTags();
        assertEquals("Should have merged tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should have merged tag", "production", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldGetSampleRate() {
        subject.setSampleRate(100);
        assertEquals("Should get sample rate", 100, subject.getSampleRate());
    }

    @Test
    public void shouldGetNamespace() {
        subject.setNamespace("test_namespace");
        assertEquals("Should get namespace", "test_namespace", subject.getNamespace());
    }

    @Test
    public void shouldGetFlushSize() {
        subject.setFlushSize(100);
        assertEquals("Should get flush size", 100, subject.getFlushSize());
    }

    @Test
    public void shouldGetFlushIntervalMillis() {
        subject.setFlushIntervalMillis(100);
        assertEquals("Should get flush interval", 100, subject.getFlushIntervalMillis());
    }

    @Test
    public void shouldGetIsDryRun() {
        subject.setDryRun(false);
        assertFalse("Should get dry run", subject.isDryRun());
    }

    @Test
    public void shouldGetHost() {
        subject.setHost("test_host");
        assertEquals("Should get host", "test_host", subject.getHost());
    }

    @Test
    public void shouldGetPort() {
        subject.setPort(2013);
        assertEquals("Should get port", 2013, subject.getPort());
    }

    @Test
    public void shouldGetPath() {
        subject.setPath("/path");
        assertEquals("Should get path", "/path", subject.getPath());
    }

    @Test
    public void shouldGetTransport() {
        subject.setTransport(Transport.UDP);
        Assert.assertEquals("Should get transport", Transport.UDP, subject.getTransport());
    }

    @Test
    public void shouldGetToken() {
        subject.setToken("test_token");
        assertEquals("Should get token", "test_token", subject.getToken());
    }

    @Test
    public void shouldGetApp() {
        subject.setApp("test_app");
        assertEquals("Should get app", "test_app", subject.getApp());
    }

    @Test
    public void shouldMergeTimerTag() {
        subject.mergeTimerTag("host", "localhost");
        subject.mergeApplicationTag("cluster", "production");

        Tags tags = subject.getTimerTags();
        assertEquals("Should merge timer tags", "localhost", tags.getTagValue("host"));
        assertEquals("Should merge timer tags", "production", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeTimerAggregation() {
        subject.mergeTimerAggregation(LAST);

        Aggregations aggregations = subject.getTimerAggregations();
        assertThat("Should merge timer aggregations", aggregations.getAggregations(), containsInAnyOrder(AVG, P90, COUNT, LAST));
    }

    @Test
    public void shouldGetTimerAggregationFreq() {
        subject.setTimerAggregationFrequency(FREQ_120);
        assertEquals("Should get timer aggregation frequency", FREQ_120, subject.getTimerAggregationFrequency());
    }

    @Test
    public void shouldMergeCounterTags() {
        subject.mergeCounterTag("host", "localhost");
        subject.mergeApplicationTag("cluster", "production");

        Tags tags = subject.getCounterTags();
        assertEquals("Should merge counter tags", "localhost", tags.getTagValue("host"));
        assertEquals("Should merge counter tags", "production", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeCounterAggregations() {
        subject.mergeCounterAggregation(LAST);

        Aggregations aggregations = subject.getCounterAggregations();
        assertThat("Should merge counter aggregations", aggregations.getAggregations(), containsInAnyOrder(COUNT, SUM, LAST));
    }

    @Test
    public void shouldGetCounterAggregationFreq() {
        subject.setCounterAggregationFrequency(FREQ_120);
        assertEquals("Should get counter aggregation frequency", FREQ_120, subject.getCounterAggregationFrequency());
    }

    @Test
    public void shouldMergeGaugeTags() {
        subject.mergeGaugeTag("host", "localhost");
        subject.mergeApplicationTag("cluster", "production");

        Tags tags = subject.getGaugeTags();
        assertEquals("Should merge gauge tags", "localhost", tags.getTagValue("host"));
        assertEquals("Should merge gauge tags", "production", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeGaugeAggregations() {
        subject.mergeGaugeAggregation(AVG);

        Aggregations aggregations = subject.getGaugeAggregations();
        assertThat("Should merge gauge aggregations", aggregations.getAggregations(), containsInAnyOrder(AVG, LAST));
    }

    @Test
    public void shouldGetGaugeAggregationFreq() {
        subject.setGaugeAggregationFrequency(FREQ_120);
        assertEquals("Should get gauge aggregation frequency", FREQ_120, subject.getGaugeAggregationFrequency());
    }

    @Test
    public void shouldHaveDefaultConfigurationValues() {

        assertEquals("127.0.0.1", subject.getHost());
        assertEquals(2013, subject.getPort());
        assertEquals(100, subject.getSampleRate());
        assertEquals("/tel/v2.0/metrics", subject.getPath());
        assertEquals("application", subject.getNamespace());
        assertEquals(10, subject.getFlushSize());
        assertEquals(5000, subject.getFlushIntervalMillis());
        assertFalse(subject.isDryRun());

        assertEquals(FREQ_10, subject.getTimerAggregationFrequency());
        assertEquals(FREQ_10, subject.getCounterAggregationFrequency());
        assertEquals(FREQ_10, subject.getGaugeAggregationFrequency());

        assertNull(subject.getTransport());
        assertNull(subject.getToken());
        assertNull(subject.getApp());
        assertNull(subject.getCounterTags());
        assertNull(subject.getGaugeTags());

        Assert.assertEquals("java", subject.getApplicationTags().getTagValue("statful_client"));
        Assert.assertEquals("ms", subject.getTimerTags().getTagValue("unit"));
        assertThat(subject.getTimerAggregations().getAggregations(), containsInAnyOrder(AVG, P90, COUNT));
        assertThat(subject.getCounterAggregations().getAggregations(), containsInAnyOrder(COUNT, SUM));
        assertThat(subject.getGaugeAggregations().getAggregations(), containsInAnyOrder(LAST));
    }
}