package com.statful.client.transport;

import com.statful.client.core.transport.ApiUriFactory;
import com.statful.client.core.transport.TransportSender;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class is an implementation of {@link com.statful.client.core.transport.TransportSender} to send metrics
 * using HTTP or HTTPS.
 */
public class HTTPSender implements TransportSender {

    private static final Logger LOGGER = Logger.getLogger(HTTPSender.class.getName());
    private static final Integer HTTP_CREATED = 201;
    private static final Integer HTTP_OK = 200;

    private final String uri;
    private final HTTPClientFactory clientFactory;

    private CloseableHttpClient httpClient;

    /**
     * Default constructor.
     *
     * @param secure Flag to send the message securely, weather using HTTP or HTTPS.
     * @param host The Statful API host
     * @param port The Staful API port
     * @param path The Staful API path
     * @param clientFactory The HTTP client factory to use in this sender
     */
    public HTTPSender(final boolean secure, final String host, final Integer port, final String path, final HTTPClientFactory clientFactory) {
        this.uri = ApiUriFactory.buildUri(secure, host, port, path);
        this.clientFactory = clientFactory;

        try {
            this.httpClient = clientFactory.createHttpClient();
        } catch (Exception e) {
            LOGGER.severe("Unable to instantiate HTTP client: " + e.toString());
        }
    }

    @Override
    public final void send(final String message) {
        doHttpRequest(uri, message);
    }

    @Override
    public final void send(final String message, final String uri) {
        doHttpRequest(uri, message);
    }

    private void doHttpRequest(final String uri, final String message) {
        CloseableHttpResponse response = null;

        try {
            if (httpClient != null) {
                response = httpClient.execute(clientFactory.createHttpPut(uri, message));
                StatusLine statusLine = response.getStatusLine();

                if (!metricCreated(statusLine)) {
                    LOGGER.warning("Unable to send metric. Reason: "
                            + statusLine.getReasonPhrase() + ", status: "
                            + statusLine.getStatusCode());
                }
            } else {
                LOGGER.warning("Unable to send metric. No http client was instantiated.");
            }
        } catch (IOException e) {
            LOGGER.severe("Unable to send metric: " + e.toString());
        } finally {
            try {
                if (response != null) {
                    response.close();
                }
            } catch (Exception e) {
                LOGGER.warning("Unable to close response resource: " + e.toString());
            }
        }
    }

    @Override
    public final void shutdown() {
        try {
            if (httpClient != null) {
                httpClient.close();
            }
        } catch (Exception e) {
            LOGGER.warning("Unable to close HTTP client: " + e.toString());
        }
    }

    private boolean metricCreated(final StatusLine statusLine) {
        return HTTP_CREATED == statusLine.getStatusCode() || HTTP_OK == statusLine.getStatusCode();
    }
}
