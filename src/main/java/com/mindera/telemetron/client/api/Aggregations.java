package com.mindera.telemetron.client.api;

import java.util.Collection;
import java.util.EnumSet;

import static java.util.Objects.nonNull;

public class Aggregations {

    private final Collection<Aggregation> aggregations = EnumSet.noneOf(Aggregation.class);

    public static Aggregations from(Aggregation... aggregations) {
        Aggregations result = new Aggregations();
        for (Aggregation aggregation : aggregations) {
            result.put(aggregation);
        }
        return result;
    }

    public void put(Aggregation aggregation) {
        aggregations.add(aggregation);
    }

    public void putAll(Collection<Aggregation> aggregations) {
        this.aggregations.addAll(aggregations);
    }

    public Collection<Aggregation> getAggregations() {
        return aggregations;
    }

    public Aggregations merge(Aggregations aggregations) {
        if (nonNull(aggregations)) {
            this.aggregations.addAll(aggregations.getAggregations());
        }
        return this;
    }
}
