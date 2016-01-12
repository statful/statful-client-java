package com.mindera.telemetron.client;

import com.mindera.telemetron.client.api.*;
import com.mindera.telemetron.client.sender.BufferedMetricsSender;
import com.mindera.telemetron.client.transport.TransportSender;
import com.mindera.telemetron.transport.UDPSender;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Logger;

import static com.mindera.telemetron.client.api.Transport.UDP;

/**
 * A factory for instantiating Telemetron UDP clients.
 */
public final class TelemetronFactory {

    private static final Logger LOGGER = Logger.getLogger(TelemetronClientImpl.class.getName());

    private TelemetronFactory() { }

    /**
     * Instantiates a new Telemetron client builder with UDP protocol.
     *
     * @param prefix The metric prefix
     * @return A Telemetron client builder, ready for configure or bootstrap
     */
    public static TelemetronClientBuilder buildUDPClient(final String prefix) {
        LOGGER.info("Starting Telemetron client.");
        ConfigurationBuilder<TelemetronClient> configurationBuilder = ConfigurationBuilder
                .newBuilder(builderChain).transport(UDP).prefix(prefix);

        return new TelemetronClientBuilder(configurationBuilder);
    }

    private static ConfigurationBuilderChain<TelemetronClient> builderChain =
            new ConfigurationBuilderChain<TelemetronClient>() {
                @Override
                public TelemetronClientImpl build(final ClientConfiguration configuration) {
                    int poolSize = configuration.getWorkersPoolSize();
                    ScheduledExecutorService executorService = Executors.newScheduledThreadPool(poolSize);

                    TransportSender transportSender = new UDPSender(configuration.getHost(), configuration.getPort());
                    MetricsSender bufferedMetricsSender = new BufferedMetricsSender(transportSender, configuration, executorService);
                    return new TelemetronClientImpl(bufferedMetricsSender, configuration);
                }
            };
}
