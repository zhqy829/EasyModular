package name.zhqydot.android.framework.easymodular.core;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {

    private static Context sContext;
    private static List<IModule> sModuleList = new ArrayList<>();

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
        for (IModule module : sModuleList) {
            module.earlyInit(context);
        }
        for (IModule module : sModuleList) {
            module.lateInit(context);
        }
    }

    public static void register(IModule module) {
        sModuleList.add(module);
    }

    public static void startDelayInit() {
        for (IModule module : sModuleList) {
            module.delayInit(sContext);
        }
    }
}
