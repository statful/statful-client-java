package io.statful.client.transport;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.ssl.SSLContexts;

import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;

/**
 * A factory for HTTP clients to communicate with Statful.
 */
public class HTTPClientFactory {

    private static final String TOKEN_HEADER = "M-Api-Token";

    private final String token;
    private final int connectionPoolSize;
    private final int connectTimeoutMs;
    private final int socketTimeoutMs;

    /**
     * Default constructor.
     *
     * @param connectionPoolSize The connection pool size
     * @param connectTimeoutMs The connect timeout in milliseconds
     * @param socketTimeoutMs The socket timeout in milliseconds
     * @param token The Statful authentication token
     */
    public HTTPClientFactory(final int connectionPoolSize, final int connectTimeoutMs, final int socketTimeoutMs, final String token) {
        this.connectionPoolSize = connectionPoolSize;
        this.connectTimeoutMs = connectTimeoutMs;
        this.socketTimeoutMs = socketTimeoutMs;
        this.token = token;
    }

    /**
     * Creates a new HTTP client to communicate with Statful.
     *
     * @return A closeable HTTP client
     * @throws GeneralSecurityException Thrown when SSL specific configurations fail
     */
    public CloseableHttpClient createHttpClient() throws GeneralSecurityException {
        return HttpClients.custom()
                .setConnectionManager(createConnectionManager(connectionPoolSize))
                .setSSLSocketFactory(createSslSocketFactory())
                .build();
    }

    private static PoolingHttpClientConnectionManager createConnectionManager(final int connectionPoolSize) {
        PoolingHttpClientConnectionManager connectionManager = new PoolingHttpClientConnectionManager();
        connectionManager.setMaxTotal(connectionPoolSize);
        connectionManager.setDefaultMaxPerRoute(connectionPoolSize);

        return connectionManager;

    }

    private static SSLConnectionSocketFactory createSslSocketFactory() throws GeneralSecurityException {
        return new SSLConnectionSocketFactory(
                SSLContexts.createSystemDefault(),
                new String[] {
                        "TLSv1", "TLSv2"
                },
                null,
                SSLConnectionSocketFactory.getDefaultHostnameVerifier());
    }

    /**
     * Creates a new {@link HttpPut} object to be used to send PUT requests to the specified URI.
     *
     * @param uri The URI as a string
     * @param body The message body as a string to send to Staful
     * @return A newly created {@link HttpPut} object
     * @throws UnsupportedEncodingException Thrown when the encoding isn't supported
     */
    public final HttpPut createHttpPut(final String uri, final String body) throws UnsupportedEncodingException {
        HttpPut httpPut = new HttpPut(uri);
        httpPut.addHeader(TOKEN_HEADER, token);
        httpPut.setEntity(new StringEntity(body));
        httpPut.setConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectTimeout(connectTimeoutMs)
                .setConnectionRequestTimeout(connectTimeoutMs)
                .setSocketTimeout(socketTimeoutMs)
                .build());

        return httpPut;
    }
}
