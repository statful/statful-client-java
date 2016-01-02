package com.mindera.telemetron.client.api;

import com.mindera.telemetron.client.config.ClientConfiguration;
import org.junit.Test;

import java.util.Collection;

import static com.mindera.telemetron.client.api.Aggregation.*;
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
                .with(transport(UDP))
                .buildConfiguration();
    }

    @Test(expected = IllegalStateException.class)
    public void shouldThrowIfTransportIsNotSpecified() {
        newBuilder()
                .with(prefix("test_prefix"))
                .buildConfiguration();
    }

    @Test
    public void shouldUseDefaultValuesWhenNoneAreSpecified() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
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
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag in ms", "ms", tags.getTagValue("unit"));

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Timer should have default aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, COUNT_PS));
        assertEquals("Should have default timer aggregation frequency", 10, config.getTimerAggregationFreq());
    }

    @Test
    public void shouldUseCounterDefaultValues() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .buildConfiguration();

        assertNull("Should not have default counter tags", config.getCounterTags());

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should have default counter aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT_PS));
        assertEquals("Should have default counter aggregation frequency", 10, config.getCounterAggregationFreq());
    }

    @Test
    public void shouldUseGaugeDefaultValues() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .buildConfiguration();

        assertNull("Should not have default gauge tags", config.getGaugeTags());

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should have default gauge aggregations", aggregations, containsInAnyOrder(LAST));
        assertEquals("Should have default gauge aggregation frequency", 10, config.getGaugeAggregationFreq());
    }

    @Test
    public void shouldConfigureUDPTransport() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(host("localhost"))
                .with(port(6167))
                .with(transport(UDP))
                .buildConfiguration();

        assertEquals("Should configure host", "localhost", config.getHost());
        assertEquals("Should configure port", 6167, config.getPort());
        assertEquals("Should configure transport", UDP, config.getTransport());
    }

    @Test
    public void shouldConfigurePrefix() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .buildConfiguration();

        assertEquals("Should configure prefix", "test_prefix", config.getPrefix());
    }

    @Test
    public void shouldConfigureToken() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(token("myToken"))
                .buildConfiguration();

        assertEquals("Should configure token", "myToken", config.getToken());
    }

    @Test
    public void shouldConfigureApp() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(app("myApp"))
                .buildConfiguration();

        assertEquals("Should configure app name", "myApp", config.getApp());
    }

    @Test
    public void shouldConfigureDryRun() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(dryRun(true))
                .buildConfiguration();

        assertTrue("Should configure token", config.isDryRun());
    }

    @Test
    public void shouldConfigureDefaultTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(tag("host", "localhost"), tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Should configure 'host' tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should configure 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldConfigureSampleRate() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(sampleRate(50))
                .buildConfiguration();

        assertEquals("Should configure sample rate", 50, config.getSampleRate());
    }

    @Test
    public void shouldConfigureNamespace() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(namespace("my namespace"))
                .buildConfiguration();

        assertEquals("Should configure namespace", "my namespace", config.getNamespace());
    }

    @Test
    public void shouldConfigureFlushSize() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(flushSize(1000))
                .buildConfiguration();

        assertEquals("Should configure flush size", 1000, config.getFlushSize());
    }

    @Test
    public void shouldConfigureFlushInterval() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(flushInterval(500))
                .buildConfiguration();

        assertEquals("Should configure flush interval", 500, config.getFlushIntervalMillis());
    }

    @Test
    public void shouldConfigureTimerTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(timer(tag("cluster", "prod")))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag", "ms", tags.getTagValue("unit"));
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeTimerTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(app("web"))
                .with(tag("host", "localhost"))
                .with(timer(tag("unit", "ns")))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Timer should have 'unit' tag", "ns", tags.getTagValue("unit"));
        assertEquals("Timer should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureTimerAggregations() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(timer(LAST))
                .with(timer(aggrFreq(100)))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, COUNT_PS, LAST));
        assertEquals("Should timer aggregation frequency", 100, config.getTimerAggregationFreq());
    }

    @Test
    public void shouldConfigureCounterTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(counter(tag("cluster", "prod")))
                .buildConfiguration();

        Tags tags = config.getCounterTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeCounterTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(app("web"))
                .with(tag("host", "localhost"))
                .with(counter(tag("unit", "ns")))
                .buildConfiguration();

        Tags tags = config.getCounterTags();
        assertEquals("Timer should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Timer should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureCounterAggregations() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(counter(LAST))
                .with(counter(aggrFreq(100)))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT_PS, LAST));
        assertEquals("Should counter aggregation frequency", 100, config.getCounterAggregationFreq());
    }

    @Test
    public void shouldConfigureGaugeTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(gauge(tag("cluster", "prod")))
                .buildConfiguration();

        Tags tags = config.getGaugeTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeGaugeTags() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(app("web"))
                .with(tag("host", "localhost"))
                .with(gauge(tag("unit", "ns")))
                .buildConfiguration();

        Tags tags = config.getGaugeTags();
        assertEquals("Gauge should have 'app' tag", "web", tags.getTagValue("app"));
        assertEquals("Gauge should have 'host' tag", "localhost", tags.getTagValue("host"));
    }

    @Test
    public void shouldConfigureGaugeAggregations() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(gauge(P90))
                .with(gauge(aggrFreq(100)))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should configure gauge aggregations", aggregations, containsInAnyOrder(LAST, P90));
        assertEquals("Should gauge aggregation frequency", 100, config.getGaugeAggregationFreq());
    }

    @Test
    public void shouldConfigureTimerAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(timer(agg(LAST)))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, COUNT_PS, LAST));
    }

    @Test
    public void shouldConfigureCounterAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(counter(agg(LAST)))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT_PS, LAST));
    }

    @Test
    public void shouldConfigureGaugeAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder()
                .with(prefix("test_prefix"))
                .with(transport(UDP))
                .with(gauge(agg(P90)))
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
        }).withPrefix("test_prefix").withTransport(UDP).build();

        assertEquals("Should build with prefix configuration", "test_prefix", subject.configuration.getPrefix());
        assertEquals("Should build with transport configuration", UDP, subject.configuration.getTransport());
    }
}