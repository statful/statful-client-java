package com.statful.client.core.api;

import com.statful.client.domain.api.Aggregation;
import com.statful.client.domain.api.ClientConfiguration;
import com.statful.client.domain.api.Tags;
import org.junit.Test;

import java.util.Collection;

import static com.statful.client.core.api.ConfigurationBuilder.newBuilder;
import static com.statful.client.core.api.MetricBuilder.*;
import static com.statful.client.domain.api.Aggregation.*;
import static com.statful.client.domain.api.AggregationFrequency.FREQ_10;
import static com.statful.client.domain.api.AggregationFrequency.FREQ_120;
import static com.statful.client.domain.api.Transport.UDP;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.*;

public class ConfigurationBuilderTest {

    @Test
    public void shouldUseDefaultValuesWhenNoneAreSpecified() {
        ClientConfiguration config = newBuilder(UDP)
                .buildConfiguration();

        assertEquals("Should have default host", "127.0.0.1", config.getHost());
        assertEquals("Should have default port", 2013, config.getPort());
        assertFalse("Should not dry run as default", config.isDryRun());
        assertEquals("Should have default sample rate", 100, config.getSampleRate());
        assertEquals("Should have default namespace", "application", config.getNamespace());
        assertEquals("Should have default flush size", 10, config.getFlushSize());
        assertEquals("Should have default flush interval", 5000, config.getFlushIntervalMillis());
    }

    @Test
    public void shouldUseTimerDefaultValues() {
        ClientConfiguration config = newBuilder(UDP)
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag in ms", "ms", tags.getTagValue("unit"));

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Timer should have default aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT));
        assertEquals("Should have default timer aggregation frequency", FREQ_10, config.getTimerAggregationFrequency());
    }

    @Test
    public void shouldUseCounterDefaultValues() {
        ClientConfiguration config = newBuilder(UDP)
                .buildConfiguration();

        assertNull("Should not have default counter tags", config.getCounterTags());

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should have default counter aggregations", aggregations, containsInAnyOrder(COUNT, SUM));
        assertEquals("Should have default counter aggregation frequency", FREQ_10, config.getCounterAggregationFrequency());
    }

    @Test
    public void shouldUseGaugeDefaultValues() {
        ClientConfiguration config = newBuilder(UDP)
                .buildConfiguration();

        assertNull("Should not have default gauge tags", config.getGaugeTags());

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should have default gauge aggregations", aggregations, containsInAnyOrder(LAST));
        assertEquals("Should have default gauge aggregation frequency", FREQ_10, config.getGaugeAggregationFrequency());
    }

    @Test
    public void shouldConfigureUDPTransport() {
        ClientConfiguration config = newBuilder(UDP)
                .host("localhost")
                .port(6167)
                .buildConfiguration();

        assertEquals("Should configure host", "localhost", config.getHost());
        assertEquals("Should configure port", 6167, config.getPort());
        assertEquals("Should configure transport", UDP, config.getTransport());
    }

    @Test
    public void shouldConfigureToken() {
        ClientConfiguration config = newBuilder(UDP)
                .token("myToken")
                .buildConfiguration();

        assertEquals("Should configure token", "myToken", config.getToken());
    }

    @Test
    public void shouldConfigureApp() {
        ClientConfiguration config = newBuilder(UDP)
                .app("myApp")
                .buildConfiguration();

        assertEquals("Should configure app name", "myApp", config.getApp());
    }

    @Test
    public void shouldConfigureDryRun() {
        ClientConfiguration config = newBuilder(UDP)
                .isDryRun(true)
                .buildConfiguration();

        assertTrue("Should configure dry run", config.isDryRun());
    }

    @Test
    public void shouldConfigureWorkersPool() {
        ClientConfiguration config = newBuilder(UDP)
                .workerPoolSize(20)
                .buildConfiguration();

        assertEquals("Should configure pool size", 20, config.getWorkersPoolSize());
    }

    @Test
    public void shouldConfigureConnectionPool() {
        ClientConfiguration config = newBuilder(UDP)
                .connectionPoolSize(20)
                .buildConfiguration();

        assertEquals("Should configure connection pool size", 20, config.getConnectionPoolSize());
    }

    @Test
    public void shouldConfigureConnectionTimeout() {
        ClientConfiguration config = newBuilder(UDP)
                .connectionTimeoutMs(200)
                .buildConfiguration();

        assertEquals("Should configure connection timeout", 200, config.getConnectTimeoutMillis());
    }

    @Test
    public void shouldConfigureSocketTimeout() {
        ClientConfiguration config = newBuilder(UDP)
                .timeoutMs(200)
                .buildConfiguration();

        assertEquals("Should configure socket timeout", 200, config.getSocketTimeoutMillis());
    }

    @Test
    public void shouldConfigureSecureTransport() {
        ClientConfiguration config = newBuilder(UDP)
                .secure(true)
                .buildConfiguration();

        assertTrue("Should configure secure transport", config.isSecure());
    }

    @Test
    public void shouldConfigureDefaultTags() {
        ClientConfiguration config = newBuilder(UDP)
                .tag("host", "localhost")
                .tag("cluster", "prod")
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Should configure 'host' tag", "localhost", tags.getTagValue("host"));
        assertEquals("Should configure 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldConfigureSampleRate() {
        ClientConfiguration config = newBuilder(UDP)
                .sampleRate(50)
                .buildConfiguration();

        assertEquals("Should configure sample rate", 50, config.getSampleRate());
    }

    @Test
    public void shouldConfigureNamespace() {
        ClientConfiguration config = newBuilder(UDP)
                .namespace("my namespace")
                .buildConfiguration();

        assertEquals("Should configure namespace", "my namespace", config.getNamespace());
    }

    @Test
    public void shouldConfigureFlushSize() {
        ClientConfiguration config = newBuilder(UDP)
                .flushSize(1000)
                .buildConfiguration();

        assertEquals("Should configure flush size", 1000, config.getFlushSize());
    }

    @Test
    public void shouldConfigureFlushInterval() {
        ClientConfiguration config = newBuilder(UDP)
                .flushInterval(500)
                .buildConfiguration();

        assertEquals("Should configure flush interval", 500, config.getFlushIntervalMillis());
    }

    @Test
    public void shouldConfigureTimerTags() {
        ClientConfiguration config = newBuilder(UDP)
                .timer(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getTimerTags();
        assertEquals("Timer should have 'unit' tag", "ms", tags.getTagValue("unit"));
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeTimerTags() {
        ClientConfiguration config = newBuilder(UDP)
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
        ClientConfiguration config = newBuilder(UDP)
                .timer(agg(LAST))
                .timer(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, LAST));
        assertEquals("Should timer aggregation frequency", FREQ_120, config.getTimerAggregationFrequency());
    }

    @Test
    public void shouldConfigureCounterTags() {
        ClientConfiguration config = newBuilder(UDP)
                .counter(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getCounterTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeCounterTags() {
        ClientConfiguration config = newBuilder(UDP)
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
        ClientConfiguration config = newBuilder(UDP)
                .counter(agg(LAST))
                .counter(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(COUNT, SUM, LAST));
        assertEquals("Should counter aggregation frequency", FREQ_120, config.getCounterAggregationFrequency());
    }

    @Test
    public void shouldConfigureGaugeTags() {
        ClientConfiguration config = newBuilder(UDP)
                .gauge(tag("cluster", "prod"))
                .buildConfiguration();

        Tags tags = config.getGaugeTags();
        assertEquals("Timer should have 'cluster' tag", "prod", tags.getTagValue("cluster"));
    }

    @Test
    public void shouldMergeGaugeTags() {
        ClientConfiguration config = newBuilder(UDP)
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
        ClientConfiguration config = newBuilder(UDP)
                .gauge(agg(P90))
                .gauge(aggFreq(FREQ_120))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getGaugeAggregations().getAggregations();
        assertThat("Should configure gauge aggregations", aggregations, containsInAnyOrder(LAST, P90));
        assertEquals("Should gauge aggregation frequency", FREQ_120, config.getGaugeAggregationFrequency());
    }

    @Test
    public void shouldConfigureTimerAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder(UDP)
                .timer(agg(LAST))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getTimerAggregations().getAggregations();
        assertThat("Should configure timer aggregations", aggregations, containsInAnyOrder(AVG, P90, COUNT, LAST));
    }

    @Test
    public void shouldConfigureCounterAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder(UDP)
                .counter(agg(LAST))
                .buildConfiguration();

        Collection<Aggregation> aggregations = config.getCounterAggregations().getAggregations();
        assertThat("Should configure counter aggregations", aggregations, containsInAnyOrder(COUNT, SUM, LAST));
    }

    @Test
    public void shouldConfigureGaugeAggregationsWithBuilder() {
        ClientConfiguration config = newBuilder(UDP)
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

        TestClass subject = newBuilder(UDP, new ConfigurationBuilderChain<TestClass>() {
            @Override
            public TestClass build(ClientConfiguration configuration) {
                return new TestClass(configuration);
            }
        }).build();

        assertEquals("Should build tags transport configuration", UDP, subject.configuration.getTransport());
    }

    @Test
    public void shouldIgnoreNullBuilderAttributes() {
        ClientConfiguration config = newBuilder(UDP)
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
        assertEquals("Should have default timer aggregation frequency", FREQ_10, config.getTimerAggregationFrequency());

        assertNull("Should not have counter tags", config.getCounterTags());
        assertEquals("Should have default counter aggregations", 2, config.getCounterAggregations().getAggregations().size());
        assertEquals("Should have default counter aggregation frequency", FREQ_10, config.getCounterAggregationFrequency());

        assertNull("Should not have gauge tags", config.getGaugeTags());
        assertEquals("Should have default gauge aggregations", 1, config.getGaugeAggregations().getAggregations().size());
        assertEquals("Should have default gauge aggregation frequency", FREQ_10, config.getGaugeAggregationFrequency());
    }
}