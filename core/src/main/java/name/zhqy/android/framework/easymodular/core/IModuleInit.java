package name.zhqy.android.framework.easymodular.core;

import android.content.Context;

public interface IModuleInit {
    void earlyInit(Context context);
    void lateInit(Context context);
    void delayInit(Context context);
}
