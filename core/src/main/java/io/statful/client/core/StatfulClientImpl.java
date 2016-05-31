package io.statful.client.core;

import io.statful.client.core.api.*;
import io.statful.client.domain.api.*;

import java.util.logging.Logger;

/**
 * This class is the Statful client, which allows to send metrics (timer, counter, gauge or raw metric) to Statful.
 */
class StatfulClientImpl implements StatfulClient {

    private static final Logger LOGGER = Logger.getLogger(StatfulClientImpl.class.getName());

    private final MetricsSender metricsSender;
    private final ClientConfiguration configuration;

    private boolean enabled = true;

    /**
     * Default constructor.
     *
     * @param metricsSender The {@link MetricsSender} to send metrics
     * @param configuration The {@link ClientConfiguration} to configure the client
     */
    StatfulClientImpl(final MetricsSender metricsSender, final ClientConfiguration configuration) {
        this.metricsSender = metricsSender;
        this.configuration = configuration;
    }

    @Override
    public final SenderFacade timer(final String metricName, final long timestamp) {
        SenderAPI senderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getTimerAggregations())
                .aggFreq(configuration.getTimerAggregationFreq())
                .tags(configuration.getTimerTags())
                .metricName("timer." + metricName)
                .value(Long.toString(timestamp));

        return new StatfulClientFacade(senderAPI);
    }

    @Override
    public final SenderFacade counter(final String metricName) {
        return counter(metricName, 1);
    }

    @Override
    public final SenderFacade counter(final String metricName, final int value) {
        SenderAPI senderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getCounterAggregations())
                .aggFreq(configuration.getCounterAggregationFreq())
                .tags(configuration.getCounterTags())
                .metricName("counter." + metricName)
                .value(Integer.toString(value));

        return new StatfulClientFacade(senderAPI);
    }

    private SenderFacade gauge(final String metricName, final String value) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getGaugeAggregations())
                .aggFreq(configuration.getGaugeAggregationFreq())
                .tags(configuration.getGaugeTags())
                .metricName("gauge." + metricName)
                .value(value);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade gauge(final String metricName, final Long value) {
        return gauge(metricName, value.toString());
    }

    @Override
    public final SenderFacade gauge(final String metricName, final Double value) {
        return gauge(metricName, value.toString());
    }

    @Override
    public final SenderFacade gauge(final String metricName, final Float value) {
        return gauge(metricName, value.toString());
    }

    @Override
    public final SenderFacade gauge(final String metricName, final Integer value) {
        return gauge(metricName, value.toString());
    }

    @Override
    public final void put(
            final String name, final String value, final Tags tags, final Aggregations aggregations,
            final AggregationFreq aggregationFreq, final Integer sampleRate, final String namespace,
            final long timestamp
    ) {
        if (enabled) {
            try {
                metricsSender.put(name, value, tags, aggregations, aggregationFreq, sampleRate, namespace, timestamp);
            } catch (Exception e) {
                LOGGER.warning("Unable to send metric: " + e.toString());
            }
        } else {
            LOGGER.fine("Telemetry client is disabled. The metric was not sent.");
        }
    }

    @Override
    public void enable() {
        enabled = true;
    }

    @Override
    public void disable() {
        enabled = false;
    }

    @Override
    public final void shutdown() {
        metricsSender.shutdown();
    }
}
