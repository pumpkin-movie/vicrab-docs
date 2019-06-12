package com.vicrab.android.demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.vicrab.Vicrab;
import com.vicrab.android.AndroidVicrabClientFactory;
import com.vicrab.event.BreadcrumbBuilder;
import com.vicrab.event.UserBuilder;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Context ctx = this.getApplicationContext();

        // Use the Vicrab DSN (client key) from the Project Settings page on Vicrab
        String vicrabDsn = "https://8b7824b0e1084ffb1a738f2f026047c8@a.vicrab.com/77083";
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

    class MyClass{
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
}
