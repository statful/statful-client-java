package com.statful.client.core.http;

import com.statful.client.core.CustomStatfulFactory;
import com.statful.client.core.api.StatfulClientBuilder;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.transport.HTTPSender;
import com.statful.client.transport.SSLClientFactory;

import static com.statful.client.domain.api.Transport.HTTP;

/**
 * A factory for instantiating Statful HTTP clients.
 */
public final class StatfulFactory {

    private static final HTTPClientFactory HTTP_CLIENT_FACTORY = new HTTPClientFactory();

    private StatfulFactory() { }

    /**
     * Instantiates a new {@link StatfulClientBuilder} to use HTTP protocol.
     *
     * @return A Statful client builder, ready for configure or bootstrap
     */
    public static StatfulClientBuilder buildHTTPClient() {
        return HTTP_CLIENT_FACTORY.buildClient();
    }

    /**
     * Private HTTP client factory.
     */
    private static class HTTPClientFactory extends CustomStatfulFactory {

        protected HTTPClientFactory() {
            super(HTTP);
        }

        @Override
        protected TransportSender buildTransportSender(final ClientConfiguration configuration) {
            SSLClientFactory clientFactory = buildHTTPClientFactory(configuration);
            return new HTTPSender(configuration.isSecure(), configuration.getHost(), configuration.getPort(),
                    configuration.getPath(), clientFactory);
        }

        private static SSLClientFactory buildHTTPClientFactory(final ClientConfiguration configuration) {
            return new SSLClientFactory(
                    configuration.getConnectionPoolSize(),
                    configuration.getConnectTimeoutMillis(),
                    configuration.getSocketTimeoutMillis(),
                    configuration.getToken()
            );
        }
    }
}
