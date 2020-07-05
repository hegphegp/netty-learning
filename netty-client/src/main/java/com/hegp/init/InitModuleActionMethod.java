package com.hegp.init;

import com.hegp.common.invoker.InitModuleActionMap;
import com.hegp.modules.TestModule;
import com.hegp.modules.UserModule;

public class InitModuleActionMethod {
    public static void init() {
        InitModuleActionMap.initModuleAction(new TestModule());
        InitModuleActionMap.initModuleAction(new UserModule());
    }
}
