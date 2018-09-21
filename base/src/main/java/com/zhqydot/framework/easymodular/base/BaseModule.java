package com.zhqydot.framework.easymodular.base;

import android.content.Context;
import android.util.Log;

import com.zhqydot.framework.easymodular.compiler.Module;
import com.zhqydot.framework.easymodular.core.IModule;

@Module
public class BaseModule implements IModule {
    @Override
    public void init(Context context) {
        Log.e("Test", "Base Module init");
    }
}
