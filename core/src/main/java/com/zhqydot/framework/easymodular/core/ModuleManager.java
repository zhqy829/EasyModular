package com.zhqydot.framework.easymodular.core;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class ModuleManager {
    public static void init(Context context) {
        List<String> classes = ClassUtils.getClasses(context, "com.zhqydot.framework.easymodular.core.ModuleLoader");
        for (String className : classes) {
            try {
                Class clazz = Class.forName(className);
                Object obj = clazz.newInstance();
                Method method = clazz.getDeclaredMethod("load", Context.class);
                method.invoke(obj, context);
            } catch (ClassNotFoundException | IllegalAccessException |
                    InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
                Log.e("ModuleManager", e.getMessage());
            }
        }
    }
}
