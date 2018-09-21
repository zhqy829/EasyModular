package com.zhqydot.framework.easymodular;

import android.content.Context;
import android.util.Log;

import com.zhqydot.framework.easymodular.compiler.Module;
import com.zhqydot.framework.easymodular.core.IModule;

@Module
public class AppModule implements IModule {

    @Override
    public void init(Context context) {
        Log.e("Test", "App Module init");
    }
}
