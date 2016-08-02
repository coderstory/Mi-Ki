package com.coderstory.miui_toolkit.tools.CrashHelper;

/**
 * Created by cc on 2016/8/1.
 */

import android.app.Application;

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler crashHandler = CrashHandler.getInstance();
        crashHandler.init(getApplicationContext());
    }
}
