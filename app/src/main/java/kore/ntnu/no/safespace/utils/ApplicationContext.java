package kore.ntnu.no.safespace.utils;

import android.app.Application;
import android.content.Context;

/**
 * The purpose of this class is to get the application context in places where it would be problematic to receive it.
 *
 * @author Kristoffer
 */
public class ApplicationContext extends Application {

    private static Application instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static Context getContext() {
        return instance.getApplicationContext();
    }


}
