package com.statful.client.core.buffer;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

import static org.junit.Assert.*;

public class AggregatedBufferTest {

    private AggregatedBuffer subject;

    @Before
    public void before() {
        subject = new AggregatedBuffer(5, 2);
    }

    @Test
    public void shouldAddToBuffer() {
        // When
        assertTrue("Should add to buffer", subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10));

        // Then
        assertEquals("Buffer should have 1 metric", 1, subject.getBuffer().size());
    }

    @Test
    public void shouldAddToExistingBuffer() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);

        // When
        assertTrue("Should add to buffer", subject.addToBuffer("bar", Aggregation.AVG, AggregationFrequency.FREQ_10));

        // Then
        assertEquals("Buffer should have 2 metric", 2, subject.getBuffer()
                .get(Aggregation.AVG.toString()).get(AggregationFrequency.FREQ_10.toString()).size());
    }

    @Test
    public void shouldReadBuffer() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);

        // When
        String bufferString = subject.readBuffer(Aggregation.AVG, AggregationFrequency.FREQ_10);

        // Then
        assertEquals("foo", bufferString);
    }

    @Test
    public void shouldReturnEmptyStringWhenBufferIsEmptyOrMissing() {
        // When
        String bufferString = subject.readBuffer(Aggregation.AVG, AggregationFrequency.FREQ_10);

        // Then
        assertTrue("Buffer string should be empty", bufferString.isEmpty());
    }

    @Test
    public void shouldReturnTrueIfIsTimeToFlush() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);

        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertTrue("Should set as time to flush", isTimeToFlush);
    }

    @Test
    public void shouldReturnFalseIfIsNotTimeToFlush() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);

        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertFalse("Should not set as time to flush", isTimeToFlush);
    }

    @Test
    public void shouldReturnFalseIfIsNotTimeToFlushBecauseTheBufferIsEmptyOrMissing() {
        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertFalse("Should not set as time to flush", isTimeToFlush);
    }

    @Test
    public void shouldReturnAggregationsSet() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);
        subject.addToBuffer("foo", Aggregation.COUNT, AggregationFrequency.FREQ_10);

        // When
        Set<Aggregation> aggregations = subject.getAggregations();

        // Then
        assertEquals("Aggregations set should have 2 entries", 2, aggregations.size());
    }

    @Test
    public void shouldReturnEmptyAggregationsSet() {
        // When
        Set<Aggregation> aggregations = subject.getAggregations();

        // Then
        assertTrue("Aggregations set should be empty", aggregations.isEmpty());
    }

    @Test
    public void shouldReturnAggregationFrequenciesSet() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_120);

        // When
        Set<AggregationFrequency> aggregationFrequencies = subject.getAggregationFrequencies(Aggregation.AVG);

        // Then
        assertEquals("Aggregation frequencies set should have 2 entries", 2, aggregationFrequencies.size());
    }

    @Test
    public void shouldReturnEmptyAggregationFrequenciesSet() {
        // Given
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_10);
        subject.addToBuffer("foo", Aggregation.AVG, AggregationFrequency.FREQ_120);

        // When
        Set<AggregationFrequency> aggregationFrequencies = subject.getAggregationFrequencies(Aggregation.COUNT);

        // Then
        assertTrue("Aggregations frequencies set should be empty", aggregationFrequencies.isEmpty());
    }
}
