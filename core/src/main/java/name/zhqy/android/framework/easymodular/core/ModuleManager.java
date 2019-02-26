package name.zhqy.android.framework.easymodular.core;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import name.zhqy.android.framework.easymodular.compiler.IModuleDepend;

public class ModuleManager {

    private static Application sApplication;
    private static Set<PriorityModule> sPriorityModuleSet = new TreeSet<>();
    private static Map<Class<? extends IModuleDepend>, IModuleDepend> sModuleDependMap = new HashMap<>();

    public static void init(Application application) {
        sApplication = application;

        List<String> classes = ClassUtils.getClasses(sApplication, "name.zhqy.android.framework.easymodular.core.ModuleRegister$");
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
        for (PriorityModule priorityModule : sPriorityModuleSet) {
            priorityModule.module.earlyInit(sApplication);
        }
        for (PriorityModule priorityModule : sPriorityModuleSet) {
            priorityModule.module.lateInit(sApplication);
        }
    }

    static <MD extends IModuleDepend> void registerModule(Class<MD> moduleDependClass, MD module, int priority) {
        if (!(module instanceof IModuleInit)) {
            throw new RuntimeException("Module must be implements IModuleInit.");
        }
        if (!moduleDependClass.isAssignableFrom(module.getClass())) {
            throw new RuntimeException("Module must be implements the depend interface.");
        }
        sPriorityModuleSet.add(new PriorityModule((IModuleInit) module, priority));
        sModuleDependMap.put(moduleDependClass, module);
    }

    @Nullable
    public static <MD extends IModuleDepend> MD getModuleDepend(Class<MD> moduleDependClass) {
        IModuleDepend moduleDepend = sModuleDependMap.get(moduleDependClass);
        return moduleDepend != null ? (MD) moduleDepend : null;
    }

    public static void startDelayInit() {
        for (PriorityModule priorityModule : sPriorityModuleSet) {
            priorityModule.module.delayInit(sApplication);
        }
        sPriorityModuleSet.clear();
    }

    private static class PriorityModule implements Comparable<PriorityModule> {

        private IModuleInit module;
        private int priority;

        public PriorityModule(IModuleInit module, int priority) {
            this.module = module;
            this.priority = priority;
        }

        @Override
        public int compareTo(@NonNull PriorityModule o) {
            if (priority > o.priority) {
                return -1;
            } else {
                return 1;
            }
        }
    }
}
