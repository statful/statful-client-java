package io.statful.client.core.api;

import io.statful.client.domain.api.SenderAPI;
import io.statful.client.domain.api.SenderFacade;

/**
 * This class is an implementation facade for the Statful client.
 */
public final class StatfulClientFacade implements SenderFacade {

    private final SenderAPI senderAPI;

    /**
     * Default constructor.
     *
     * @param senderAPI The {@link SenderAPI} to use to send metrics
     */
    public StatfulClientFacade(final SenderAPI senderAPI) {
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
