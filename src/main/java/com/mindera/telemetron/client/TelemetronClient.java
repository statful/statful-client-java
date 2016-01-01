package com.mindera.telemetron.client;

import com.mindera.telemetron.client.api.*;
import com.mindera.telemetron.client.config.ClientConfiguration;
import com.mindera.telemetron.client.sender.BufferedMetricsSender;
import com.mindera.telemetron.client.sender.MetricsSender;
import com.mindera.telemetron.client.transport.DummySender;
import com.mindera.telemetron.client.transport.TransportSender;

import java.util.logging.Logger;

import static com.mindera.telemetron.client.api.Transport.*;

public class TelemetronClient implements MetricsSender {

    private static final Logger LOGGER = Logger.getLogger(TelemetronClient.class.getName());

    private final MetricsSender metricsSender;
    private final ClientConfiguration configuration;

    public TelemetronClient(MetricsSender metricsSender, ClientConfiguration configuration) {
        this.metricsSender = metricsSender;
        this.configuration = configuration;
    }

    public APIBuilder timer(String metricName, long value) {
        return new APIBuilder(this)
                .withConfiguration(configuration)
                .with(configuration.getTimerAggregations())
                .withAggrFreq(configuration.getTimerAggregationFreq())
                .with(configuration.getTimerTags())
                .withMetricName("timer." + metricName)
                .withValue(Long.toString(value));
    }

    public APIBuilder counter(String metricName) {
        return counter(metricName, 0);
    }

    public APIBuilder counter(String metricName, int value) {
        return new APIBuilder(this)
                .withConfiguration(configuration)
                .with(configuration.getCounterAggregations())
                .withAggrFreq(configuration.getCounterAggregationFreq())
                .with(configuration.getCounterTags())
                .withMetricName("counter." + metricName)
                .withValue(Integer.toString(value));
    }

    public APIBuilder gauge(String metricName, String value) {
        return new APIBuilder(this)
                .withConfiguration(configuration)
                .with(configuration.getGaugeAggregations())
                .withAggrFreq(configuration.getGaugeAggregationFreq())
                .with(configuration.getGaugeTags())
                .withMetricName("gauge." + metricName)
                .withValue(value);
    }

    @Override
    public void put(String name, String value, Tags tags, Aggregations aggregations, Integer aggregationFreq, Integer sampleRate, String namespace, String timestamp) {
        metricsSender.put(name, value, tags, aggregations, aggregationFreq, sampleRate, namespace, timestamp);
    }

    public static ConfigurationBuilder<TelemetronClient> newUDPClient(String prefix) {
        return ConfigurationBuilder
                .newBuilder(new ConfigurationBuilderChain<TelemetronClient>() {
                    @Override
                    public TelemetronClient build(ClientConfiguration configuration) {
                        TransportSender transportSender = new DummySender();
                        MetricsSender buffuredMetricsSender = new BufferedMetricsSender(transportSender, configuration);
                        return new TelemetronClient(buffuredMetricsSender, configuration);
                    }
                })
                .withTransport(UDP)
                .withPrefix(prefix);
    }
}
