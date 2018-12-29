package name.zhqydot.android.framework.easymodular.core;

import android.content.Context;

public interface IModule {
    void earlyInit(Context context);
    void lateInit(Context context);
    void delayInit(Context context);
}
