package com.mindera.telemetron.client.api;

public interface SenderFacade {
    SenderAPI with();
    void send();
}
