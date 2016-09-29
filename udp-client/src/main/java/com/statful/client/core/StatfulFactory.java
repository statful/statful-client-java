package com.statful.client.core;

import com.statful.client.core.api.StatfulClientBuilder;
import com.statful.client.core.transport.TransportSender;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.Transport;
import com.statful.client.transport.UDPSender;

/**
 * A factory for instantiating Statful UDP clients.
 */
public final class StatfulFactory {

    private static final UDPClientFactory UDP_CLIENT_FACTORY = new UDPClientFactory();

    private StatfulFactory() { }

    /**
     * Instantiates a new {@link com.statful.client.core.api.StatfulClientBuilder} to use UDP protocol.
     *
     * @return A Statful client builder, ready for configure or bootstrap
     */
    public static StatfulClientBuilder buildUDPClient() {
        return UDP_CLIENT_FACTORY.buildClient();
    }

    /**
     * Private UDP client factory.
     */
    private static class UDPClientFactory extends CustomStatfulFactory {
        UDPClientFactory() {
            super(Transport.UDP);
        }

        @Override
        protected TransportSender buildTransportSender(final ClientConfiguration configuration) {
            return new UDPSender(configuration.getHost(), configuration.getPort());
        }
    }
}
