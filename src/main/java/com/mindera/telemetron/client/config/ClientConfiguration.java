package com.mindera.telemetron.client.config;

import com.mindera.telemetron.client.api.*;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.AggregationFreq.*;

public interface ClientConfiguration {

    static final String DEFAULT_HOST = "127.0.0.1";
    static final int DEFAULT_PORT = 2013;
    static final int DEFAULT_SAMPLE_RATE = 100;
    static final String DEFAULT_NAMESPACE = "application";
    static final int DEFAULT_FLUSH_SIZE = 10;
    static final AggregationFreq DEFAULT_AGGREGATION_FREQ = FREQ_10;

    static final Tags DEFAULT_APP_TAGS = Tags.from("telemetron_client", "java");
    static final Tags DEFAULT_TIMER_TAGS = Tags.from("unit", "ms");

    static final Aggregation[] DEFAULT_TIMER_AGGREGATIONS = new Aggregation[] { AVG, P90, COUNT, COUNT_PS };
    static final Aggregation[] DEFAULT_COUNTER_AGGREGATIONS = new Aggregation[] { AVG, P90, COUNT_PS };
    static final Aggregation[] DEFAULT_GAUGE_AGGREGATIONS = new Aggregation[] { LAST };

    boolean isValid();

    Tags getApplicationTags();

    int getSampleRate();

    String getNamespace();

    String getPrefix();

    int getFlushSize();

    long getFlushIntervalMillis();

    boolean isDryRun();

    String getHost();

    int getPort();

    Tags getTimerTags();

    Aggregations getTimerAggregations();

    AggregationFreq getTimerAggregationFreq();

    Tags getCounterTags();

    Aggregations getCounterAggregations();

    AggregationFreq getCounterAggregationFreq();

    Tags getGaugeTags();

    Aggregations getGaugeAggregations();

    AggregationFreq getGaugeAggregationFreq();

    Transport getTransport();

    String getToken();

    String getApp();
}
