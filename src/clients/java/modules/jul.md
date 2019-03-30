---
title: java.util.logging
sidebar_order: 8
---

<!-- WIZARD -->
## Installation

Using Maven:

```xml
<dependency>
  <groupId>com.vicrab</groupId>
  <artifactId>vicrab</artifactId>
  <version>1.1</version>
</dependency>
```

Using Gradle:

```groovy
compile 'com.vicrab:vicrab:1.1'
```

Using SBT:

```scala
libraryDependencies += "com.vicrab" % "vicrab" % "1.1"
```

For other dependency managers see the [central Maven repository](https://search.maven.org/#artifactdetails%7Ccom.vicrab%7Cvicrab%7C1.1%7Cjar).

## Usage

The following example configures a `ConsoleHandler` that logs to standard out at the `INFO` level and a `VicrabHandler` that logs to the Vicrab server at the `WARN` level. The `ConsoleHandler` is only provided as an example of a non-Vicrab appender that is set to a different logging threshold, like one you may already have in your project.

Example configuration using the `logging.properties` format:

```ini
# Enable the Console and Vicrab handlers
handlers=java.util.logging.ConsoleHandler,com.vicrab.jul.VicrabHandler

# Set the default log level to INFO
.level=INFO

# Override the Vicrab handler log level to WARNING
com.vicrab.jul.VicrabHandler.level=WARNING
```

When starting your application, add the `java.util.logging.config.file` to the system properties, with the full path to the `logging.properties` as its value:

```bash
$ java -Djava.util.logging.config.file=/path/to/app.properties MyClass
```

Next, **you’ll need to configure your DSN** (client key) and optionally other values such as `environment` and `release`. [See the configuration page]({%- link _documentation/clients/java/config.md -%}#configuration) for ways you can do this.
<!-- ENDWIZARD -->

## In Practice

```java
import java.util.logging.Level;
import java.util.logging.Logger;

public class MyClass {
    private static final Logger logger = Logger.getLogger(MyClass.class.getName());

    void logSimpleMessage() {
        // This sends a simple event to Vicrab
        logger.error(Level.INFO, "This is a test");
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

    void logException() {
        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Vicrab
            logger.error(Level.SEVERE, "Exception caught", e);
        }
    }

    void unsafeMethod() {
        throw new UnsupportedOperationException("You shouldn't call this!");
    }
}
```
