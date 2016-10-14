package com.statful.client.core.buffer;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class StandardBufferTest {

    private StandardBuffer subject;

    @Before
    public void before() {
        subject = new StandardBuffer(5, 2);
    }

    @Test
    public void shouldAddToBuffer() {
        // When
        assertTrue("Should add to buffer", subject.addToBuffer("foo"));

        // Then
        assertEquals("Buffer should have 1 metric", 1, subject.getBuffer().size());
    }

    @Test
    public void shouldAddToExistingBuffer() {
        // Given
        subject.addToBuffer("foo");

        // When
        assertTrue("Should add to buffer", subject.addToBuffer("bar"));

        // Then
        assertEquals("Buffer should have 2 metric", 2, subject.getBuffer().size());
    }

    @Test
    public void shouldReadBuffer() {
        // Given
        subject.addToBuffer("foo");

        // When
        String bufferString = subject.readBuffer();

        // Then
        assertEquals("foo\n", bufferString);
    }

    @Test
    public void shouldReturnEmptyStringWhenBufferIsEmptyOrMissing() {
        // When
        String bufferString = subject.readBuffer();

        // Then
        assertTrue("Buffer string should be empty", bufferString.isEmpty());
    }

    @Test
    public void shouldReturnTrueIfIsTimeToFlush() {
        // Given
        subject.addToBuffer("foo");
        subject.addToBuffer("foo");
        subject.addToBuffer("foo");

        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertTrue("Should set as time to flush", isTimeToFlush);
    }

    @Test
    public void shouldReturnFalseIfIsNotTimeToFlush() {
        // Given
        subject.addToBuffer("foo");

        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertFalse("Should not set as time to flush", isTimeToFlush);
    }

    @Test
    public void shouldReturnFalseIfIsNotTimeToFlushBecauseBufferIsEmpty() {
        // When
        boolean isTimeToFlush = subject.isTimeToFlush();

        // Then
        assertFalse("Should not set as time to flush", isTimeToFlush);
    }
}
