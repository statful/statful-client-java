package com.mindera.telemetron.client.api;

public final class TelemetronMetricBuilder {
    private final APIBuilder apiBuilder;

    public TelemetronMetricBuilder(APIBuilder apiBuilder) {
        this.apiBuilder = apiBuilder;
    }

    public APIBuilder with() {
        return apiBuilder;
    }

    public void send() {
        apiBuilder.send();
    }
}
