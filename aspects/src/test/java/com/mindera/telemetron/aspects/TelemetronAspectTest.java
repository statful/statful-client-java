package com.mindera.telemetron.aspects;

import com.mindera.telemetron.annotations.Timer;
import com.mindera.telemetron.client.api.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.*;
import static org.mockito.MockitoAnnotations.initMocks;

public class TelemetronAspectTest {

    @Mock
    private Timer timer;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SenderAPI telemetronSenderAPI;

    @Mock
    private SenderFacade telemetronSenderFacade;

    @Mock
    private TelemetronClient telemetronClient;

    private TelemetronAspect subject;

    @Before
    public void setUp() {
        initMocks(this);

        MethodSignature signature = mock(MethodSignature.class);
        when(signature.getMethod()).thenReturn(this.getClass().getDeclaredMethods()[0]);
        when(joinPoint.getSignature()).thenReturn(signature);

        when(timer.name()).thenReturn("timerName");
        when(timer.namespace()).thenReturn("namespace");
        when(timer.aggregations()).thenReturn(new Aggregation[] {});
        when(timer.tags()).thenReturn(new String[] {});

        when(telemetronSenderFacade.with()).thenReturn(telemetronSenderAPI);
        when(telemetronSenderAPI.tags(any(Tags.class))).thenReturn(telemetronSenderAPI);
        when(telemetronSenderAPI.aggregations(any(Aggregations.class))).thenReturn(telemetronSenderAPI);
        when(telemetronSenderAPI.namespace(anyString())).thenReturn(telemetronSenderAPI);
        when(telemetronClient.timer(anyString(), anyLong())).thenReturn(telemetronSenderFacade);

        subject = new TelemetronAspect();
        subject.setTelemetronClient(telemetronClient);
    }

    @Test
    public void shouldSendMetricOnTimerAnnotationSuccessfully() throws Throwable {
        // Given
        when(joinPoint.proceed()).thenReturn("something");

        // When
        subject.methodTiming(joinPoint, timer);

        // Then
        ArgumentCaptor<Tags> tagsCaptor = ArgumentCaptor.forClass(Tags.class);
        verify(telemetronSenderAPI).tags(tagsCaptor.capture());
        assertEquals("success", tagsCaptor.getValue().getTagValue("status"));

        verify(telemetronClient).timer(eq("timerName"), anyLong());
        verify(telemetronSenderAPI).send();
    }

    @Test
    public void shouldSendMetricOnTimerAnnotationOnError() throws Throwable {
        // Given
        when(joinPoint.proceed()).thenThrow(new NullPointerException());

        // When
        try {
            subject.methodTiming(joinPoint, timer);
        } catch (Exception e) { }

        // Then
        ArgumentCaptor<Tags> tagsCaptor = ArgumentCaptor.forClass(Tags.class);
        verify(telemetronSenderAPI).tags(tagsCaptor.capture());
        assertEquals("error", tagsCaptor.getValue().getTagValue("status"));

        verify(telemetronClient).timer(eq("timerName"), anyLong());
        verify(telemetronSenderAPI).send();
    }

    @Test
    public void shouldSendMetricOnTimerAnnotationWithEmptyNamespace() throws Throwable {
        // Given
        when(joinPoint.proceed()).thenReturn("something");
        when(timer.namespace()).thenReturn("");

        // When
        subject.methodTiming(joinPoint, timer);

        // Then
        ArgumentCaptor<String> namespaceCaptor = ArgumentCaptor.forClass(String.class);
        verify(telemetronSenderAPI).namespace(namespaceCaptor.capture());
        assertNull(namespaceCaptor.getValue());

        verify(telemetronClient).timer(eq("timerName"), anyLong());
        verify(telemetronSenderAPI).send();
    }
}