package com.zhqydot.framework.easymodular;

import android.app.Application;
import android.util.Log;

import name.zhqy.android.framework.easymodular.compiler.Module;
import name.zhqy.android.framework.easymodular.core.IModuleInit;

@Module(depend = AppModuleDepend.class)
public class AppModule implements IModuleInit, AppModuleDepend {

    @Override
    public void earlyInit(Application application) {
        Log.e("Test", "App Module early init");
    }

    @Override
    public void lateInit(Application application) {
        Log.e("Test", "App Module late init");
    }

    @Override
    public void delayInit(Application application) {
        Log.e("Test", "App Module delay init");
    }

    @Override
    public void app() {
        Log.e("Test", "AppModule app()");
    }
}
