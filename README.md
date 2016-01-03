# Telemetron Client for Java #

## Description ##

telemetron-client-java is intended to gather application and JVM metrics and send them to the Telemetron server.

## Quick start - UDP client ##

To bootstrap a Telemetron client to use UDP protocol, the quickest way is to do the following:

    TelemetronClient telemetron = TelemetronClient.newUDPClient("prefix").build();


The _prefix_ option is always required and it will be a prefix for all metric names sent to Telemetron. This configuration uses the default _host_ and _port_.

### Timer ###
The simplest way of sending a _timer_ metric to Telemetron can be done like this:

    telemetron.timer("response_time", 1000).send();

### Counter ###
Or, if you prefer to send a _counter_ metric to Telemetron:

    subject.counter("transactions").send();

### Gauge ###
And finally, a _gauge_ metric to Telemetron can be preformed in the following way:

    subject.gauge("current_sessions", "2").send();

## Client configuration ##

To bootstrap the client, the following options can be used:

* **prefix** [required] - global metrics prefix
* **host** [optional] [default: '127.0.0.1']
* **port** [optional] [default: 2013]
* **secure** [optional] [default: true] - enable or disable https
* **timeout** [optional] [default: 2000ms] - timeout for http/tcp transports
* **token** [optional] - An authentication token to send to Telemetron
* **app** [optional] - if specified set a tag ‘app=foo’
* **dryrun** [optional] [default: false] - do not actually send metrics when flushing the buffer
* **logger** [optional] - logger object which supports at least debug/error methods
* **tags** [optional] - global list of tags to set
* **sampleRate** [optional] [default: 100] [between: 1-100] - global rate sampling
* **namespace** [optional] [default: ‘application’] - default namespace (can be overridden in method calls)
* **flushSize** [optional] [default: 10] - defines the periodicity of buffer flushes
* **flushInterval** [optional] [default: 0] - Defines an interval to flush the metrics

### Configuration example ###

    TelemetronClient telemetron = TelemetronClient.newUDPClient("prefix")
        .with
            .host("telemetry-relay.youcompany.com")
            .port(2001)
            .token("MyAppToken")
            .app("AccountService")
            .tag("cluster", "production")
            .build();

### Timer defaults configuration ###

The bellow example uses the _timer_ method to configure default timer tags, timer aggregations and timer aggregation frequency.

    TelemetronClient telemetron = TelemetronClient.newUDPClient("prefix").with
        .timer(tag("unit", "s"))
        .timer(agg(LAST))
        .timer(aggrFreq(100))
        .build();
        
### Counter defaults configuration ###

To configure Counter defaults configuration, you should use the _counter_ method. Please check the Timer defaults configuration for an example.

### Gauge defaults configuration ###

To configure Gauge defaults configuration, you should use the _gauge_ method. Please check the Timer defaults configuration for an example.
        
## Building metrics ##

### Building metrics tags ###

    subject.counter("transactions").with.tag("host", "localhost").tag("status", "SUCCESS").send();
        
### Adding metrics with aggregations ###

    subject.counter("transactions").with.aggregations(AVG, P90)).aggFreq(FREQ_10).send();
        
### Adding metrics with namespace ###

    subject.counter("transactions").with.namespace("my-namespace").send();

## Setup tips ##

* Using Java 6 to compile

## Contribution guidelines ##

* Use Pull Requests and git "Feature Branch Workflow"
* Write unit tests