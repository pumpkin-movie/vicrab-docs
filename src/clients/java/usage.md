---
title: 'Manual Usage'
sidebar_order: 2
---

<!-- WIZARD installation -->
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
<!-- ENDWIZARD -->

<!-- WIZARD capture-an-error -->
## Capture an Error

To report an event manually you need to initialize a `VicrabClient`. It is recommended that you use the static API via the `Vicrab` class, but you can also construct and manage your own `VicrabClient` instance. An example of each style is shown below:

```java
import com.vicrab.context.Context;
import com.vicrab.event.BreadcrumbBuilder;
import com.vicrab.event.UserBuilder;

public class MyClass {
    private static VicrabClient vicrab;

    public static void main(String... args) {
        /*
         It is recommended that you use the DSN detection system, which
         will check the environment variable "VICRAB_DSN", the Java
         System Property "vicrab.dsn", or the "vicrab.properties" file
         in your classpath. This makes it easier to provide and adjust
         your DSN without needing to change your code. See the configuration
         page for more information.
         */
        Vicrab.init();

        // You can also manually provide the DSN to the ``init`` method.
        String dsn = args[0];
        Vicrab.init(dsn);

        /*
         It is possible to go around the static ``Vicrab`` API, which means
         you are responsible for making the VicrabClient instance available
         to your code.
         */
        vicrab = VicrabClientFactory.vicrabClient();

        MyClass myClass = new MyClass();
        myClass.logWithStaticAPI();
        myClass.logWithInstanceAPI();
    }

    /**
      * An example method that throws an exception.
      */
    void unsafeMethod() {
        throw new UnsupportedOperationException("You shouldn't call this!");
    }

    /**
      * Examples using the (recommended) static API.
      */
    void logWithStaticAPI() {
        // Note that all fields set on the context are optional. Context data is copied onto
        // all future events in the current context (until the context is cleared).

        // Record a breadcrumb in the current context. By default the last 100 breadcrumbs are kept.
        Vicrab.getContext().recordBreadcrumb(
            new BreadcrumbBuilder().setMessage("User made an action").build()
        );

        // Set the user in the current context.
        Vicrab.getContext().setUser(
            new UserBuilder().setEmail("hello@vicrab.com").build()
        );

        // Add extra data to future events in this context.
        Vicrab.getContext().addExtra("extra", "thing");

        // Add an additional tag to future events in this context.
        Vicrab.getContext().addTag("tagName", "tagValue");

        /*
         This sends a simple event to Vicrab using the statically stored instance
         that was created in the ``main`` method.
         */
        Vicrab.capture("This is a test");

        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Vicrab using the statically stored instance
            // that was created in the ``main`` method.
            Vicrab.capture(e);
        }
    }

    /**
      * Examples that use the VicrabClient instance directly.
      */
    void logWithInstanceAPI() {
        // Retrieve the current context.
        Context context = vicrab.getContext();

        // Record a breadcrumb in the current context. By default the last 100 breadcrumbs are kept.
        context.recordBreadcrumb(new BreadcrumbBuilder().setMessage("User made an action").build());

        // Set the user in the current context.
        context.setUser(new UserBuilder().setEmail("hello@vicrab.com").build());

        // This sends a simple event to Vicrab.
        vicrab.sendMessage("This is a test");

        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Vicrab.
            vicrab.sendException(e);
        }
    }
}
```
<!-- ENDWIZARD -->

### Building More Complex Events

For more complex messages, you’ll need to build an `Event` with the `EventBuilder` class:

```java
   import com.vicrab.Vicrab;
   import com.vicrab.event.Event;
   import com.vicrab.event.EventBuilder;
   import com.vicrab.event.interfaces.ExceptionInterface;

   public class MyClass {
       public static void main(String... args) {
           Vicrab.init();
       }

       void unsafeMethod() {
           throw new UnsupportedOperationException("You shouldn't call this!");
       }

       void logSimpleMessage() {
           // This sends an event to Vicrab.
           EventBuilder eventBuilder = new EventBuilder()
                           .withMessage("This is a test")
                           .withLevel(Event.Level.INFO)
                           .withLogger(MyClass.class.getName());

           // Note that the *unbuilt* EventBuilder instance is passed in so that
           // EventBuilderHelpers are run to add extra information to your event.
           Vicrab.capture(eventBuilder);
       }

       void logException() {
           try {
               unsafeMethod();
           } catch (Exception e) {
               // This sends an exception event to Vicrab.
               EventBuilder eventBuilder = new EventBuilder()
                               .withMessage("Exception caught")
                               .withLevel(Event.Level.ERROR)
                               .withLogger(MyClass.class.getName())
                               .withVicrabInterface(new ExceptionInterface(e));

               // Note that the *unbuilt* EventBuilder instance is passed in so that
               // EventBuilderHelpers are run to add extra information to your event.
               Vicrab.capture(eventBuilder);
           }
       }
}
```

### Automatically Enhancing Events

You can also implement an `EventBuilderHelper` that is able to automatically enhance outgoing events.

```java
import com.vicrab.Vicrab;
import com.vicrab.VicrabClient;
import com.vicrab.event.EventBuilder;
import com.vicrab.event.helper.EventBuilderHelper;

public class MyClass {
    public void myMethod() {
        VicrabClient client = Vicrab.getStoredClient();

        EventBuilderHelper myEventBuilderHelper = new EventBuilderHelper() {
            @Override
            public void helpBuildingEvent(EventBuilder eventBuilder) {
                eventBuilder.withMessage("Overwritten by myEventBuilderHelper!");
            }
        };

        // Add an ``EventBuilderHelper`` to the current client instance. Note that
        // this helper will process *all* future events.
        client.addBuilderHelper(myEventBuilderHelper);

        // Send an event to Vicrab. During construction of the event the message
        // body will be overwritten by ``myEventBuilderHelper``.
        Vicrab.capture("Hello, world!");
    }
}
```
