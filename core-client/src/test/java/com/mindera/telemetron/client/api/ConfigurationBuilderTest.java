package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import org.junit.Test;

import java.util.Collection;

import static com.mindera.telemetron.client.api.Aggregation.*;
import static com.mindera.telemetron.client.api.AggregationFreq.*;
import static com.mindera.telemetron.client.api.ConfigurationBuilder.*;
import static com.mindera.telemetron.client.api.MetricBuilder.*;
import static com.mindera.telemetron.client.api.Transport.UDP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class ConfigurationBuilderTest {

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIfPrefixIsNotSpecified() {
        newBuilder()
                .transport(UDP)
                .buildConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIfTransportIsNotSpecified() {
        newBuilder()
                .prefix("test_prefix")
                .buildConfiguration();
    }

    @Test
    public void shouldUseDefaultValuesWhenNoneAreSpecified() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .buildConfiguration();

        assertEquals("Should have default host", "127.0.0.1", config.getHost());
        assertEquals("Should have default port", 2013, config.getPort());
        assertFalse("Should not dry run as default", config.isDryRun());
        assertEquals("Should have default sample rate", 100, config.getSampleRate());
        assertEquals("Should have default namespace", "application", config.getNamespace());
        assertEquals("Should have default flush size", 10, config.getFlushSize());
        assertEquals("Should have default flush interval", 0, config.getFlushIntervalMillis());
    }

    @Test
    public void shouldUseTimerDefaultValues() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag in ms", "ms", tags.getTagValue("unit"));

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Timer should have default aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT));
        assertEquals("Should have default timer aggregation frequency", FREQ_10, config.getTimerAggregationFreq());
    }

    @Test
    public void shouldUseCounterDefaultValues() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .buildConfiguration();

        assertNull("Should not have default counter tags", config.getCounterTags());

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should have default counter aggregations", aggregations, containsInAnyOrder(AVG, P90));
        assertEquals("Should have default counter aggregation frequency", FREQ_10, config.getCounterAggregationFreq());
    }

    @Test
    public void shouldUseGaugeDefaultValues() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .buildConfiguration();

        assertNull("Should not have default gauge tags", config.getGaugeTags());

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should have default gauge aggregations", aggregations, containsInAnyOrder(LAST));
        assertEquals("Should have default gauge aggregation frequency", FREQ_10, config.getGaugeAggregationFreq());
    }

    @Test
    public void shouldConfigureUDPTransport() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .host("localhost")
                .port(6167)
                .transport(UDP)
                .buildConfiguration();

        assertEquals("Should configure host", "localhost", config.getHost());
        assertEquals("Should configure port", 6167, config.getPort());
        assertEquals("Should configure transport", UDP, config.getTransport());
    }

    @Test
    public void shouldConfigurePrefix() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .buildConfiguration();

        assertEquals("Should configure prefix", "test_prefix", config.getPrefix());
    }

    @Test
    public void shouldConfigureToken() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .token("myToken")
                .buildConfiguration();

        assertEquals("Should configure token", "myToken", config.getToken());
    }

    @Test
    public void shouldConfigureApp() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .app("myApp")
                .buildConfiguration();

        assertEquals("Should configure app name", "myApp", config.getApp());
    }

    @Test
    public void shouldConfigureDryRun() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .isDryRun(true)
                .buildConfiguration();

        assertTrue("Should configure dry run", config.isDryRun());
    }

    @Test
    public void shouldConfigureWorkersPool() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .workerPoolSize(20)
                .buildConfiguration();

        assertEquals("Should configure pool size", 20, config.getWorkersPoolSize());
    }

    @Test
    public void shouldConfigureDefaultTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .tag("host", "localhost")
                .tag("cluster", "prod")
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Should configure 'host' tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should configure 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldConfigureSampleRate() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .sampleRate(50)
                .buildConfiguration();

        assertEquals("Should configure sample rate", 50, config.getSampleRate());
    }

    @Test
    public void shouldConfigureNamespace() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .namespace("my namespace")
                .buildConfiguration();

        assertEquals("Should configure namespace", "my namespace", config.getNamespace());
    }

    @Test
    public void shouldConfigureFlushSize() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .flushSize(1000)
                .buildConfiguration();

        assertEquals("Should configure flush size", 1000, config.getFlushSize());
    }

    @Test
    public void shouldConfigureFlushInterval() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .flushInterval(500)
                .buildConfiguration();

        assertEquals("Should configure flush interval", 500, config.getFlushIntervalMillis());
    }

    @Test
    public void shouldConfigureTimerTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .timer(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag", "ms", tags.getTagValue("unit"));
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeTimerTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .app("web")
                .tag("host", "localhost")
                .timer(tag("unit", "ns"))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Timer should have 'unit' tag", "ns", tags.getTagValue("unit"));
        assertEquals("Timer should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureTimerAggregations() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .timer(agg(LAST))
                .timer(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, LAST));
        assertEquals("Should timer aggregation frequency", FREQ_120, config.getTimerAggregationFreq());
    }

    @Test
    public void shouldConfigureCounterTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .counter(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getCounterTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeCounterTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .app("web")
                .tag("host", "localhost")
                .counter(tag("unit", "ns"))
                .buildConfiguration();

        Tags tags = config.getCounterTags();
        assertEquals("Timer should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Timer should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureCounterAggregations() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .counter(agg(LAST))
                .counter(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(AVG, P90, LAST));
        assertEquals("Should counter aggregation frequency", FREQ_120, config.getCounterAggregationFreq());
    }

    @Test
    public void shouldConfigureGaugeTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .gauge(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getGaugeTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeGaugeTags() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .app("web")
                .tag("host", "localhost")
                .gauge(tag("unit", "ns"))
                .buildConfiguration();

        Tags tags = config.getGaugeTags();
        assertEquals("Gauge should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Gauge should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureGaugeAggregations() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .gauge(agg(P90))
                .gauge(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should configure gauge aggregations", aggregations, containsInAnyOrder(LAST, P90));
        assertEquals("Should gauge aggregation frequency", FREQ_120, config.getGaugeAggregationFreq());
    }

    @Test
    public void shouldConfigureTimerAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .timer(agg(LAST))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, LAST));
    }

    @Test
    public void shouldConfigureCounterAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .counter(agg(LAST))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(AVG, P90, LAST));
    }

    @Test
    public void shouldConfigureGaugeAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .gauge(agg(P90))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should configure gauge aggregations", aggregations, containsInAnyOrder(LAST, P90));
    }

    @Test
    public void shouldChainBuilder() {
        class TestClass {
            final ClientConfiguration configuration;

            TestClass(ClientConfiguration configuration) {
                this.configuration = configuration;
            }
        }

        TestClass subject = newBuilder(new ConfigurationBuilderChain<TestClass>() {
            @Override
            public TestClass build(ClientConfiguration configuration) {
                return new TestClass(configuration);
            }
        }).prefix("test_prefix").transport(UDP).build();

        assertEquals("Should build tags prefix configuration", "test_prefix", subject.configuration.getPrefix());
        assertEquals("Should build tags transport configuration", UDP, subject.configuration.getTransport());
    }

    @Test
    public void shouldIgnoreNullBuilderAttributes() {
        ClientConfiguration config = newBuilder()
                .prefix("test_prefix")
                .transport(UDP)
                .token("")
                .namespace("")
                .app("")
                .host(null)
                .tag(null, null)
                .timer((AggregationBuilder[]) null)
                .timer((TagBuilder[]) null)
                .timer((AggregationFreqBuilder) null)
                .counter((AggregationBuilder[]) null)
                .counter((TagBuilder[]) null)
                .counter((AggregationFreqBuilder) null)
                .gauge((AggregationBuilder[]) null)
                .gauge((TagBuilder[]) null)
                .gauge((AggregationFreqBuilder) null)
                .buildConfiguration();

        assertEquals("Should have default application tags", 1, config.getApplicationTags().getTags().size());

        assertEquals("Should have default timer tags", 1, config.getTimerTags().getTags().size());
        assertEquals("Should have default timer aggregations", 3, config.getTimerAggregations().getAggregations().size());
        assertEquals("Should have default timer aggregation frequency", FREQ_10, config.getTimerAggregationFreq());

        assertNull("Should not have counter tags", config.getCounterTags());
        assertEquals("Should have default counter aggregations", 2, config.getCounterAggregations().getAggregations().size());
        assertEquals("Should have default counter aggregation frequency", FREQ_10, config.getCounterAggregationFreq());

        assertNull("Should not have gauge tags", config.getGaugeTags());
        assertEquals("Should have default gauge aggregations", 1, config.getGaugeAggregations().getAggregations().size());
        assertEquals("Should have default gauge aggregation frequency", FREQ_10, config.getGaugeAggregationFreq());
    }
}