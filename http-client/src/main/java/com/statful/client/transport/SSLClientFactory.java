package com.statful.client.transport;

import com.statful.client.domain.api.ClientConfiguration;
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
 * A factory for HTTP clients to communicate with Statful using SSL (it also supports non-secure communications).
 */
public class SSLClientFactory implements HTTPClientFactory {

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
    public SSLClientFactory(final int connectionPoolSize, final int connectTimeoutMs, final int socketTimeoutMs, final String token) {
        this.connectionPoolSize = connectionPoolSize;
        this.connectTimeoutMs = connectTimeoutMs;
        this.socketTimeoutMs = socketTimeoutMs;
        this.token = token;
    }

    @Override
    public final CloseableHttpClient createHttpClient() throws GeneralSecurityException {
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

    @Override
    public final HttpPut createHttpPut(final String uri, final String body) throws UnsupportedEncodingException {
        HttpPut httpPut = new HttpPut(uri);
        httpPut.addHeader(ClientConfiguration.TOKEN_HEADER, token);
        httpPut.setEntity(new StringEntity(body));
        httpPut.setConfig(RequestConfig.copy(RequestConfig.DEFAULT)
                .setConnectTimeout(connectTimeoutMs)
                .setConnectionRequestTimeout(connectTimeoutMs)
                .setSocketTimeout(socketTimeoutMs)
                .build());

        return httpPut;
    }
}
