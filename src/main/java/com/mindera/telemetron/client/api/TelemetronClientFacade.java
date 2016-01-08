package com.mindera.telemetron.client.api;

public final class TelemetronClientFacade implements SenderFacade {

    private final SenderAPI senderAPI;

    public TelemetronClientFacade(final SenderAPI senderAPI) {
        this.senderAPI = senderAPI;
    }

    @Override
    public SenderAPI with() {
        return senderAPI;
    }

    @Override
    public void send() {
        senderAPI.send();
    }
}
