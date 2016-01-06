package com.mindera.telemetron.client.api;

public final class TelemetronClientDelegate {

    private final APIBuilder apiBuilder;

    public TelemetronClientDelegate(final APIBuilder apiBuilder) {
        this.apiBuilder = apiBuilder;
    }

    public APIBuilder with() {
        return apiBuilder;
    }

    public void send() {
        apiBuilder.send();
    }
}
