package com.mindera.telemetron.client.api;

import java.util.HashMap;
import java.util.Map;

/**
 * An holder for a collection of tags.
 */
public class Tags {

    private final Map<String, String> tags = new HashMap<String, String>();

    /**
     * Creates a new {@link com.mindera.telemetron.client.api.Tags} from a <code>type</code> and a <code>value</code>.
     *
     * @param type The type of the tag
     * @param value The value of the tag
     * @return A new instance of {@link com.mindera.telemetron.client.api.Tags}
     */
    public static Tags from(final String type, final String value) {
        Tags result = new Tags();
        result.putTag(type, value);
        return result;
    }

    /**
     * Copies a {@link com.mindera.telemetron.client.api.Tags} object.
     *
     * @param tags The {@link com.mindera.telemetron.client.api.Tags} to copy
     * @return A new instance of {@link com.mindera.telemetron.client.api.Tags}
     */
    public static Tags from(final Tags tags) {
        Tags result = new Tags();
        result.merge(tags);
        return result;
    }

    /**
     * Creates a new {@link com.mindera.telemetron.client.api.Tags} from an array of {@link java.lang.String}.
     * <p>
     * The array of {@link java.lang.String} should represent pairs of <code>type</code> and <code>value</code>.
     *
     * @param tagsArray An array of {@link java.lang.String}
     * @return A new instance of {@link com.mindera.telemetron.client.api.Tags}
     */
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

    /**
     * Puts a new tag into this object, represented by <code>type</code> and <code>value</code>.
     *
     * @param type The type of the tag to put
     * @param value The value of the tag to put
     */
    public final void putTag(final String type, final String value) {
        tags.put(type, value);
    }

    /**
     * Returns a {@link java.util.Map} representing a par of type and value of this object.
     *
     * @return A {@link java.util.Map} containing tags
     */
    public final Map<String, String> getTags() {
        return tags;
    }

    /**
     * Returns the tag value.
     *
     * @param type The type of the tag to fetch
     * @return A tag value
     */
    public final String getTagValue(final String type) {
        return tags.get(type);
    }

    /**
     * Merges a new {@link com.mindera.telemetron.client.api.Tags} into this object.
     *
     * @param tags The {@link com.mindera.telemetron.client.api.Tags} to merge
     * @return A reference to this object
     */
    public final Tags merge(final Tags tags) {
        if (tags != null) {
            this.tags.putAll(tags.getTags());
        }
        return this;
    }

    /**
     * Utility method to check if the tag type and values are valid.
     *
     * @param type The tag type
     * @param value The tag value
     * @return True if the tag components are valid
     */
    public static boolean isEmptyOrNull(final String type, final String value) {
        return isEmpty(type) || isEmpty(value);
    }

    private static boolean isEmpty(final String value) {
        return value == null || value.isEmpty();
    }
}
