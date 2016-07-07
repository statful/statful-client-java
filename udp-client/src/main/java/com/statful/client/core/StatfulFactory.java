package com.statful.client.core;

import com.statful.client.core.api.ConfigurationBuilder;
import com.statful.client.core.api.ConfigurationBuilderChain;
import com.statful.client.core.api.StatfulClientBuilder;
import com.statful.client.core.sender.BufferedMetricsSender;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.MetricsSender;
import com.statful.client.domain.api.StatfulClient;
import com.statful.client.domain.api.Transport;
import com.statful.client.transport.UDPSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

/**
 * A factory for instantiating Statful UDP clients.
 */
public final class StatfulFactory {

    private static final Logger LOGGER = Logger.getLogger(StatfulClientImpl.class.getName());

    private StatfulFactory() { }

    /**
     * Instantiates a new {@link com.statful.client.core.api.StatfulClientBuilder} to use UDP protocol.
     *
     * @return A Statful client builder, ready for configure or bootstrap
     */
    public static StatfulClientBuilder buildUDPClient() {
        LOGGER.info("Starting Statful client.");
        ConfigurationBuilder<StatfulClient> configurationBuilder = ConfigurationBuilder
                .newBuilder(builderChain).transport(Transport.UDP);

        return new StatfulClientBuilder(configurationBuilder);
    }

    private static ConfigurationBuilderChain<StatfulClient> builderChain =
            new ConfigurationBuilderChain<StatfulClient>() {
                @Override
                public StatfulClientImpl build(final ClientConfiguration configuration) {
                    int poolSize = configuration.getWorkersPoolSize();
                    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);

                    TransportSender transportSender = new UDPSender(configuration.getHost(), configuration.getPort());
                    MetricsSender bufferedMetricsSender = new BufferedMetricsSender(transportSender, configuration, executorService);
                    return new StatfulClientImpl(bufferedMetricsSender, configuration);
                }
            };
}
