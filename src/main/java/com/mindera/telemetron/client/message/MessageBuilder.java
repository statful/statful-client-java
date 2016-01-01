package com.mindera.telemetron.client.message;

import com.mindera.telemetron.client.api.Aggregation;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Telemetron message builder. This is an internal class to build messages from the metric data.<br/>
 * Message format follows the specification of Telemetron API.
 */
public class MessageBuilder {

    private String prefix;
    private String namespace;
    private String name;
    private Map<String, String> tags = new HashMap<>();
    private List<Aggregation> aggregations = new ArrayList<>();
    private String value;
    private String timestamp;
    private int aggregationFreq;

    private MessageBuilder() {}

    public static MessageBuilder newBuilder() {
        return new MessageBuilder();
    }

    public MessageBuilder withPrefix(String prefix) {
        this.prefix = prefix;
        return this;
    }

    public MessageBuilder withNamespace(String namespace) {
        this.namespace = namespace;
        return this;
    }

    public MessageBuilder withName(String name) {
        this.name = name;
        return this;
    }

    public MessageBuilder withTags(Tags tags) {
        if (tags != null) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }

    public MessageBuilder withValue(String value) {
        this.value = value;
        return this;
    }

    public MessageBuilder withAggregations(Aggregations aggregations) {
        if (aggregations != null) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }

    public MessageBuilder withAggregationFreq(int aggregationFreq) {
        this.aggregationFreq = aggregationFreq;
        return this;
    }

    public MessageBuilder withTimestamp(String timestamp) {
        this.timestamp = timestamp;
        return this;
    }

    public String build() {
        validate();

        StringBuilder sb = new StringBuilder();

        //append prefix and namespace
        sb.append(prefix).append(".").append(namespace);

        //append name
        sb.append(".").append(name);

        //append tags
        for (Map.Entry<String, String> entry : tags.entrySet()) {
            sb.append(",").append(entry.getKey()).append("=").append(entry.getValue());
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
            sb.append(aggregationFreq);
        }

        return sb.toString();
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
