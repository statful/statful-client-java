package com.statful.client.transport;

import com.statful.client.core.transport.ApiUriFactory;
import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.AggregationFrequency;
import com.statful.client.test.HttpTest;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.impl.client.CloseableHttpClient;
import org.junit.Test;

import java.io.IOException;
import java.security.GeneralSecurityException;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static org.mockserver.verify.VerificationTimes.once;

public class HTTPSenderAPITest extends HttpTest {

    private static final String METRIC = "telemetron.application.timer.execution,app=uwt,delegate=InterceptorDelegate,unit=ms,environment=production,method=preHandle,status=success 26 1465394947 avg,p90,count,10";

    private HTTPSender subject;

    @Test
    public void shouldSendThroughHttp() {
        // Given
        mockMetricsPutWithStatusCode(201);
        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", new SSLClientFactory(10, 1000, 5000, "any-token"));

        // When
        subject.send(METRIC);

        // Then
        mockClientAndServer.verify(
                request()
                        .withBody(METRIC),
                once());
    }

    @Test
    public void shouldSendThroughHttpWithUri() {
        // Given
        String uri = ApiUriFactory.buildAggregatedUri(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics")
                .replace("{aggregation}", Aggregation.AVG.getName())
                .replace("{frequency}", Integer.toString(AggregationFrequency.FREQ_10.getValue()));

        mockMetricsPutWithStatusCodeAndUri(201, uri);
        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", new SSLClientFactory(10, 1000, 5000, "any-token"));

        // When
        subject.send(METRIC, uri);

        // Then
        mockClientAndServer.verify(
                request()
                        .withBody(METRIC),
                once());
    }

    @Test
    public void shouldNotThrowWhenMetricIsNotCreated() {
        // Given
        mockMetricsPutWithStatusCode(402);
        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", new SSLClientFactory(10, 1000, 5000, "any-token"));

        // When
        subject.send(METRIC);

        // Then
        mockClientAndServer.verify(request().withBody(METRIC), once());
    }

    @Test
    public void shouldNotThrowWhenHttpClientIsNull() throws Exception {
        // Given
        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenThrow(new GeneralSecurityException());

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.send(METRIC);
    }

    @Test
    public void shouldNotThrowWhenHttpClientThrowsException() throws Exception {
        // Given
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        when(httpClient.execute(any(HttpPut.class))).thenThrow(new IOException());

        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenReturn(httpClient);

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.send(METRIC);
    }

    @Test
    public void shouldShutDownClientSuccessfully() throws Exception {
        // Given
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);

        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenReturn(httpClient);

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.shutdown();

        // Then
        verify(httpClient).close();
    }

    @Test
    public void shouldNotThrowWhenHttpClientThrowsWhenClosing() throws Exception {
        // Given
        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        doThrow(new IOException()).when(httpClient).close();

        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenReturn(httpClient);

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.shutdown();
    }

    @Test
    public void shouldNotThrowWhenHttpClientIsNullWhenClosing() throws Exception {
        // Given
        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenReturn(null);

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.shutdown();
    }

    @Test
    public void shouldNotThrowWhenHttpClientResponseThrowsException() throws Exception {
        // Given
        StatusLine statusLine = mock(StatusLine.class);

        CloseableHttpResponse httpResponse = mock(CloseableHttpResponse.class);
        when(httpResponse.getStatusLine()).thenReturn(statusLine);
        doThrow(new IOException()).when(httpResponse).close();

        CloseableHttpClient httpClient = mock(CloseableHttpClient.class);
        when(httpClient.execute(any(HttpPut.class))).thenReturn(httpResponse);

        HTTPClientFactory httpClientFactory = mock(HTTPClientFactory.class);
        when(httpClientFactory.createHttpClient()).thenReturn(httpClient);

        subject = new HTTPSender(false, "127.0.0.1", mockServerPort, "/tel/v2.0/metrics", httpClientFactory);

        // When
        subject.send(METRIC);
    }

    private void mockMetricsPutWithStatusCode(int statusCode) {
        mockClientAndServer.when(
                request()
                        .withMethod("PUT")
                        .withPath("/tel/v2.0/metrics"),
                exactly(1))
                .respond(response().withStatusCode(statusCode));
    }

    private void mockMetricsPutWithStatusCodeAndUri(int statusCode, String uri) {
        mockClientAndServer.when(
                request()
                        .withMethod("PUT")
                        .withPath(uri),
                exactly(1))
                .respond(response().withStatusCode(statusCode));
    }
}