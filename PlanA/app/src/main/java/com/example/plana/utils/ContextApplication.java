package com.example.plana.utils;

import android.app.Application;
import android.content.Context;

/**
 * @program: PlanA
 * @description: 获取 Context
 */
public class ContextApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        ContextApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ContextApplication.context;
    }

}
