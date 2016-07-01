package com.statful.client.aspects;

import com.statful.client.annotations.Timer;
import com.statful.client.domain.api.*;
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

public class StatfulAspectTest {

    @Mock
    private Timer timer;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private SenderAPI statfulSenderAPI;

    @Mock
    private SenderFacade statfulSenderFacade;

    @Mock
    private StatfulClient statfulClient;

    private StatfulAspect subject;

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

        when(statfulSenderFacade.with()).thenReturn(statfulSenderAPI);
        when(statfulSenderAPI.tags(any(Tags.class))).thenReturn(statfulSenderAPI);
        when(statfulSenderAPI.aggregations(any(Aggregations.class))).thenReturn(statfulSenderAPI);
        when(statfulSenderAPI.namespace(anyString())).thenReturn(statfulSenderAPI);
        when(statfulClient.timer(anyString(), anyLong())).thenReturn(statfulSenderFacade);

        subject = new StatfulAspect();
        subject.setStatfulClient(statfulClient);
    }

    @Test
    public void shouldSendMetricOnTimerAnnotationSuccessfully() throws Throwable {
        // Given
        when(joinPoint.proceed()).thenReturn("something");

        // When
        subject.methodTiming(joinPoint, timer);

        // Then
        ArgumentCaptor<Tags> tagsCaptor = ArgumentCaptor.forClass(Tags.class);
        verify(statfulSenderAPI).tags(tagsCaptor.capture());
        assertEquals("success", tagsCaptor.getValue().getTagValue("status"));

        verify(statfulClient).timer(eq("timerName"), anyLong());
        verify(statfulSenderAPI).send();
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
        verify(statfulSenderAPI).tags(tagsCaptor.capture());
        assertEquals("error", tagsCaptor.getValue().getTagValue("status"));

        verify(statfulClient).timer(eq("timerName"), anyLong());
        verify(statfulSenderAPI).send();
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
        verify(statfulSenderAPI).namespace(namespaceCaptor.capture());
        assertNull(namespaceCaptor.getValue());

        verify(statfulClient).timer(eq("timerName"), anyLong());
        verify(statfulSenderAPI).send();
    }
}