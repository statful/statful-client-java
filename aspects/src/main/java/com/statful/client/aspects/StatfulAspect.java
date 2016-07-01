package com.statful.client.aspects;

import com.statful.client.annotations.Timer;
import com.statful.client.domain.api.Aggregations;
import com.statful.client.domain.api.Tags;
import com.statful.client.domain.api.StatfulClient;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.logging.Logger;

/**
 * This aspect intercepts all {@link java.lang.annotation.ElementType} annotated with
 * {@link com.statful.client.annotations.Timer} (more to support ), creates a corresponding metric and sends it to
 * Statful client. An instance of a {@link com.statful.client.domain.api.StatfulClient} must be set.
 */
@SuppressFBWarnings(
        value = "UWF_FIELD_NOT_INITIALIZED_IN_CONSTRUCTOR",
        justification = "This is an AspectJ annotated class"
)
@Aspect
public class StatfulAspect {

    private static final Logger LOGGER = Logger.getLogger(StatfulAspect.class.getName());

    private StatfulClient statful;

    /**
     * Sets the Statful client instance that will handle the metrics.
     *
     * @param statful An instance of {@link com.statful.client.domain.api.StatfulClient}
     */
    public final void setStatfulClient(final StatfulClient statful) {
        this.statful = statful;
    }

    /**
     * Invoked by aspectj.
     *
     * @param joinPoint The joinPoint of the called method
     * @param timer The annotation containing the timer configured on the invoked method
     * @return The result of the invoked method
     * @throws Throwable Eventually thrown by the invoked method
     */
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
            if (statful != null) {
                statful.timer(timer.name(), stopTimer).with()
                        .namespace(getNamespace(timer))
                        .aggregations(getAggregations(timer))
                        .tags(tags)
                        .send();
            } else {
                LOGGER.warning("Statful client is not configured");
            }
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
