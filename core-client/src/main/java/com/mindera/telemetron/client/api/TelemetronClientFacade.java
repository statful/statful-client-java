package com.mindera.telemetron.client.api;

/**
 * This class is an implementation facade for the Telemetron client.
 */
public final class TelemetronClientFacade implements SenderFacade {

    private final SenderAPI senderAPI;

    /**
     * Default constructor.
     *
     * @param senderAPI The {@link com.mindera.telemetron.client.api.SenderAPI} to use to send metrics
     */
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
