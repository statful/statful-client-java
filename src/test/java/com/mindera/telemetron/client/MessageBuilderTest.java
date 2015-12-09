package com.mindera.telemetron.client;

import org.junit.Test;
import static org.hamcrest.CoreMatchers.*;

import java.util.HashMap;

import static org.junit.Assert.*;

public class MessageBuilderTest {

    private final String prefix = "TEST_PREF";
    private final String namespace = "TEST_NS";
    private final String type = "timer";
    private final String name = "response_time";
    private final long value = 3;
    private final HashMap<String, String> tags = new HashMap<String, String>();
    private final String[] aggr = new String[]{MetricsConfig.AGGREGATION_AVG, MetricsConfig.AGGREGATION_COUNT};
    private final int aggrFreq = 10;
    private final long timestamp = 121232323;

    private final MessageBuilder msgBuilder = new MessageBuilder();

    @Test
    public void testBuildMessageAllAttributes() throws Exception {
        tags.put("unit", "s");
        tags.put("app", "telemetron");
        String msg = msgBuilder.buildMessage(name, type, value, prefix, namespace, tags, aggr, aggrFreq, timestamp);
        assertThat(msg, anyOf(
                is("TEST_PREF.TEST_NS.timer.response_time,unit=s,app=telemetron 3 121232323 avg,count,10"),
                is("TEST_PREF.TEST_NS.timer.response_time,app=telemetron,unit=s 3 121232323 avg,count,10")));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildMessageNameNull() throws Exception {
        msgBuilder.buildMessage(null, type, value, prefix, namespace, tags, aggr, aggrFreq, timestamp);
    }

    @Test
    public void testBuildMessageTypeNull() throws Exception {
        tags.put("unit", "s");
        String msg = msgBuilder.buildMessage(name, null, value, prefix, namespace, tags, aggr, aggrFreq, timestamp);
        assertEquals(msg, ("TEST_PREF.TEST_NS.response_time,unit=s 3 121232323 avg,count,10"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildMessagePrefixNull() throws Exception {
        msgBuilder.buildMessage(name, type, value, null, namespace, tags, aggr, aggrFreq, timestamp);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBuildMessageNamespaceNull() throws Exception {
        msgBuilder.buildMessage(name, type, value, prefix, null, tags, aggr, aggrFreq, timestamp);
    }

    @Test
    public void testBuildMessageTagsNull() throws Exception {
        String msg = msgBuilder.buildMessage(name, type, value, prefix, namespace, null, aggr, aggrFreq, timestamp);
        assertEquals(msg, ("TEST_PREF.TEST_NS.timer.response_time 3 121232323 avg,count,10"));
    }

    @Test
    public void testBuildMessageTagsEmpty() throws Exception {
        String msg = msgBuilder.buildMessage(name, type, value, prefix, namespace, tags, aggr, aggrFreq, timestamp);
        assertEquals(msg, ("TEST_PREF.TEST_NS.timer.response_time 3 121232323 avg,count,10"));
    }

    @Test
    public void testBuildMessageNoAggregations() throws Exception {
        tags.put("unit", "s");
        String msg = msgBuilder.buildMessage(name, type, value, prefix, namespace, tags, null, 0, timestamp);
        assertEquals(msg, ("TEST_PREF.TEST_NS.timer.response_time,unit=s 3 121232323"));
    }
}