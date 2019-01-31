package com.zhqydot.framework.easymodular.base;

import android.content.Context;
import android.util.Log;

import name.zhqy.android.framework.easymodular.compiler.Module;
import name.zhqy.android.framework.easymodular.core.IModuleInit;

@Module(depend = BaseModuleDepend.class)
public class BaseModule implements IModuleInit, BaseModuleDepend {

    @Override
    public void earlyInit(Context context) {
        Log.e("Test", "Base Module early init");
    }

    @Override
    public void lateInit(Context context) {
        Log.e("Test", "Base Module late init");
    }

    @Override
    public void delayInit(Context context) {
        Log.e("Test", "Base Module delay init");
    }

    @Override
    public void base() {
        Log.e("Test", "BaseModuleDepend base()");
    }
}
