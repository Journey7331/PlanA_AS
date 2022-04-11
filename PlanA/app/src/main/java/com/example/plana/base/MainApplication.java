package com.example.plana.base;

import android.app.Application;
import android.content.Context;
import android.util.Log;

import com.example.plana.activity.SplashActivity;
import com.example.plana.config.Constant;

import cn.leancloud.LCInstallation;
import cn.leancloud.LCLogger;
import cn.leancloud.LCObject;
import cn.leancloud.LeanCloud;
import cn.leancloud.push.PushService;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
