package com.mindera.telemetron.client.api;

class SampleRateBuilder {
    private final int sampleRate;

    SampleRateBuilder(int sampleRate) {
        this.sampleRate = sampleRate;
    }

    public int getSampleRate() {
        return sampleRate;
    }
}
