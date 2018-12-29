package com.zhqydot.framework.easymodular;

import android.content.Context;
import android.util.Log;

import name.zhqydot.android.framework.easymodular.compiler.Module;
import name.zhqydot.android.framework.easymodular.core.IModule;

@Module
public class AppModule implements IModule {

    @Override
    public void earlyInit(Context context) {
        Log.e("Test", "App Module early init");
    }

    @Override
    public void lateInit(Context context) {
        Log.e("Test", "App Module late init");
    }

    @Override
    public void delayInit(Context context) {
        Log.e("Test", "App Module delay init");
    }
}
