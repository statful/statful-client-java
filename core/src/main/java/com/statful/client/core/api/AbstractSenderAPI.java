package com.statful.client.core.api;

import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.SenderAPI;
import com.statful.client.domain.api.Tags;

/**
 * This class is an implementation of the {@link SenderAPI}.
 */
public abstract class AbstractSenderAPI implements SenderAPI {

    private static final long TIMESTAMP_DIVIDER = 1000L;

    private String metricName;
    private String value;
    private String namespace;
    private Tags tags;
    private Integer sampleRate;

    @Override
    public final SenderAPI metricName(final String metricName) {
        if (isStringSafe(metricName)) {
            this.metricName = metricName;
        }
        return this;
    }

    @Override
    public final SenderAPI value(final String value) {
        if (isStringSafe(value)) {
            this.value = value;
        }
        return this;
    }

    @Override
    public final SenderAPI configuration(final ClientConfiguration configuration) {
        if (configuration != null) {
            this.withNamespace(configuration.getNamespace()).sampleRate(configuration.getSampleRate());
        }
        return this;
    }

    @Override
    public final SenderAPI sampleRate(final Integer sampleRate) {
        if (sampleRate != null) {
            this.sampleRate = sampleRate;
        }
        return this;
    }

    @Override
    public final SenderAPI tag(final String type, final String value) {
        if (!Tags.isEmptyOrNull(type, value)) {
            getSafeTags().putTag(type, value);
        }
        return this;
    }

    @Override
    public final SenderAPI tags(final Tags tags) {
        if (tags != null) {
            getSafeTags().merge(tags);
        }
        return this;
    }

    @Override
    public final SenderAPI namespace(final String namespace) {
        withNamespace(namespace);
        return this;
    }

    protected final SenderAPI withNamespace(final String namespace) {
        if (isStringSafe(namespace)) {
            this.namespace = namespace;
        }
        return this;
    }

    protected final boolean isValid() {
        return isStringSafe(metricName) && isStringSafe(value);
    }

    protected final long getUnixTimestamp() {
        return System.currentTimeMillis() / TIMESTAMP_DIVIDER;
    }

    protected final Tags getSafeTags() {
        if (tags == null) {
            tags = new Tags();
        }
        return tags;
    }

    protected final boolean isStringSafe(final String string) {
        return string != null && !string.isEmpty();
    }

    /**
     * A getter for the metric name.
     *
     * @return The metric name
     */
    protected final String getMetricName() {
        return metricName;
    }

    /**
     * A getter for the metric value.
     *
     * @return The value of the metric
     */
    protected final String getValue() {
        return value;
    }

    /**
     * A getter for the namespace.
     *
     * @return The namespace of the metric
     */
    protected final String getNamespace() {
        return namespace;
    }

    /**
     * A getter for the {@link Tags} of the metric.
     *
     * @return The {@link Tags} of the metric
     */
    protected final Tags getTags() {
        return tags;
    }

    /**
     * A getter for the sample rate.
     *
     * @return The sample rate
     */
    protected final Integer getSampleRate() {
        return sampleRate;
    }
}
