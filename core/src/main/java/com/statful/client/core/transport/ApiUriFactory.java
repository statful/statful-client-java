package com.statful.client.core.transport;

/**
 * A factory for producing URIs for the Statful API.
 */
public abstract class ApiUriFactory {

    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String METRICS_PATH = "/tel/v2.0/metrics";
    private static final String AGGREGATED_METRICS_PATH = "/tel/v2.0/metrics/aggregation/{aggregation}/frequency/{frequency}";

    /**
     * Builds an URI to send metrics to Statful.
     *
     * @param secure Whether it should be sent using HTTP or HTTPS
     * @param host The Statful API host
     * @param port The Statful API port
     * @return A fully built URI for sending metrics to Statful
     */
    public static String buildUri(final boolean secure, final String host, final Integer port) {
        return getProtocolForUri(secure) + host + getPortForUri(port) + METRICS_PATH;
    }

    /**
     * Builds an URI to send aggregated metrics to Statful.
     *
     * @param secure Whether it should be sent using HTTP or HTTPS
     * @param host The Statful API host
     * @param port The Statful API port
     * @return A fully built URI for sending metrics to Statful
     */
    public static String buildAggregatedUri(final boolean secure, final String host, final Integer port) {
        return getProtocolForUri(secure) + host + getPortForUri(port) + AGGREGATED_METRICS_PATH;
    }

    private static String getProtocolForUri(final boolean secure) {
        return secure ? HTTPS_PROTOCOL : HTTP_PROTOCOL;
    }

    private static String getPortForUri(final Integer port) {
        return port != null ? ":" + port : "";
    }
}
