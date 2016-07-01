package com.statful.client.domain.api;

/**
 * This interface serves as a facade to a {@link SenderAPI} implementation
 * to allow sending metrics without using the {@link SenderAPI} directly.
 * <p>
 * Example:<br>
 * <p>
 * <code>
 *     statful.counter("my_metric").send();
 * </code>
 * <p>
 * Or:<br>
 * <code>
 *     statful.counter("my_metric").with().namespace("my_namespace").send();
 * </code>
 */
public interface SenderFacade {

    /**
     * A syntax sugar method.
     *
     * @return A reference to {@link SenderAPI}
     */
    SenderAPI with();

    /**
     * A facade method to send the metrics.
     */
    void send();
}
