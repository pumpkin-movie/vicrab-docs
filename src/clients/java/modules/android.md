---
title: Android
sidebar_order: 6
---

## Features

The Vicrab Android SDK is built on top of the main Java SDK and supports all of the same features, [configuration options], and more. Adding version `1.1` of the Android SDK to a sample application that doesn’t even use Proguard only increased the release `.apk` size by approximately 200KB.

Events will be [buffered to disk]({%- link _documentation/clients/java/config.md -%}#buffering-events-to-disk) (in the application’s cache directory) by default. This allows events to be sent at a later time if the device does not have connectivity when an event is created. This can be disabled by [setting the option] `buffer.enabled` to `false`.

An `UncaughtExceptionHandler` is configured so that crash events will be stored to disk and sent the next time the application is run.

The `AndroidEventBuilderHelper` is enabled by default, which will automatically enrich events with data about the current state of the device, such as memory usage, storage usage, display resolution, connectivity, battery level, model, Android version, whether the device is rooted or not, etc.

<!-- WIZARD -->
## Installation

Using Gradle (Android Studio) in your `app/build.gradle` add:

```groovy

implementation 'com.vicrab:vicrab-all:1.1'
implementation 'com.vicrab:vicrab:1.1'
implementation 'com.vicrab:vicrab-android:1.1'

//dependency
```

For other dependency managers see the [central Maven repository](https://search.maven.org/#artifactdetails%7Ccom.vicrab%7Cvicrab-android%7C1.1%7Cjar).
<!-- ENDWIZARD -->

## Initialization

Your application must have permission to access the internet in order to send events to the Vicrab server. In your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

Then initialize the Vicrab client in your application’s main `onCreate` method:

```java
import com.vicrab.Vicrab;
import com.vicrab.android.AndroidVicrabClientFactory;

public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Context ctx = this.getApplicationContext();

        // Use the Vicrab DSN (client key) from the Project Settings page on Vicrab
        String vicrabDsn = "https://secretKey@host:port/1?options";
        try {
            Vicrab.init(vicrabDsn, new AndroidVicrabClientFactory(ctx));
        } catch (NoSuchMethodError e) {
            e.printStackTrace();
        } catch (IncompatibleClassChangeError error){
            error.printStackTrace();
        }catch(NoClassDefFoundError error) {
            error.printStackTrace();
        }catch (Exception e){
            e.printStackTrace();
        }
        
        
        //Example of calling a unit test method
        new MyClass().logWithStaticAPI();
    }
}
```

You can optionally configure other values such as `environment` and `release`. [See the configuration page]({%- link _documentation/clients/java/config.md -%}#configuration) for ways you can do this.

<!-- WIZARD -->
## Usage

Now you can use `Vicrab` to capture events anywhere in your application:

```java
import com.vicrab.context.Context;
import com.vicrab.event.BreadcrumbBuilder;
import com.vicrab.event.UserBuilder;

public class MyClass {
    /**
      * An example method that throws an exception.
      */
    void unsafeMethod() {
        throw new UnsupportedOperationException("You shouldn't call this!");
    }

    /**
      * Note that the ``Vicrab.init`` method must be called before the static API
      * is used, otherwise a ``NullPointerException`` will be thrown.
      */
    void logWithStaticAPI() {
        /*
         Record a breadcrumb in the current context which will be sent
         with the next event(s). By default the last 100 breadcrumbs are kept.
         */
        Vicrab.getContext().recordBreadcrumb(
            new BreadcrumbBuilder().setMessage("User made an action").build()
        );

        // Set the user in the current context.
        Vicrab.getContext().setUser(
            new UserBuilder().setEmail("developer@vicrab.com").build()
        );

        /*
         This sends a simple event to Vicrab using the statically stored instance
         that was created in the ``main`` method.
         */

        try {
            unsafeMethod();
        } catch (Exception e) {
            // This sends an exception event to Vicrab using the statically stored instance
            // that was created in the ``main`` method.
            Vicrab.capture(e);
        }
    }
}
```
<!-- ENDWIZARD -->

