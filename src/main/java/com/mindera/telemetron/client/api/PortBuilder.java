package com.mindera.telemetron.client.api;

class PortBuilder {
    private final int port;

    PortBuilder(int port) {
        this.port = port;
    }

    int getPort() {
        return port;
    }
}
