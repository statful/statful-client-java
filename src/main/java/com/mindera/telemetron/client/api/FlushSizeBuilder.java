package com.mindera.telemetron.client.api;

class FlushSizeBuilder {
    private final int flushSize;

    FlushSizeBuilder(int flushSize) {
        this.flushSize = flushSize;
    }

    int getFlushSize() {
        return flushSize;
    }
}
