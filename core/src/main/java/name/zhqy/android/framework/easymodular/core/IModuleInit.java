package name.zhqy.android.framework.easymodular.core;

import android.app.Application;

public interface IModuleInit {
    void earlyInit(Application application);
    void lateInit(Application application);
    void delayInit(Application application);
}
