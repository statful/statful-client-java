package com.mindera.telemetron.client.sender;

import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;

public interface MetricsSender {
    void put(String name, String value, Tags tags, Aggregations aggregations, Integer aggregationFreq, Integer sampleRate, String namespace, String timestamp);

    void shutdown();
}
