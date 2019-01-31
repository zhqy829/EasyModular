package com.zhqydot.framework.easymodular;

import android.app.Application;

import name.zhqy.android.framework.easymodular.core.ModuleManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        ModuleManager.init(this);
        ModuleManager.startDelayInit();
    }
}
