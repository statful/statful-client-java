package com.mindera.telemetron.aspects;

import com.mindera.telemetron.annotations.Timer;
import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import com.mindera.telemetron.client.api.TelemetronClient;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

/**
 * This aspect intercepts all {@link java.lang.annotation.ElementType} annotated with
 * {@link com.mindera.telemetron.annotations.Timer} (more to support ), creates a corresponding metric and sends it to
 * Telemetron client. An instance of a {@link com.mindera.telemetron.client.api.TelemetronClient} must be set.
 */
@SuppressFBWarnings(
        value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
        justification = "This is an AspectJ annotated class"
)
@Aspect
public class TelemetronAspect {

    private TelemetronClient telemetron;

    /**
     * Sets the Telemetron client instance that will handle the metrics.
     *
     * @param telemetron An instance of {@link com.mindera.telemetron.client.api.TelemetronClient}
     */
    public final void setTelemetronClient(final TelemetronClient telemetron) {
        this.telemetron = telemetron;
    }

    @Around("@annotation(timer)")
    public final Object methodTiming(final ProceedingJoinPoint joinPoint, final Timer timer) throws Throwable {
        Tags tags = new Tags();
        tags.merge(getTags(timer));

        long startTimer = startWatch();
        long stopTimer = startTimer;
        try {
            Object returnValue = joinPoint.proceed();

            stopTimer = stopWatch(startTimer);
            tags.putTag("status", "success");

            return returnValue;
        } catch (Throwable t) {
            stopTimer = stopWatch(startTimer);
            tags.putTag("status", "error");
            throw t;
        } finally {
            telemetron.timer(timer.name(), stopTimer).with()
                    .namespace(getNamespace(timer))
                    .aggregations(getAggregations(timer))
                    .tags(tags)
                    .send();
        }
    }

    private String getNamespace(final Timer timer) {
        String namespace = timer.namespace();

        if (namespace.isEmpty()) {
            namespace = null;
        }

        return namespace;
    }

    private Aggregations getAggregations(final Timer timer) {
        return Aggregations.from(timer.aggregations());
    }

    private Tags getTags(final Timer timer) {
        String[] tagsArray = timer.tags();
        return Tags.from(tagsArray);
    }

    private long startWatch() {
        return System.currentTimeMillis();
    }

    private long stopWatch(final long initialTime) {
        return System.currentTimeMillis() - initialTime;
    }
}
