package com.statful.client.core;

import com.statful.client.core.api.MetricsSenderAPI;
import com.statful.client.core.api.StatfulClientFacade;
import com.statful.client.domain.api.*;

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
    public final SenderFacade timer(final String metricName, final long value) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getTimerAggregations())
                .aggregationFrequency(configuration.getTimerAggregationFrequency())
                .tags(configuration.getTimerTags())
                .name("timer." + metricName)
                .value(Long.toString(value));

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade counter(final String metricName) {
        return counter(metricName, 1);
    }

    @Override
    public final SenderFacade counter(final String metricName, final int value) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getCounterAggregations())
                .aggregationFrequency(configuration.getCounterAggregationFrequency())
                .tags(configuration.getCounterTags())
                .name("counter." + metricName)
                .value(Integer.toString(value));

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
    public final SenderFacade put(final String metricName, final Long value) {
        return put(metricName, Long.toString(value));
    }

    @Override
    public final SenderFacade put(final String metricName, final Double value) {
        return put(metricName, Double.toString(value));
    }

    @Override
    public final SenderFacade put(final String metricName, final Float value) {
        return put(metricName, Float.toString(value));
    }

    @Override
    public final SenderFacade put(final String metricName, final Integer value) {
        return put(metricName, Integer.toString(value));
    }

    @Override
    public final SenderFacade aggregatedTimer(final String metricName, final long value, final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this, true).with()
                .configuration(configuration)
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .tags(configuration.getTimerTags())
                .name("timer." + metricName)
                .value(Long.toString(value));

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade aggregatedCounter(final String metricName, final int value, final Aggregation aggregation,
                                                final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this, true).with()
                .configuration(configuration)
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .tags(configuration.getCounterTags())
                .name("counter." + metricName)
                .value(Long.toString(value));

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade aggregatedGauge(final String metricName, final Long value, final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {
        return aggregatedGauge(metricName, value.toString(), aggregation, aggregationFrequency);
    }

    @Override
    public final SenderFacade aggregatedGauge(final String metricName, final Double value, final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {
        return aggregatedGauge(metricName, value.toString(), aggregation, aggregationFrequency);
    }

    @Override
    public final SenderFacade aggregatedGauge(final String metricName, final Float value, final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {
        return aggregatedGauge(metricName, value.toString(), aggregation, aggregationFrequency);
    }

    @Override
    public final SenderFacade aggregatedGauge(final String metricName, final Integer value,
                                              final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {
        return aggregatedGauge(metricName, value.toString(), aggregation, aggregationFrequency);
    }

    @Override
    public SenderFacade aggregatedPut(final String metricName, final Long value, final Aggregation aggregation,
                                      final AggregationFrequency aggregationFrequency) {
        return aggregatedPut(metricName, Long.toString(value), aggregation, aggregationFrequency);
    }

    @Override
    public SenderFacade aggregatedPut(final String metricName, final Double value, final Aggregation aggregation,
                                      final AggregationFrequency aggregationFrequency) {
        return aggregatedPut(metricName, Double.toString(value), aggregation, aggregationFrequency);
    }

    @Override
    public SenderFacade aggregatedPut(final String metricName, final Float value, final Aggregation aggregation,
                                      final AggregationFrequency aggregationFrequency) {
        return aggregatedPut(metricName, Float.toString(value), aggregation, aggregationFrequency);
    }

    @Override
    public final SenderFacade aggregatedPut(final String metricName, final Integer value, final Aggregation aggregation,
                                            final AggregationFrequency aggregationFrequency) {
        return aggregatedPut(metricName, Integer.toString(value), aggregation, aggregationFrequency);
    }

    @Override
    public final void put(
            final String name, final String value, final Tags tags, final Aggregations aggregations,
            final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace,
            final long timestamp
    ) {
        if (enabled) {
            try {
                metricsSender.put(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace, timestamp);
            } catch (Exception e) {
                LOGGER.warning("Unable to send metric: " + e.toString());
            }
        } else {
            LOGGER.fine("Statful client is disabled. The metric was not sent.");
        }
    }

    @Override
    public final void aggregatedPut(final String name, final String value, final Tags tags,
                                    final Aggregation aggregation, final AggregationFrequency aggregationFrequency,
                                    final Integer sampleRate, final String namespace, final long timestamp) {
        if (enabled) {
            try {
                metricsSender.aggregatedPut(name, value, tags, aggregation, aggregationFrequency, sampleRate, namespace,
                        timestamp);
            } catch (Exception e) {
                LOGGER.warning("Unable to send metric: " + e.toString());
            }
        } else {
            LOGGER.fine("Statful client is disabled. The metric was not sent.");
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

    @Override
    public void forceSyncFlush() {
        metricsSender.forceSyncFlush();
    }

    private SenderFacade put(final String metricName, final String value) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregationFrequency(configuration.getDefaultAggregationFreq())
                .name(metricName)
                .value(value);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade aggregatedPut(final String metricName, final String value, final Aggregation aggregation,
                                       final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this, true).with()
                .configuration(configuration)
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .name(metricName)
                .value(value);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade gauge(final String metricName, final String value) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this).with()
                .configuration(configuration)
                .aggregations(configuration.getGaugeAggregations())
                .aggregationFrequency(configuration.getGaugeAggregationFrequency())
                .tags(configuration.getGaugeTags())
                .name("gauge." + metricName)
                .value(value);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade aggregatedGauge(final String metricName, final String value, final Aggregation aggregation,
                                         final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this, true).with()
                .configuration(configuration)
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .tags(configuration.getGaugeTags())
                .name("gauge." + metricName)
                .value(value);

        return new StatfulClientFacade(metricsSenderAPI);
    }
}
