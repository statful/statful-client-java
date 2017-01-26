package com.statful.client.annotations;

import com.statful.client.domain.api.Aggregation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * An annotation for measuring the timer of a method.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Timer {
    /**
     * @return The timer's name
     */
    String name();

    /**
     * @return The namespace of the metric
     */
    String namespace() default "";

    /**
     * Sets the tags that should be sent to Statful. The cardinality should always be even because each pair of two
     * represents the type and the value of the tag.
     *
     * Example:
     * {@literal @}Timer(name = "timerName", tags = { "env", "prod", "method" = "getIt" })
     *
     * @return An array of tags
     */
    String[] tags() default {};

    /**
     * @return An array of aggregations to send to Statful
     */
    Aggregation[] aggregations() default {};

    /**
     * @return An int of the sampleRate to send to Statful
     */
    int sampleRate();
}
