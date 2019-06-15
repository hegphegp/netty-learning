package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModuleInfoInvoker {
    public final static Map<String, ModuleInfo> moduleInfoMap = new HashMap();

    public static Object execute(String module, String action) {
        ModuleInfo moduleInfo = moduleInfoMap.get(module);
        if (moduleInfo ==null) {
            throw new RuntimeException(module+" module not exists");
        }
        Method method = moduleInfo.getActionMethodMap().get(action);
        if (method==null) {
            throw new RuntimeException("In "+module+" module, "+action+" action not exists");
        }
        try {
            Object result = method.invoke(moduleInfo.getInvokeObject(), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }

    }
}
