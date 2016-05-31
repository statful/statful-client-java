# Statful Client for Java #

## Description ##

statful-client-java is intended to gather application and JVM metrics and send them to the Statful server.

## Quick start - UDP client ##

To bootstrap a Statful client to use UDP protocol, the quickest way is to do the following:

    StatfulClient statful = StatfulClient.buildUDPClient("prefix").build();


The _prefix_ option is always required and it will be a prefix for all metric names sent to Statful. This configuration uses the default _host_ and _port_.

### Timer ###
The simplest way of sending a _timer_ metric to Statful can be done like this:

    statful.timer("response_time", 1000).send();

### Counter ###
Or, if you prefer to send a _counter_ metric to Statful:

    statful.counter("transactions").send();

### Gauge ###
And finally, a _gauge_ metric to Statful can be preformed in the following way:

    statful.gauge("current_sessions", "2").send();

## Client configuration ##

To bootstrap the client, the following options can be used:

* __prefix__ [required] - global metrics prefix
* __host__ [optional] [default: '127.0.0.1']
* __port__ [optional] [default: 2013]
* __secure__ [__not supported yet__] [default: true] - enable or disable https
* __timeout__ [__not supported yet__] [default: 2000ms] - timeout for http/tcp transports
* __token__ [optional] - An authentication token to send to Statful
* __app__ [optional] - if specified set a tag ‘app=foo’
* __dryrun__ [optional] [default: false] - do not actually send metrics when flushing the buffer
* __tags__ [optional] - global list of tags to set
* __sampleRate__ [optional] [default: 100] [between: 1-100] - global rate sampling
* __namespace__ [optional] [default: ‘application’] - default namespace (can be overridden in method calls)
* __flushSize__ [optional] [default: 10] - defines the periodicity of buffer flushes
* __flushInterval__ [optional] [default: 0] - Defines an interval to flush the metrics

### Configuration example ###

    StatfulClient statful = StatfulFactory.buildUDPClient("prefix").with()
        .host("telemetry-relay.youcompany.com")
        .port(2001)
        .token("MyAppToken")
        .app("AccountService")
        .tag("cluster", "production")
        .build();

### Timer defaults configuration ###

The bellow example uses the _timer_ method to configure default timer tags, timer aggregations and timer aggregation frequency.

    StatfulClient statful = StatfulClient.buildUDPClient("prefix").with()
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

    statful.counter("transactions").with().tag("host", "localhost").tag("status", "SUCCESS").send();
        
### Adding metrics with aggregations ###

    statful.counter("transactions").with().aggregations(AVG, P90)).aggFreq(FREQ_10).send();
        
### Adding metrics with namespace ###

    statful.counter("transactions").with().namespace("my-namespace").send();
    
### Enabling/disabling Statful ###

    statful.enable();
    statful.counter("transactions").send(); // This metric will be sent to Statful
    
    statful.disable();
    statful.counter("transactions").send(); // This metric will NOT be sent to Statful
    
## Using annotations to instrument the application ## 
    
## Using AspectJ ##

Configure your project to weave your application as you like but don't forget to include the following dependencies on your project:

    <dependency>
        <groupId>com.mindera.statful</groupId>
        <artifactId>statful-client-aspects</artifactId>
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
        <include within="io.statful.client.aspects.*"/>
    </weaver>

    <aspects>
        <!-- weave the following aspects -->
        <aspect name="io.statful.client.aspects.StatfulAspect"/>
    </aspects>

## Setup tips ##

* Using Java 6 to compile

## Contribution guidelines ##

* Use Pull Requests and git "Feature Branch Workflow"
* Write unit tests