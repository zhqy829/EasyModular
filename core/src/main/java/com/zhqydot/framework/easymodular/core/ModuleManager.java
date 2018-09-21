package com.zhqydot.framework.easymodular.core;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ModuleManager {
    public static void init(Context context) {
        try {
            Class clazz = Class.forName("com.zhqydot.framework.easymodular.core.ModuleLoader");
            Object obj = clazz.newInstance();
            Method method = clazz.getDeclaredMethod("load");
            method.invoke(obj);
        } catch (ClassNotFoundException | IllegalAccessException |
                InstantiationException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
            Log.e("ModuleManager", e.getMessage());
        }
    }
}
