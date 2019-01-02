package name.zhqydot.android.framework.easymodular.core;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import name.zhqydot.android.framework.easymodular.compiler.IModuleDepend;

public class ModuleManager {

    private static Context sContext;
    private static List<IModuleInit> sModuleInitList = new ArrayList<>();
    private static Map<Class<? extends IModuleDepend>, IModuleDepend> sModuleDependMap = new HashMap<>();

    public static void init(Context context) {
        sContext = context.getApplicationContext();

        List<String> classes = ClassUtils.getClasses(context, "name.zhqydot.android.framework.easymodular.core.ModuleRegister$");
        for (String className : classes) {
            try {
                Class clazz = Class.forName(className);
                if (IModuleRegister.class.isAssignableFrom(clazz)) {
                    ((IModuleRegister) clazz.newInstance()).register();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("ModuleManager", e.getMessage());
            }
        }
        for (IModuleInit module : sModuleInitList) {
            module.earlyInit(context);
        }
        for (IModuleInit module : sModuleInitList) {
            module.lateInit(context);
        }
    }

    static <MD extends IModuleDepend> void registerModule(Class<MD> moduleDependClass, MD module) {
        if (!(module instanceof IModuleInit)) {
            throw new RuntimeException("Module must be implements IModuleInit.");
        }
        if (!moduleDependClass.isAssignableFrom(module.getClass())) {
            throw new RuntimeException("Module must be implements the depend interface.");
        }
        sModuleInitList.add((IModuleInit) module);
        sModuleDependMap.put(moduleDependClass, module);
    }

    @Nullable
    public static <MD extends IModuleDepend> MD getModuleDepend(Class<MD> moduleDependClass) {
        IModuleDepend moduleDepend = sModuleDependMap.get(moduleDependClass);
        return moduleDepend != null ? (MD) moduleDepend : null;
    }

    public static void startDelayInit() {
        for (IModuleInit moduleInit : sModuleInitList) {
            moduleInit.delayInit(sContext);
        }
        sModuleInitList.clear();
    }
}
