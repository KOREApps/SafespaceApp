package kore.ntnu.no.safespace.utils;

import android.app.Application;
import android.content.Context;

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
