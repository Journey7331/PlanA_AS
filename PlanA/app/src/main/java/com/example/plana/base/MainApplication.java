package com.example.plana.base;

import android.app.Application;
import android.content.Context;

/**
 * @program: PlanA
 * @description: 获取 Context
 */
public class MainApplication extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        MainApplication.context = getApplicationContext();


    }

    public static Context getAppContext() {
        return MainApplication.context;
    }

}