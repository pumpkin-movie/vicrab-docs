---
title: Java
sidebar_order: 6
sidebar_relocation: platforms
---

Vicrab for Java is a collection of modules provided by Vicrab. At its core, Vicrab for Java provides a raw client for sending events to Vicrab. To begin, we **highly recommend** you use one of the library or framework integrations listed under Installation. Otherwise, [manual usage]({%- link _documentation/clients/java/usage.md -%}) is another option. 

## Getting Started

Getting started with Vicrab is a three step process:

1.  [Sign up for an account](https://www.vicrab.com/signup/)
2.  [Install your SDK](#Installation)
3.  [Configure it](#Configuration)

## Installation

-   [Android](./modules/android.md)
-   [java.util.logging](./modules/jul.md) >> [demo](./modules/demo/vicrab-utilog-demo)
-   [Log4j 1.x](./modules/log4j.md) >> [demo](./modules/demo/vicrab-log4j1-demo)
-   [Log4j 2.x](./modules/log4j2.md) >> [demo](./modules/demo/vicrab-log4j-demo)
-   [Logback](./modules/logback.md) >> [demo](./modules/demo/vicrab-logback-demo)

## Configuration 
Use the configuration below in combination with any of the integrations from above. The configuration will only work after an integration is installed. After that, [set your DSN]{#setting-the-dsn}.

### Setting the DSN (Data Source Name) {#setting-the-dsn}

The DSN is the first and most important thing to configure because it tells the SDK where to send events. You can find your project’s DSN in the “Client Keys” section of your “Project Settings” in Vicrab.

In a properties file on your filesystem or classpath (defaults to `vicrab.properties`):

```
dsn=https://private_key@host:port/1
```

Via the Java System Properties _(not available on Android)_:

```bash
java -Dvicrab.dsn=https://private_key@host:port/1 -jar app.jar
```

Via a System Environment Variable _(not available on Android)_:

```bash
VICRAB_DSN=https://private_key@host:port/1 java -jar app.jar
```

Or in code:

```java
import com.vicrab.Vicrab;

Vicrab.init("https://private_key@host:port/1");

```

### Configuration Methods

There are multiple ways to configure the Java SDK, but all of them take the same options. 

## Resources

-   [Examples](https://github.com/vicrab/vicrab-example)
