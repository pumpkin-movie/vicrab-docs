---
title: Logback
sidebar_order: 11
---

The `Vicrab-logback` library provides [Logback](http://logback.qos.ch/) support for Vicrab via an [Appender](http://logback.qos.ch/apidocs/ch/qos/logback/core/Appender.html) that sends logged exceptions to Vicrab. Once this integration is configured you can _also_ use Vicrab’s static API, [as shown on the usage page]({%- link _documentation/clients/java/usage.md -%}#usage-example), in order to do things like record breadcrumbs, set the current user, or manually send events.

<!-- WIZARD -->
## Installation

Using Maven:

```xml
<dependency>
    <groupId>com.vicrab</groupId>
    <artifactId>vicrab-logback</artifactId>
    <version>1.1</version>
</dependency>
```

If your project uses the spring boot framework, you need to introduce another jar package：


```xml
<dependency>
  <groupId>com.vicrab</groupId>
  <artifactId>vicrab-spring-boot-starter</artifactId>
  <version>1.2</version>
</dependency>
```

Using Gradle:

```groovy
compile 'com.vicrab:vicrab-logback:1.1'
```

Using SBT:

```scala
libraryDependencies += "com.vicrab" % "vicrab-logback" % "1.1"
```

For other dependency managers see the [central Maven repository](https://search.maven.org/#artifactdetails%7Ccom.vicrab%7Cvicrab-logback%7C1.1%7Cjar).

## Usage

The following example configures a `ConsoleAppender` that logs to standard out at the `INFO` level and a `VicrabAppender` that logs to the Vicrab server at the `WARN` level. The `ConsoleAppender` is only provided as an example of a non-Vicrab appender that is set to a different logging threshold, like one you may already have in your project.

Example configuration using the `logback.xml` format:

```xml
<configuration>
    <!-- Configure the Console appender -->
    <appender name="Console" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{HH:mm:ss.SSS} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Configure the Vicrab appender, overriding the logging threshold to the WARN level -->
    <appender name="Vicrab" class="com.vicrab.logback.VicrabAppender">
        <filter class="ch.qos.logback.classic.filter.ThresholdFilter">
            <level>WARN</level>
        </filter>
    </appender>

    <!-- Enable the Console and Vicrab appenders, Console is provided as an example
 of a non-Vicrab logger that is set to a different logging threshold -->
    <root level="INFO">
        <appender-ref ref="Console" />
        <appender-ref ref="Vicrab" />
    </root>
</configuration>
```

Next, **you’ll need to configure your DSN** (client key) and optionally other values such as `environment` and `release`. [See the configuration page] for ways you can do this.
<!-- ENDWIZARD -->

## Additional Data

It’s possible to add extra data to events thanks to [the MDC system provided by Logback](http://logback.qos.ch/manual/mdc.html).

### Mapped Tags

By default all MDC parameters are stored under the “Additional Data” tab in Vicrab. By specifying the `mdctags` option in your configuration you can choose which MDC keys to send as tags instead, which allows them to be used as filters within the Vicrab UI.

```java
void logWithExtras() {
    // MDC extras
    MDC.put("Environment", "Development");
    MDC.put("OS", "Linux");

    // This sends an event where the Environment and OS MDC values are set as additional data
    logger.error("This is a test");
}
```

## In Practice

```java
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.slf4j.MarkerFactory;

public class MyClass {
    private static final Logger logger = LoggerFactory.getLogger(MyClass.class);
    private static final Marker MARKER = MarkerFactory.getMarker("myMarker");

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
        // This sends an event with a tag named 'logback-Marker' to Vicrab
        logger.error(MARKER, "This is a test");
    }

    void logWithExtras() {
        // MDC extras
        MDC.put("extra_key", "extra_value");
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
