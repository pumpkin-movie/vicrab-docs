---
title: 'Log4j 2.x'
sidebar_order: 10
---

The `vicrab-log4j2` library provides [Log4j 2.x](https://logging.apache.org/log4j/2.x/) support for Vicrab via an [Appender](https://logging.apache.org/log4j/2.x/log4j-core/apidocs/org/apache/logging/log4j/core/Appender.html) that sends logged exceptions to Vicrab. Once this integration is configured you can _also_ use Vicrab’s static API, [as shown on the usage page], in order to do things like record breadcrumbs, set the current user, or manually send events.

<!-- WIZARD -->
## Installation

Using Maven:

```xml
<dependency>
    <groupId>com.vicrab</groupId>
    <artifactId>vicrab-log4j2</artifactId>
    <version>1.1</version>
</dependency>
```

If your project uses the spring boot framework, you need to introduce another jar package based on the previous one.

```xml
<dependency>
  <groupId>com.vicrab</groupId>
  <artifactId>vicrab-spring-boot-starter</artifactId>
  <version>1.2</version>
</dependency>
```


Using Gradle:

```groovy
compile 'com.vicrab:vicrab-log4j2:1.1'
```

Using SBT:

```scala
libraryDependencies += "com.vicrab" % "vicrab-log4j2" % "1.1"
```

For other dependency managers see the [central Maven repository](https://search.maven.org/#artifactdetails%7Ccom.vicrab%7Cvicrab-log4j2%7C1.1%7Cjar).

## Usage

The following example configures a `ConsoleAppender` that logs to standard out at the `INFO` level and a `VicrabAppender` that logs to the Vicrab server at the `WARN` level. The `ConsoleAppender` is only provided as an example of a non-Vicrab appender that is set to a different logging threshold, like one you may already have in your project.

Example configuration using the `log4j2.xml` format:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration status="warn" packages="org.apache.logging.log4j.core,com.vicrab.log4j2">
    <appenders>
        <Console name="Console" target="SYSTEM_OUT">
            <PatternLayout pattern="%d{HH:mm:ss.SSS} [%t] %-5level %logger{36} - %msg%n" />
        </Console>

        <Vicrab name="Vicrab" />
    </appenders>

    <loggers>
        <root level="INFO">
            <appender-ref ref="Console" />
            <!-- Note that the Vicrab logging threshold is overridden to the WARN level -->
            <appender-ref ref="Vicrab" level="WARN" />
        </root>
    </loggers>
</configuration>
```

Next, **you’ll need to configure your DSN** (client key) and optionally other values such as `environment` and `release`. [See the configuration page]({%- link _documentation/clients/java/config.md -%}#configuration) for ways you can do this.
<!-- ENDWIZARD -->

## Additional Data

It’s possible to add extra data to events thanks to [the marker system](https://logging.apache.org/log4j/2.x/manual/markers.html) provided by Log4j 2.x.

### Mapped Tags

By default all MDC parameters are stored under the “Additional Data” tab in Vicrab. By specifying the `mdctags` option in your configuration you can choose which MDC keys to send as tags instead, which allows them to be used as filters within the Vicrab UI.

```java
void logWithExtras() {
    // ThreadContext ("MDC") extras
    ThreadContext.put("Environment", "Development");
    ThreadContext.put("OS", "Linux");

    // This sends an event where the Environment and OS MDC values are set as additional data
    logger.error("This is a test");
}
```

## In Practice

```java
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class MyClass {
    private static final Logger logger = LogManager.getLogger(MyClass.class);
    private static final Marker MARKER = MarkerManager.getMarker("myMarker");

    void logSimpleMessage() {
        // This sends a simple event to Vicrab
        logger.error("This is a test");
    }

    void logWithBreadcrumbs() {
        // Record a breadcrumb that will be sent with the next event(s),
        // by default the last 100 breadcrumbs are kept.
        Vicrab.record(
            new BreadcrumbBuilder().setMessage("User made an action").build()
        );

        // This sends a simple event to Vicrab
        logger.error("This is a test");
    }

    void logWithTag() {
        // This sends an event with a tag named 'log4j2-Marker' to Vicrab
        logger.error(MARKER, "This is a test");
    }

    void logWithExtras() {
        // MDC extras
        ThreadContext.put("extra_key", "extra_value");
        // NDC extras are sent under 'log4j2-NDC'
        ThreadContext.push("Extra_details");
        // This sends an event with extra data to Vicrab
        logger.error("This is a test");
    }

    void logException() {
        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Vicrab
            logger.error("Exception caught", e);
        }
    }

    void unsafeMethod() {
        throw new UnsupportedOperationException("You shouldn't call this!");
    }
}
```
