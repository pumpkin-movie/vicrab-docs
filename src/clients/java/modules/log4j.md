---
title: 'Log4j 1.x'
sidebar_order: 9
---

The `vicrab-log4j` library provides [Log4j 1.x](https://logging.apache.org/log4j/1.2/) support for Vicrab via an [Appender](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/Appender.html) that sends logged exceptions to Vicrab. Once this integration is configured you can _also_ use Vicrab’s static API, [as shown on the usage page], in order to do things like record breadcrumbs, set the current user, or manually send events.

<!-- WIZARD -->
## Installation

Using Maven:

```xml
<dependency>
    <groupId>com.vicrab</groupId>
    <artifactId>vicrab-log4j</artifactId>
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
compile 'com.vicrab:vicrab-log4j:1.1'
```

Using SBT:

```scala
libraryDependencies += "com.vicrab" % "vicrab-log4j" % "1.1"
```

For other dependency managers see the [central Maven repository](https://search.maven.org/#artifactdetails%7Ccom.vicrab%7Cvicrab-log4j%7C1.1%7Cjar).

## Usage

The following examples configure a `ConsoleAppender` that logs to standard out at the `INFO` level and a `VicrabAppender` that logs to the Vicrab server at the `WARN` level. The `ConsoleAppender` is only provided as an example of a non-Vicrab appender that is set to a different logging threshold, like one you may already have in your project.

Example configuration using the `log4j.properties` format:

```ini
# Enable the Console and Vicrab appenders
log4j.rootLogger=INFO, Console, Vicrab

# Configure the Console appender
log4j.appender.Console=org.apache.log4j.ConsoleAppender
log4j.appender.Console.layout=org.apache.log4j.PatternLayout
log4j.appender.Console.layout.ConversionPattern=%d{HH:mm:ss.SSS} [%t] %-5p: %m%n

# Configure the Vicrab appender, overriding the logging threshold to the WARN level
log4j.appender.Vicrab=com.vicrab.log4j.VicrabAppender
log4j.appender.Vicrab.threshold=WARN
```

Alternatively, using the `log4j.xml` format:

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE log4j:configuration SYSTEM "log4j.dtd">
<log4j:configuration debug="true"
    xmlns:log4j='http://jakarta.apache.org/log4j/'>

    <!-- Configure the Console appender -->
    <appender name="Console" class="org.apache.log4j.ConsoleAppender">
        <layout class="org.apache.log4j.PatternLayout">
            <param name="ConversionPattern"
                   value="%d{yyyy-MM-dd HH:mm:ss} %-5p %c{1}:%L - %m%n" />
        </layout>
    </appender>

    <!-- Configure the Vicrab appender, overriding the logging threshold to the WARN level -->
    <appender name="Vicrab" class="com.vicrab.log4j.VicrabAppender">
        <!-- Override the Vicrab handler log level to WARN -->
        <filter class="org.apache.log4j.varia.LevelRangeFilter">
            <param name="levelMin" value="WARN" />
        </filter>
    </appender>

    <!-- Enable the Console and Vicrab appenders, Console is provided as an example
 of a non-Vicrab logger that is set to a different logging threshold -->
    <root level="INFO">
        <appender-ref ref="Console" />
        <appender-ref ref="Vicrab" />
    </root>
</log4j:configuration>
```

Next, **you’ll need to configure your DSN** (client key) and optionally other values such as `environment` and `release`. [See the configuration page]({%- link _documentation/clients/java/config.md -%}#configuration) for ways you can do this.
<!-- ENDWIZARD -->

## Additional Data

It’s possible to add extra data to events thanks to [the MDC](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/MDC.html) and [the NDC](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/NDC.html) systems provided by Log4j 1.x.

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
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

public class MyClass {
    private static final Logger logger = Logger.getLogger(MyClass.class);

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

    void logWithExtras() {
        // MDC extras
        MDC.put("extra_key", "extra_value");
        // NDC extras are sent under 'log4J-NDC'
        NDC.push("Extra_details");
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

## Asynchronous Logging

Vicrab uses asynchronous communication by default, and so it is unnecessary to use an [AsyncAppender](https://logging.apache.org/log4j/1.2/apidocs/org/apache/log4j/AsyncAppender.html).
