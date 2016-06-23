package io.statful.client.transport;

import io.statful.client.core.transport.TransportSender;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.impl.client.CloseableHttpClient;

import java.io.IOException;
import java.util.logging.Logger;

/**
 * This class is an implementation of {@link io.statful.client.core.transport.TransportSender} to send metrics
 * using HTTP or HTTPS.
 */
public class HTTPSender implements TransportSender {

    private static final Logger LOGGER = Logger.getLogger(HTTPSender.class.getName());
    private static final Integer HTTP_CREATED = 201;

    private final String uri;
    private final HTTPClientFactory clientFactory;

    private CloseableHttpClient httpClient;

    /**
     * Default constructor.
     *
     * @param secure Flag to send the message securely, weather using HTTP or HTTPS.
     * @param host The Statful API host
     * @param port The Staful API port
     * @param clientFactory The HTTP client factory to use in this sender
     */
    public HTTPSender(final boolean secure, final String host, final Integer port, final HTTPClientFactory clientFactory) {
        this.uri = HTTPUriFactory.buildUri(secure, host, port);
        this.clientFactory = clientFactory;

        try {
            this.httpClient = clientFactory.createHttpClient();
        } catch (Exception e) {
            LOGGER.severe("Unable to instantiate HTTP client: " + e.toString());
        }
    }

    @Override
    public final void send(final String message) {
        CloseableHttpResponse response = null;

        try {
            if (httpClient != null) {
                response = httpClient.execute(clientFactory.createHttpPut(uri, message));
                StatusLine statusLine = response.getStatusLine();

                if (metricNotCreated(statusLine)) {
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

    private boolean metricNotCreated(final StatusLine statusLine) {
        return HTTP_CREATED != statusLine.getStatusCode();
    }
}
