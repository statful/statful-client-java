package io.statful.client.core.api;

/**
 * This is an holder for tag data (type and value) to aid building a metric.
 */
public final class TagBuilder {
    private final String type;
    private final String value;

    /**
     * Default constructor.
     *
     * @param type The type of the metric
     * @param value The value of the metric
     */
    TagBuilder(final String type, final String value) {
        this.type = type;
        this.value = value;
    }

    /**
     * A getter for the tag type.
     *
     * @return The type of the tag
     */
    String getType() {
        return type;
    }

    /**
     * A getter for the tag value.
     *
     * @return The value of the tag
     */
    String getValue() {
        return value;
    }
}
