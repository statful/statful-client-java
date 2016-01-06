package com.mindera.telemetron.client.message;

import com.mindera.telemetron.client.api.Aggregations;
import com.mindera.telemetron.client.api.Tags;
import org.junit.Test;

import static com.mindera.telemetron.client.api.Aggregation.AVG;
import static com.mindera.telemetron.client.api.Aggregation.COUNT;
import static com.mindera.telemetron.client.api.AggregationFreq.FREQ_10;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class MessageBuilderTest {

    private final static String PREFIX = "TEST_PREF";
    private final static String NAMESPACE = "TEST_NS";
    private final static String NAME = "response_time";
    private final static Tags TAGS = new Tags();
    private final static Aggregations AGGREGATIONS = new Aggregations();
    private final static long TIMESTAMP = 121232323;

    static {
        TAGS.putTag("unit", "s");
        TAGS.putTag("app", "telemetron");

        AGGREGATIONS.put(AVG);
        AGGREGATIONS.put(COUNT);
    }

    @Test
    public void shouldBuildMessageWithAllAttributes() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertThat(message, anyOf(
                is("TEST_PREF.TEST_NS.response_time,unit=s,app=telemetron 3 121232323 avg,count,10"),
                is("TEST_PREF.TEST_NS.response_time,app=telemetron,unit=s 3 121232323 avg,count,10")));
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildMessageWithoutName() throws Exception {
        MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildMessageWithoutPrefix() throws Exception {
        MessageBuilder.newBuilder()
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotBuildMessageWithoutValue() throws Exception {
        MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withTags(TAGS)
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();
    }

    @Test
    public void shouldBuildMessageWithoutTags() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertEquals(message, ("TEST_PREF.TEST_NS.response_time 3 121232323 avg,count,10"));
    }

    @Test
    public void shouldBuildMessageWithEmptyTags() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withTags(new Tags())
                .withValue("3")
                .withAggregations(AGGREGATIONS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertEquals(message, ("TEST_PREF.TEST_NS.response_time 3 121232323 avg,count,10"));
    }

    @Test
    public void shouldBuildMessageWithoutAggregations() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertThat(message, anyOf(
                is("TEST_PREF.TEST_NS.response_time,unit=s,app=telemetron 3 121232323"),
                is("TEST_PREF.TEST_NS.response_time,app=telemetron,unit=s 3 121232323")));
    }

    @Test
    public void shouldBuildMessageWithEmptyAggregations() throws Exception {
        String message = MessageBuilder.newBuilder()
                .withPrefix(PREFIX)
                .withNamespace(NAMESPACE)
                .withName(NAME)
                .withValue("3")
                .withTags(TAGS)
                .withAggregations(new Aggregations())
                .withAggregationFreq(FREQ_10)
                .withTimestamp(TIMESTAMP)
                .build();

        assertThat(message, anyOf(
                is("TEST_PREF.TEST_NS.response_time,unit=s,app=telemetron 3 121232323"),
                is("TEST_PREF.TEST_NS.response_time,app=telemetron,unit=s 3 121232323")));
    }
}