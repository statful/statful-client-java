package io.statful.client.core;

import io.statful.client.core.api.ConfigurationBuilder;
import io.statful.client.core.api.ConfigurationBuilderChain;
import io.statful.client.core.api.StatfulClientBuilder;
import io.statful.client.core.sender.BufferedMetricsSender;
import io.statful.client.core.transport.TransportSender;
import io.statful.client.domain.api.ClientConfiguration;
import io.statful.client.domain.api.MetricsSender;
import io.statful.client.domain.api.StatfulClient;
import io.statful.client.domain.api.Transport;
import io.statful.client.transport.SSLClientFactory;
import io.statful.client.transport.HTTPSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * A factory for instantiating Statful HTTP clients.
 */
public final class StatfulFactory {

    private static final Logger LOGGER = Logger.getLogger(StatfulClientImpl.class.getName());

    private StatfulFactory() { }

    /**
     * Instantiates a new {@link io.statful.client.core.api.StatfulClientBuilder} to use HTTP protocol.
     *
     * @param prefix The metric prefix
     * @return A Statful client builder, ready for configure or bootstrap
     */
    public static StatfulClientBuilder buildHTTPClient(final String prefix) {
        LOGGER.info("Starting Statful client.");
        ConfigurationBuilder<StatfulClient> configurationBuilder = ConfigurationBuilder
                .newBuilder(builderChain).transport(Transport.HTTP).prefix(prefix);

        return new StatfulClientBuilder(configurationBuilder);
    }

    private static ConfigurationBuilderChain<StatfulClient> builderChain =
            new ConfigurationBuilderChain<StatfulClient>() {
                @Override
                public StatfulClientImpl build(final ClientConfiguration configuration) {
                    int poolSize = configuration.getWorkersPoolSize();
                    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);

                    SSLClientFactory clientFactory = buildHTTPClientFactory(configuration);
                    TransportSender transportSender = buildTransportSender(configuration, clientFactory);
                    MetricsSender bufferedMetricsSender = new BufferedMetricsSender(transportSender, configuration, executorService);
                    return new StatfulClientImpl(bufferedMetricsSender, configuration);
                }
            };

    private static SSLClientFactory buildHTTPClientFactory(final ClientConfiguration configuration) {
        return new SSLClientFactory(
                configuration.getConnectionPoolSize(),
                configuration.getConnectTimeoutMillis(),
                configuration.getSocketTimeoutMillis(),
                configuration.getToken()
        );
    }

    private static TransportSender buildTransportSender(final ClientConfiguration configuration, final SSLClientFactory clientFactory) {
        return new HTTPSender(configuration.isSecure(), configuration.getHost(), configuration.getPort(), clientFactory);
    }
}
