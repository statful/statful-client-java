package com.mindera.telemetron.client.api;

import java.util.HashMap;
import java.util.Map;

import static java.util.Objects.nonNull;

public class Tags {

    private final Map<String, String> tags = new HashMap<String, String>();

    public static Tags from(String type, String name) {
        Tags result = new Tags();
        result.putTag(type, name);
        return result;
    }

    public static Tags from(Tags tags) {
        Tags result = new Tags();
        result.merge(tags);
        return result;
    }

    public void putTag(String type, String value) {
        tags.put(type, value);
    }

    public Map<String, String> getTags() {
        return tags;
    }

    public String getTagValue(String type) {
        return tags.get(type);
    }

    public Tags merge(Tags tags) {
        if (nonNull(tags)) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }
}
