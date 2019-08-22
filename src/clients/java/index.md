---
title: Java
sidebar_order: 6
sidebar_relocation: platforms
---

Vicrab for Java is a collection of modules provided by Vicrab. At its core, Vicrab for Java provides a raw client for sending events to Vicrab. To begin, we **highly recommend** you use one of the library or framework integrations listed under Installation. Otherwise, [manual usage]({%- link _documentation/clients/java/usage.md -%}) is another option. 


## Introduction Guide
Start using Vicrab in the following four steps, in order.

1. Register SaaS account

2. Select SDK and modify log configuration

3. Get the configuration DSN and configure it

4. Cloud services view data

## Registered Account

Getting started with Vicrab is a three step process:

1.  [Register SaaS account](https://www.vicrab.com/signup/)

2.  Create a new organization

3.  Create a new project to get the value of DSN

## Select the correct SDK and modify the log configuration
   
1 Configuration guide for Android Development
- [Android](https://www.vicrab.com/config/android) > [Click to view demo](https://github.com/vicrab/vicrab-docs/tree/master/src/clients/java/modules/demo/vicrab-android-demo)

2 Guidelines for java development using java.util.logging
- [java.util.logging](https://www.vicrab.com/config/jul)> [click to view demo](https://github.com/vicrab/vicrab-docs/tree/master/src/clients/java/modules/demo/vicrab-utilog-demo)

3 Configuration guide using log4j version 1.x
- [Log4j 1.x](https://www.vicrab.com/config/log4j)> [click to view demo](https://github.com/vicrab/vicrab-docs/tree/master/src/clients/java/modules/demo/vicrab-log4j1-demo)

4 Configuration Guide for 2.x Version of Log4j
- [Log4j 2.x](https://www.vicrab.com/config/log4j2)> [click to view demo](https://github.com/vicrab/vicrab-docs/tree/master/src/clients/java/modules/demo/vicrab-log4j-demo)

5 Configuration guides using logback, usually in springboot projects
- [Logback](https://www.vicrab.com/config/logback) > [click to view demo](https://github.com/vicrab/vicrab-docs/tree/master/src/clients/java/modules/demo/vicrab-logback-demo)

## Configuration DSN 
After integrating SDK (Note: Configuration can only work when SDK is installed and integrated), functions can be set through configuration items. First, you need to configure the DSN

### What is DNS ?  (Data Source Name)

The DSN is the first and most important thing to configure because it tells the SDK where to send events. You can find your project’s DSN in the “Client Keys” section of your “Project Settings” in Vicrab.

### How to get the value of DSN?

First, you have to login to vicrab, then create a new project, click on the "settings" menu, select an item in the sub-menu item, click in to view the "client access" menu, and copy it in the input box below.

### How config dsn in your project?
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

## Configuration Completion Console View Data
   
[登陆账号](https://www.vicrab.com/login/) ，choose your project ,view your data 

