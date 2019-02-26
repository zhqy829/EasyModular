package com.zhqydot.framework.easymodular.base;

import android.app.Application;
import android.util.Log;

import name.zhqy.android.framework.easymodular.compiler.Module;
import name.zhqy.android.framework.easymodular.core.IModuleInit;

@Module(depend = BaseModuleDepend.class)
public class BaseModule implements IModuleInit, BaseModuleDepend {

    @Override
    public void earlyInit(Application application) {
        Log.e("Test", "Base Module early init");
    }

    @Override
    public void lateInit(Application application) {
        Log.e("Test", "Base Module late init");
    }

    @Override
    public void delayInit(Application application) {
        Log.e("Test", "Base Module delay init");
    }

    @Override
    public void base() {
        Log.e("Test", "BaseModuleDepend base()");
    }
}
