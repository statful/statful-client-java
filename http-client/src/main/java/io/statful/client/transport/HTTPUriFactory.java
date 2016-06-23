package io.statful.client.transport;

/**
 * A factory for producing URIs for Statful.
 */
public final class HTTPUriFactory {

    private static final String HTTP_PROTOCOL = "http://";
    private static final String HTTPS_PROTOCOL = "https://";
    private static final String METRICS_PATH = "/tel/v1/metrics";

    private HTTPUriFactory() {

    }

    /**
     * Builds an URI to send metrics to Statful.
     *
     * @param secure Whether it should be sent using HTTP or HTTPS
     * @param host The Staful API host
     * @param port The Statful API port
     * @return A fully built URI for sending metrics to Statful
     */
    public static String buildUri(final boolean secure, final String host, final Integer port) {
        return getProtocolForUri(secure) + host + getPortForUri(port) + METRICS_PATH;
    }

    private static String getProtocolForUri(final boolean secure) {
        if (secure) {
            return HTTPS_PROTOCOL;
        } else {
            return HTTP_PROTOCOL;
        }
    }

    private static String getPortForUri(final Integer port) {
        if (port != null) {
            return ":" + port;
        } else {
            return "";
        }
    }
}
