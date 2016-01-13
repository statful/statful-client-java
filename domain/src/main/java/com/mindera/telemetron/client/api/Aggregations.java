package com.mindera.telemetron.client.api;

import java.util.Collection;
import java.util.EnumSet;

/**
 * An holder for a collection of {@link com.mindera.telemetron.client.api.Aggregation}.
 */
public class Aggregations {

    private final Collection<Aggregation> aggregations = EnumSet.noneOf(Aggregation.class);

    /**
     * Creates a new {@link com.mindera.telemetron.client.api.Aggregations} from an array of
     * {@link com.mindera.telemetron.client.api.Aggregation}.
     *
     * @param aggregations An array of {@link com.mindera.telemetron.client.api.Aggregation}
     * @return An instance of {@link com.mindera.telemetron.client.api.Aggregations}
     */
    public static Aggregations from(final Aggregation... aggregations) {
        Aggregations result = new Aggregations();
        for (Aggregation aggregation : aggregations) {
            result.put(aggregation);
        }
        return result;
    }

    /**
     * Puts a new aggregation into this object.
     *
     * @param aggregation The {@link com.mindera.telemetron.client.api.Aggregation} to put
     */
    public final void put(final Aggregation aggregation) {
        aggregations.add(aggregation);
    }

    /**
     * Puts a {@link java.util.Collection} of {@link com.mindera.telemetron.client.api.Aggregation} into this object.
     *
     * @param aggregations The collection of {@link com.mindera.telemetron.client.api.Aggregation} to put
     */
    public final void putAll(final Collection<Aggregation> aggregations) {
        this.aggregations.addAll(aggregations);
    }

    /**
     * Getter for the aggregations.
     *
     * @return A {@link Collection} of {@link com.mindera.telemetron.client.api.Aggregation}
     */
    public final Collection<Aggregation> getAggregations() {
        return aggregations;
    }

    /**
     * Merges the {@link com.mindera.telemetron.client.api.Aggregations} into this object.
     *
     * @param aggregations The {@link com.mindera.telemetron.client.api.Aggregations} to merge
     * @return A reference to this object
     */
    public final Aggregations merge(final Aggregations aggregations) {
        if (aggregations != null) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }
}
