package com.mindera.telemetron.client.api;

public final class TelemetronClientDelegate {

    private final MetricsSenderAPI metricsSenderAPI;

    public TelemetronClientDelegate(final MetricsSenderAPI metricsSenderAPI) {
        this.metricsSenderAPI = metricsSenderAPI;
    }

    public MetricsSenderAPI with() {
        return metricsSenderAPI;
    }

    public void send() {
        metricsSenderAPI.send();
    }
}
