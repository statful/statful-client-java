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

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.logging.Logger;

/**
 * This is an abstract Statful factory for building custom clients.
 */
public abstract class CustomStatfulFactory {

    private static final Logger LOGGER = Logger.getLogger(CustomStatfulFactory.class.getName());

    private final Transport transport;
    private final ConfigurationBuilderChain<StatfulClient> builderChain;

    protected CustomStatfulFactory(final Transport transport) {
        this.transport = transport;
        this.builderChain = new ConfigurationBuilderChain<StatfulClient>() {
            @Override
            public StatfulClient build(final ClientConfiguration configuration) {
                TransportSender transportSender = buildTransportSender(configuration);
                ScheduledThreadPoolExecutor executorService = new ScheduledThreadPoolExecutor(configuration.getWorkersPoolSize());
                MetricsSender bufferedMetricsSender = new BufferedMetricsSender(transportSender, configuration, executorService);
                return new StatfulClientImpl(bufferedMetricsSender, configuration);
            }
        };
    }

    /**
     * Builder that returns a fully instantiated Statful client.
     *
     * @return An instance of the Statful client
     */
    public final StatfulClientBuilder buildClient() {
        LOGGER.info("Starting Statful client.");
        ConfigurationBuilder<StatfulClient> configurationBuilder =
                ConfigurationBuilder
                        .newBuilder(builderChain)
                        .transport(transport);

        return new StatfulClientBuilder(configurationBuilder);
    }

    /**
     * Abstract method to be implemented by custom Statful client implementations.
     *
     * @param configuration The client configuration object with configuration values
     * @return An instance of the custom Statful transport sender
     */
    protected abstract TransportSender buildTransportSender(ClientConfiguration configuration);
}
