package com.mindera.telemetron.client.api;

class FlushIntervalBuilder {
    private final long flushInterval;

    FlushIntervalBuilder(long flushInterval) {
        this.flushInterval = flushInterval;
    }

    long getFlushInterval() {
        return flushInterval;
    }
}
