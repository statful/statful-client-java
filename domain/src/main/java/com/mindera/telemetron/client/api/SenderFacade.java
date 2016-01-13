package com.mindera.telemetron.client.api;

/**
 * This interface serves as a facade to a {@link com.mindera.telemetron.client.api.SenderAPI} implementation
 * to allow sending metrics without using the {@link com.mindera.telemetron.client.api.SenderAPI} directly.
 * <p>
 * Example:<br>
 * <p>
 * <code>
 *     telemetron.counter("my_metric").send();
 * </code>
 * <p>
 * Or:<br>
 * <code>
 *     telemetron.counter("my_metric").with().namespace("my_namespace").send();
 * </code>
 */
public interface SenderFacade {

    /**
     * A syntax sugar method.
     *
     * @return A reference to {@link com.mindera.telemetron.client.api.SenderAPI}
     */
    SenderAPI with();

    /**
     * A facade method to send the metrics.
     */
    void send();
}
