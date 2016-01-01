package com.mindera.telemetron.client.api;

class TransportBuilder {
    private final Transport transport;

    TransportBuilder(Transport transport) {
        this.transport = transport;
    }

    Transport getTransport() {
        return transport;
    }
}
