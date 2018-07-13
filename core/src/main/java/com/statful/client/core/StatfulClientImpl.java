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
        SenderAPI metricsSenderAPI = createTimer(metricName, value, false, false)
                .aggregations(configuration.getTimerAggregations())
                .aggregationFrequency(configuration.getTimerAggregationFrequency());

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade counter(final String metricName) {
        return counter(metricName, 1);
    }

    @Override
    public final SenderFacade counter(final String metricName, final int value) {

        SenderAPI metricsSenderAPI = createCounter(metricName, value, false, false)
                .aggregations(configuration.getCounterAggregations())
                .aggregationFrequency(configuration.getCounterAggregationFrequency());

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
    public SenderFacade sampledTimer(final String metricName, final long value, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createTimer(metricName, value, false, true)
                .aggregations(configuration.getTimerAggregations())
                .aggregationFrequency(configuration.getTimerAggregationFrequency())
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledCounter(final String metricName, final Integer sampleRate) {
        return sampledCounter(metricName, 1, sampleRate);
    }

    @Override
    public SenderFacade sampledCounter(final String metricName, final int value, final Integer sampleRate) {

        SenderAPI metricsSenderAPI = createCounter(metricName, value, false, true)
                .aggregations(configuration.getCounterAggregations())
                .aggregationFrequency(configuration.getCounterAggregationFrequency())
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledGauge(final String metricName, final Long value, final Integer sampleRate) {
        return sampledGauge(metricName, value.toString(), sampleRate);
    }

    @Override
    public SenderFacade sampledGauge(final String metricName, final Double value, final Integer sampleRate) {
        return sampledGauge(metricName, value.toString(), sampleRate);
    }

    @Override
    public SenderFacade sampledGauge(final String metricName, final Float value, final Integer sampleRate) {
        return sampledGauge(metricName, value.toString(), sampleRate);
    }

    @Override
    public SenderFacade sampledGauge(final String metricName, final Integer value, final Integer sampleRate) {
        return sampledGauge(metricName, value.toString(), sampleRate);
    }

    @Override
    public SenderFacade sampledPut(final String metricName, final Long value, final Integer sampleRate) {
        return sampledPut(metricName, Long.toString(value), sampleRate);
    }

    @Override
    public SenderFacade sampledPut(final String metricName, final Double value, final Integer sampleRate) {
        return sampledPut(metricName, Double.toString(value), sampleRate);
    }

    @Override
    public SenderFacade sampledPut(final String metricName, final Float value, final Integer sampleRate) {
        return sampledPut(metricName, Float.toString(value), sampleRate);
    }

    @Override
    public SenderFacade sampledPut(final String metricName, final Integer value, final Integer sampleRate) {
        return sampledPut(metricName, value.toString(), sampleRate);
    }

    @Override
    public final SenderFacade aggregatedTimer(final String metricName, final long value, final Aggregation aggregation,
                                              final AggregationFrequency aggregationFrequency) {

        SenderAPI metricsSenderAPI = createTimer(metricName, value, true, false)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public final SenderFacade aggregatedCounter(final String metricName, final int value, final Aggregation aggregation,
                                                final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = MetricsSenderAPI.newInstance(this, true, false).with()
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
    public SenderFacade sampledAggregatedTimer(final String metricName, final long timestamp, final Aggregation aggregation,
                                               final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createTimer(metricName, timestamp, true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedCounter(final String metricName, final int value, final Aggregation aggregation,
                                                 final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createCounter(metricName, value, true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedGauge(final String metricName, final Long value, final Aggregation aggregation,
                                               final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value.toString(), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedGauge(final String metricName, final Double value, final Aggregation aggregation,
                                               final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value.toString(), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedGauge(final String metricName, final Float value, final Aggregation aggregation,
                                               final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value.toString(), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedGauge(final String metricName, final Integer value, final Aggregation aggregation,
                                               final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value.toString(), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedPut(final String metricName, final Long value, final Aggregation aggregation,
                                             final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createPut(metricName, Long.toString(value), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedPut(final String metricName, final Double value, final Aggregation aggregation,
                                             final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createPut(metricName, Double.toString(value), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedPut(final String metricName, final Float value, final Aggregation aggregation,
                                             final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createPut(metricName, Float.toString(value), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    @Override
    public SenderFacade sampledAggregatedPut(final String metricName, final Integer value, final Aggregation aggregation,
                                             final AggregationFrequency aggregationFrequency, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createPut(metricName, value.toString(), true, true)
                .aggregations(aggregation)
                .aggregationFrequency(aggregationFrequency)
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
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
    public void putSampled(final String name, final String value, final Tags tags, final Aggregations aggregations,
                           final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace, final long timestamp) {
        if (enabled) {
            try {
                metricsSender.putSampled(name, value, tags, aggregations, aggregationFrequency, sampleRate, namespace, timestamp);
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
                metricsSender.aggregatedPut(name, value, tags, aggregation, aggregationFrequency, sampleRate, namespace, timestamp);
            } catch (Exception e) {
                LOGGER.warning("Unable to send metric: " + e.toString());
            }
        } else {
            LOGGER.fine("Statful client is disabled. The metric was not sent.");
        }
    }

    @Override
    public void aggregatedSampledPut(final String name, final String value, final Tags tags, final Aggregation aggregation,
                                     final AggregationFrequency aggregationFrequency, final Integer sampleRate, final String namespace, final long timestamp) {

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
        SenderAPI metricsSenderAPI = createPut(metricName, value, false, false)
                .aggregationFrequency(configuration.getDefaultAggregationFreq());

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade aggregatedPut(final String metricName, final String value, final Aggregation aggregation,
                                       final AggregationFrequency aggregationFrequency) {

        SenderAPI metricsSenderAPI = createPut(metricName, value, true, false)
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade sampledPut(final String metricName, final String value, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createPut(metricName, value, false, true)
                .aggregationFrequency(configuration.getDefaultAggregationFreq())
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderAPI createPut(final String metricName, final String value, final boolean isAggregated,
                                final boolean isSampled) {
        return MetricsSenderAPI.newInstance(this, isAggregated, isSampled).with()
                .configuration(configuration)
                .name(metricName)
                .value(value);

    }

    private SenderFacade gauge(final String metricName, final String value) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value, false, false).with()
                .aggregations(configuration.getGaugeAggregations())
                .aggregationFrequency(configuration.getGaugeAggregationFrequency());

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade aggregatedGauge(final String metricName, final String value, final Aggregation aggregation,
                                         final AggregationFrequency aggregationFrequency) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value, true, false).with()
                .aggregation(aggregation)
                .aggregationFrequency(aggregationFrequency);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderFacade sampledGauge(final String metricName, final String value, final Integer sampleRate) {
        SenderAPI metricsSenderAPI = createGauge(metricName, value, true, false).with()
                .aggregations(configuration.getGaugeAggregations())
                .aggregationFrequency(configuration.getGaugeAggregationFrequency())
                .sampleRate(sampleRate);

        return new StatfulClientFacade(metricsSenderAPI);
    }

    private SenderAPI createGauge(final String metricName, final String value, final boolean isAggregated, final boolean isSampled) {
        return MetricsSenderAPI.newInstance(this, isAggregated, isSampled).with()
                .configuration(configuration)
                .tags(configuration.getGaugeTags())
                .name("gauge." + metricName)
                .value(value);
    }

    private SenderAPI createTimer(final String metricName, final long value, final boolean isAggregated, final boolean isSampled) {
        return MetricsSenderAPI.newInstance(this, isAggregated, isSampled).with()
                .configuration(configuration)
                .tags(configuration.getTimerTags())
                .name("timer." + metricName)
                .value(Long.toString(value));
    }

    private SenderAPI createCounter(final String metricName, final int value, final boolean isAggregated, final boolean isSampled) {
        return MetricsSenderAPI.newInstance(this, isAggregated, isSampled).with()
                .configuration(configuration)
                .tags(configuration.getCounterTags())
                .name("counter." + metricName)
                .value(Integer.toString(value));
    }
}
