package com.mindera.telemetron.client.message;

import com.mindera.telemetron.client.api.Aggregation;
import com.mindera.telemetron.client.api.AggregationFreq;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mindera.telemetron.client.api.AggregationFreq.*;

/**
 * Telemetron message builder. This is an internal class to build messages from the metric data.
 * Message format follows the specification of Telemetron API.
 */
public final class MessageBuilder {

    private String prefix;
    private String namespace;
    private String name;
    private Map<String, String> tags = new HashMap<String, String>();
    private List<Aggregation> aggregations = new ArrayList<Aggregation>();
    private String value;
    private long timestamp;
    private AggregationFreq aggregationFreq = FREQ_10;

    private MessageBuilder() { }

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public MessageBuilder withPrefix(final String prefix) {
        this.prefix = prefix;
        return this;
    }

    public MessageBuilder withNamespace(final String namespace) {
        this.namespace = namespace;
        return this;
    }

    public MessageBuilder withName(final String name) {
        this.name = name;
        return this;
    }

    public MessageBuilder withTags(final Tags tags) {
        if (tags != null) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }

    public MessageBuilder withValue(final String value) {
        this.value = value;
        return this;
    }

    public MessageBuilder withAggregations(final Aggregations aggregations) {
        if (aggregations != null) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }

    public MessageBuilder withAggregationFreq(final AggregationFreq aggregationFreq) {
        if (aggregationFreq != null) {
            this.aggregationFreq = aggregationFreq;
        }
        return this;
    }

    public MessageBuilder withTimestamp(final long timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String build() {
        validate();

        StringBuilder sb = new StringBuilder();

        //append prefix and namespace
        sb.append(escapeMeasurement(prefix))
                .append(".")
                .append(escapeMeasurement(namespace));

        //append name
        sb.append(".").append(escapeMeasurement(name));

        //append tags
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            sb.append(",")
                    .append(escapeTag(entry.getKey()))
                    .append("=")
                    .append(escapeTag(entry.getValue()));
        }

        //append value
        sb.append(" ").append(value).append(" ");

        //append timestamp
        sb.append(timestamp);

        //append aggregation information
        if (!aggregations.isEmpty()) {
            sb.append(" ");
            for (Aggregation aggr : aggregations) {
                sb.append(aggr.getName()).append(",");
            }

            sb.append(aggregationFreq.getValue());
        }

        return sb.toString();
    }

    /**
     * This method adds an escape character in a tag type or tag value (<code>\</code>) before any of the following
     * characters: <code>\s</code>, <code>,</code> and <code>=</code>.
     *
     * @param string The tag key or tag value to escape
     * @return The escaped string
     */
    private static String escapeTag(final String string) {
        return string.replaceAll("[\\s,=]", "\\\\$0");
    }

    /**
     * This method adds an escape character in a measurement (<code>\</code>) before any of the following characters:
     * <code>\s</code> and <code>,</code>.
     *
     * @param string The measurement to escape
     * @return The escaped string
     */
    private static String escapeMeasurement(final String string) {
        return string.replaceAll("[\\s,]", "\\\\$0");
    }

    private void validate() {
        if (prefix == null) {
            throw new IllegalStateException("Prefix should not be null");
        }

        if (name == null) {
            throw new IllegalStateException("Name should not be null");
        }

        if (value == null) {
            throw new IllegalStateException("Value should not be null");
        }
    }
}
