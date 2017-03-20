Statful Client for Java
==============

[![Build Status](https://travis-ci.org/statful/statful-client-java.svg?branch=master)](https://travis-ci.org/statful/statful-client-java)

Statful client for Java. This client is intended to gather metrics and send them to Statful.

## Table of Contents

* [Supported Versions of Java](#supported-versions-of-java)
* [Configuration](#configuration)
* [Quick Start](#quick-start)
* [Custom Transport](#custom-transport)
* [Examples](#examples)
* [Reference](#reference)
* [Authors](#authors)
* [License](#license)

## Supported Versions of Java

| Statful client Version | Tested Java versions  |
|:---|:---|
| 1.x.x | `Java 7` |

## Configuration

Add one of the following snippets to your POM.xml file.

### Configuration for UDP client

    <dependency>
        <groupId>com.statful.client</groupId>
        <artifactId>udp-client</artifactId>
        <version>${statful-client.version}</version>
    </dependency>

### Configuration for HTTP client

    <dependency>
        <groupId>com.statful.client</groupId>
        <artifactId>http-client</artifactId>
        <version>${statful-client.version}</version>
    </dependency>

### Using AspectJ

### Configuration

Configure your project to weave your application as you like but don't forget to include the following dependencies on your project:

    <dependency>
        <groupId>com.statful.client</groupId>
        <artifactId>aspects</artifactId>
        <version>${statful-client.version}</version>
    </dependency>

    <dependency>
        <groupId>org.aspectj</groupId>
        <artifactId>aspectjrt</artifactId>
        <version>${aspectj.version}</version>
    </dependency>

Then, you must set `StatfulAspect` with your `StatfulClient` instance:

    StatfulAspect statfulAspect = Aspects.aspectOf(StatfulAspect.class);
    statfulAspect.setStatfulClient(statfulClient());

You must include the aspect on your AspectJ configuration:

    <weaver>
        <include within="com.statful.client.aspects.*"/>
    </weaver>
    <aspects>
        <aspect name="com.statful.client.aspects.StatfulAspect"/>
    </aspects>
    
### Annotation

After configuring `StatfulAspect`, you can use `@Timer` to annotate your methods and measure the time of execution.

```java
@Timer(name = "execution", tags = {"controller", "Entities", "method", "postEntity"})
public Response<Status> postEntity(Entity entity) {
    // ...
}
```

The above example would send a metric named `application.timer.execution` with the configured tags and the default timer aggregations.

## Quick start

After declaring Statful Client as a dependency, you are ready to use it. The quickest way is to do the following:

```java
// Instantiates a new HTTP client and build the configuration fluently
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .token("STATFUL_API_TOKEN")
        .build();

// Send a metric
client.counter("transactions", 1).send();
```
> **IMPORTANT:** This configuration uses the default **host** and **port**. You can learn more about configuration in [Reference](#reference).

## Custom Transport

You can add support for your own custom transport, besides the currently supported HTTP and UDP. To adapt the client to your implementation, you must perform two steps:

1. Implement the `TransportSender` interface, which defines a method to send metric using your own transport.
2. Implement a client factory by extending the abstract class CustomStatfulFactory`.

By extending implementing `TransportSender buildTransportSender(final ClientConfiguration configuration)`, your custom TransportSender` implementation has access to all global client configuration, as well as the asynchronous sending of metrics, so you only need to worry about sending metrics.  

### Custom Transport Example

First implement the `TransportSender` interface, then you can implement your client factory similar to the following way:

```java
public final class StatfulFactory {

    private static final ThriftClientFactory THRIFT_CLIENT_FACTORY = new ThriftClientFactory();

    private StatfulFactory() { }

    public static StatfulClientBuilder buildThriftClient() {
        // The client is built with default underlying mechanism used in HTTP and UDP transports, including asynchronous sending
        return THRIFT_CLIENT_FACTORY.buildClient();
    }

    private static class ThriftClientFactory extends CustomStatfulFactory {
        ThriftClientFactory() {
            super(Transport.OTHER);
        }

        @Override
        protected TransportSender buildTransportSender(final ClientConfiguration configuration) {
            // Has access to global configuration
            return new ThriftSender(configuration.getHost(), configuration.getPort());
        }
    }
}
```

To use your code, you need to include the following Statful dependency in your POM.xml file:

    <dependency>
        <groupId>com.statful.client</groupId>
        <artifactId>core</artifactId>
        <version>${statful-client.version}</version>
    </dependency>
    
And then, use your own implementation to send metrics:

```java
StatfulClient client = StatfulFactory.buildThriftClient().with()
        .app("AccountService")
        .host("statful-relay.yourcompany.com")
        .tag("cluster", "production")
        .build();

client.gauge("testGauge", 10).send();
```

### HTTP Authentication Token

If you implement HTTP-based transport, you need to send the `M-Api-Token` header with your token. You can access the token name inside your client factory using `ClientConfiguration.TOKEN_HEADER`.


### Avoiding metrics back-pressure

When the rate of metrics sent to the Statful client is too high, metrics start to be lost and warn log messages are printed. To help reduce this problem there are multiple configuration parameters that can be useful:

- **timeout**: Reducing communication timeout, for when the network latency is too high, helps free busy workers sooner to handle other metrics in the buffer.
- **workerPoolSize**: Increase the workers pool (default is 1). Note that each worker spawns a thread and, if too many workers are spawn, thread contention could start to be a problem.
- **flushSize** and **flushInterval**: Increasing metrics flush size and interval helps reducing back-pressure but it'll have memory impact and and the payload size sent to Statful will be bigger.

## Examples

You can find here some useful usage examples of the Statful Client. In the following examples is assumed you have already included Statful Client in your project.

### UDP Configuration

Creates a simple UDP configuration for the client.

```java
StatfulClient client = StatfulFactory.buildUDPClient().with()
        .host("statful-relay.yourcompany.com")
        .app("AccountService")
        .tag("cluster", "production")
        .build();
```

### HTTP Configuration

Creates a simple HTTP API configuration for the client.

```java
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .app("AccountService")
        .token("STATFUL_API_TOKEN")
        .tag("cluster", "production")
        .build();
```

### Defaults Configuration Per Method

Creates a configuration for the client with custom default options per method.

```java
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .tag("cluster", "production")
        .token("STATFUL_API_TOKEN")
        .counter(agg(AVG)).counter(aggFreq(FREQ_180))
        .gauge(agg(FIRST)).gauge(aggFreq(FREQ_180))
        .timer(agg(COUNT)).timer(aggFreq(FREQ_180))
        .timer(tag("cluster", "qa"))
        .build();
```

### Mixed Complete Configuration

Creates a configuration defining a value for other available options.

```java
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .token("STATFUL_API_TOKEN")
        .namespace("application")
        .isDryRun(true)
        .flushInterval(5000)
        .flushSize(50)
        .timeoutMs(300)
        .workerPoolSize(2)
        .connectionTimeoutMs(300)
        .connectionPoolSize(4)
        .sampleRate(10)
        .secure(false)
        .build();
```

### Add metrics

Creates a simple client configuration and use it to send some metrics.

```java
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .app("AccountService")
        .token("TOKEN")
        .tag("cluster", "production")
        .build();

// Send three different metrics (gauge, timer and a counter)
client.gauge("testGauge", 10).send();
client.timer("testTimer", 100).send();
client.counter("testTimer", 1).send();
client.put("testCustomMetric", 1).send();

// Metric to be sent with tags
client.counter("testCounter", 1).with()
        .tag("host", "localhost").tag("status", "SUCCESS")
        .send();
```

### Add aggregated metrics

Create a simple client configuration and use it to store previously aggregated metrics. 
This is useful for existing collectors that expose metrics aggregated over periods of time.

```java
StatfulClient client = StatfulFactory.buildHTTPClient().with()
        .app("AccountService")
        .token("TOKEN")
        .tag("cluster", "production")
        .build();

// Send three different metrics (gauge, timer and a counter)
client.aggregatedGauge("testGauge", 10, Aggregation.AVG, AggregationFreq.FREQ_120).send();
client.aggregatedTimer("testTimer", 100, Aggregation.AVG, AggregationFreq.FREQ_10).send();
client.aggregatedCounter("testTimer", 1, Aggregation.SUM, AggregationFreq.FREQ_10).send();
client.aggregatedPut("testCustomMetric", Aggregation.COUNT, AggregationFreq.FREQ_180).send();

// Metric to be sent with tags
client.aggregatedCounter("testCounter", 1, Aggregation.SUM, AggregationFreq.FREQ_30).with()
        .tag("host", "localhost").tag("status", "SUCCESS")
        .send();
```

## Reference

Detailed reference if you want to take full advantage from Statful.

### Global configuration

The custom options that can be set on config param are detailed below.

| Option | Description | Type | Default | Required |
|:---|:---|:---|:---|:---|
| _app_ | Defines the application global name. If specified sets a global tag `app=setValue`. | `String` | **none** | **NO** |
| _dryRun_ | Defines if metrics should be output to the logger instead of being send. | `boolean` | `false` | **NO** |
| _flushInterval_ | Defines the periodicity of buffer flushes in **miliseconds**. | `int` | `3000` | **NO** |
| _flushSize_ | Defines the maximum buffer size before performing a flush. | `int` | `1000` | **NO** |
| _namespace_ | Defines the global namespace. | `String` | `application` | **NO** |
| _sampleRate_ | Defines the rate sampling. **Should be a number between [1, 100]**. | `int` | `100` | **NO** |
| _tags_ | Defines a list global tags. | `String`, `String` pairs | Empty list of tags | **NO** |
| _host_ | Defines the host name to where the metrics should be sent. | `String` | `127.0.0.1` | **NO** |
| _port_ | Defines the port. | `int` | `2013` | **NO** |
| _token_ | Defines the authentication token to be used. | `String` | **none** | **NO** |
| _timeout_ | Defines the timeout for the transport layers in **miliseconds**. Must be set inside _api_. | `long` | `2000` | **NO** |
| _secure_ | Enable or disables HTTPS. | `boolean` | `true` | **NO** |
| _connectTimeout_ | Connection timeout for http/tcp transports in **milliseconds**. | `long` | `500` | **NO** |
| _connectionPoolSize_ | Connection pool size. | `int` | `10` | **NO** |
| _workerPoolSize_ | Asynchronous workers pool size. | `int` | `1` | **NO** |

### Methods

```java
// normal methods
client.counter("testCounter", 1).with().aggregations(SUM).send();
client.gauge("testGauge", 10).with().tag("host", "localhost").send();
client.timer("testTimer", 200).with().namespace("sandbox").send();
client.metric("testCustomMetric").with().namespace("my-namespace").send();
client.counter("transactions").with().tag("host", "localhost").tag("status", "SUCCESS").send();
client.counter("transactions").with().aggregations(AVG, P90).aggFreq(FREQ_10).send();

// aggregated methods
client.aggregatedGauge("testGauge", 10, Aggregation.AVG, AggregationFreq.FREQ_120).send();
client.aggregatedTimer("testTimer", 100, Aggregation.AVG, AggregationFreq.FREQ_10).send();
client.aggregatedCounter("testTimer", 1, Aggregation.SUM, AggregationFreq.FREQ_10).send();
client.aggregatedPut("testCustomMetric", Aggregation.COUNT, AggregationFreq.FREQ_180).send();
```

The methods for sending metrics receive a metric name and a metric value as arguments and send a counter/gauge/timer/custom metric.
Read the methods options reference bellow to get more information about the default values.

| Option | Description | Default for Counter | Default for Gauge | Default for Timer | Default for Custom Metric |
|:---|:---|:---|:---|:---|:---|:---|
| _aggregations_ | Defines the aggregations to be executed. These aggregations are merged with the ones configured globally, including method defaults.<br><br> **Valid Aggregations:** `AVG, COUNT, SUM, FIRST, LAST, P90, P95, MIN, MAX` | `SUM, COUNT` | `LAST` | `AVG, P90, COUNT` | none |
| _aggFreq_ | Defines the aggregation frequency in **seconds**. It overrides the global aggregation frequency configuration.<br><br> **Valid Aggregation Frequencies:** `10, 30, 60, 120, 180, 300` | `10` | `10` | `10` | `10` |
| _namespace_ | Defines the namespace of the metric. It overrides the global namespace configuration. | `application` | `application` | `application` | `application` |
| _tags_ | Defines the tags of the metric. These tags are merged with the ones configured globally, including method defaults. | none | none | ` unit: 'ms'` | none |
| _timestamp_ | Defines the timestamp of the metric. This timestamp is a **POSIX/Epoch** time in **seconds**. | `current timestamp` | `current timestamp` | `current timestamp` | `current timestamp` |

> Note that calling the `aggregations` method on the `aggregatedX` methods will throw `UnsupportedOperationException`.

### Enabling/disabling Statful

```java
// The following metric will be sent to Statful
statful.enable();
statful.counter("transactions").send();

// The following metric will NOT be sent to Statful
statful.disable();
statful.counter("transactions").send();
```

## Authors

[Mindera - Software Craft](https://github.com/Mindera)

## License

Statful Java Client is available under the MIT license. See the [LICENSE](https://raw.githubusercontent.com/statful/statful-client-objc/master/LICENSE) file for more information.
