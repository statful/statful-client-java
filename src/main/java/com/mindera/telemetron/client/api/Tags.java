package com.mindera.telemetron.client.api;

import java.util.HashMap;
import java.util.Map;

public class Tags {

    private final Map<String, String> tags = new HashMap<String, String>();

    public static Tags from(final String type, final String value) {
        Tags result = new Tags();
        result.putTag(type, value);
        return result;
    }

    public static Tags from(final Tags tags) {
        Tags result = new Tags();
        result.merge(tags);
        return result;
    }

    public static Tags from(final String[] tagsArray) {
        if (!canGroupByTwo(tagsArray.length)) {
            return null;
        } else {
            return buildTagFrom(tagsArray);
        }
    }

    private static boolean canGroupByTwo(final int length) {
        return length > 0 && length % 2 == 0;
    }

    private static Tags buildTagFrom(final String[] tagsArray) {
        Tags tags = new Tags();
        for (int i = 0; i < tagsArray.length - 1; i += 2) {
            tags.putTag(tagsArray[i], tagsArray[i + 1]);
        }
        return tags;
    }

    public final void putTag(final String type, final String value) {
        tags.put(type, value);
    }

    public final Map<String, String> getTags() {
        return tags;
    }

    public final String getTagValue(final String type) {
        return tags.get(type);
    }

    public final Tags merge(final Tags tags) {
        if (tags != null) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }

    public static boolean isEmptyOrNull(final String type, final String value) {
        return isEmpty(type) || isEmpty(value);
    }

    private static boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }
}
