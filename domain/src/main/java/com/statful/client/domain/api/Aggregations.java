package com.statful.client.domain.api;

import java.util.Collection;
import java.util.EnumSet;

/**
 * An holder for a collection of {@link Aggregation}.
 */
public class Aggregations {

    private final Collection<Aggregation> aggregations = EnumSet.noneOf(Aggregation.class);

    /**
     * Creates a new {@link Aggregations} from an array of
     * {@link Aggregation}.
     *
     * @param aggregations An array of {@link Aggregation}
     * @return An instance of {@link Aggregations}
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
     * @param aggregation The {@link Aggregation} to put
     */
    public final void put(final Aggregation aggregation) {
        aggregations.add(aggregation);
    }

    /**
     * Puts a {@link java.util.Collection} of {@link Aggregation} into this object.
     *
     * @param aggregations The collection of {@link Aggregation} to put
     */
    public final void putAll(final Collection<Aggregation> aggregations) {
        this.aggregations.addAll(aggregations);
    }

    /**
     * Getter for the aggregations.
     *
     * @return A {@link Collection} of {@link Aggregation}
     */
    public final Collection<Aggregation> getAggregations() {
        return aggregations;
    }

    /**
     * Merges the {@link Aggregations} into this object.
     *
     * @param aggregations The {@link Aggregations} to merge
     * @return A reference to this object
     */
    public final Aggregations merge(final Aggregations aggregations) {
        if (aggregations != null) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }
}
