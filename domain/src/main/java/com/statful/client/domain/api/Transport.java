package com.statful.client.domain.api;

/**
 * Supported transport types. Changing the default values will break the tests, to avoid unnoticed changes.
 */
public enum Transport {
    UDP {
        @Override
        public String getDefaultHost() {
            return DEFAULT_UDP_HOST;
        }

        @Override
        public int getDefaultPort() {
            return DEFAULT_UDP_PORT;
        }
    }, HTTP {
        @Override
        public String getDefaultHost() {
            return DEFAULT_HTTP_HOST;
        }

        @Override
        public int getDefaultPort() {
            return DEFAULT_HTTP_PORT;
        }
    }, OTHER {
        @Override
        public String getDefaultHost() {
            throw new UnsupportedOperationException("You must set a host with transport OTHER");
        }

        @Override
        public int getDefaultPort() {
            throw new UnsupportedOperationException("You must set a port with transport OTHER");
        }
    };

    private static final String DEFAULT_UDP_HOST = "127.0.0.1";
    private static final int DEFAULT_UDP_PORT = 2013;
    private static final String DEFAULT_HTTP_HOST = "api.statful.com";
    private static final int DEFAULT_HTTP_PORT = 443;

    /**
     * @return The default host for the transport used
     */
    public abstract String getDefaultHost();

    /**
     * @return The default port for the transport used
     */
    public abstract int getDefaultPort();
}
