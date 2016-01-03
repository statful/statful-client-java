package com.mindera.telemetron.client.api;

import java.util.Collection;
import java.util.EnumSet;

import static java.util.Objects.nonNull;

public class Aggregations {

    private final Collection<Aggregation> aggregations = EnumSet.noneOf(Aggregation.class);

    public static Aggregations from(final Aggregation... aggregations) {
        Aggregations result = new Aggregations();
        for (Aggregation aggregation : aggregations) {
            result.put(aggregation);
        }
        return result;
    }

    public final void put(final Aggregation aggregation) {
        aggregations.add(aggregation);
    }

    public final void putAll(final Collection<Aggregation> aggregations) {
        this.aggregations.addAll(aggregations);
    }

    public final Collection<Aggregation> getAggregations() {
        return aggregations;
    }

    public final Aggregations merge(final Aggregations aggregations) {
        if (nonNull(aggregations)) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }
}
