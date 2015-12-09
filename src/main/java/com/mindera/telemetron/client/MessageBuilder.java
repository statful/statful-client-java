package com.mindera.telemetron.client;

import java.util.HashMap;
import java.util.Map;

/**
 * Telemetron message builder. This is an internal class to build messages from the metric data.<br/>
 * Message format follows the specification of Telemetron API.
 */
class MessageBuilder {

    String buildMessage(String name, long value,
                               TelemetronConfig clientConfig,
                               MetricsConfig config) {
        HashMap<String, String> tags = getMessageTags(clientConfig.getTags(), config.getTags());
        String metricType = config != null ? config.getMetricType() : null;

        return this.buildMessage(name, metricType, value, clientConfig.getPrefix(), config.getNamespace(), tags,
                config.getAggregation(), config.getAggregationFreq(), config.getTimestamp());
    }

    HashMap<String, String> getMessageTags(HashMap<String, String> clientTags, HashMap<String, String> metricTags) {
        HashMap<String, String> tags = new HashMap<String, String>(clientTags);
        tags.putAll(metricTags);
        return tags;
    }

    String buildMessage(String name, String type, long value, String prefix, String namespace,
                               HashMap<String, String> tags, String[] aggregations, int aggrFrequency, long timestamp) {
        if (name == null) {
            throw new IllegalArgumentException("name is required");
        }
        if (prefix == null) {
            throw new IllegalArgumentException("prefix is required");
        }
        if (namespace == null) {
            throw new IllegalArgumentException("namespace is required");
        }

        StringBuilder sb = new StringBuilder();

        //append prefix and namespace
        sb.append(prefix)
                .append(".")
                .append(namespace);

        //append type if defined
        if (type != null) {
            sb.append(".").append(type);
        }

        //append name
        sb.append(".").append(name);

        //append tags
        if (tags != null) {
            for (Map.Entry<String, String> entry : tags.entrySet()) {
                sb.append(",").append(entry.getKey()).append("=").append(entry.getValue());
            }
        }

        //append value
        sb.append(" ").append(value).append(" ");

        //append timestamp
        sb.append(timestamp);

        //append aggregation information
        if (aggregations != null) {
            sb.append(" ");
            for (String aggr : aggregations) {
                sb.append(aggr).append(",");
            }
            sb.append(aggrFrequency);
        }

        return sb.toString();
    }


}
