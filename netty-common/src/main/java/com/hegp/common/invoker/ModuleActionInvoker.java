package com.hegp.common.invoker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModuleActionInvoker {
    public final static Map<Byte, ModuleAction> moduleActionMap = new HashMap();

    public static Object execute(Byte module, Byte action) {
        ModuleAction moduleAction = moduleActionMap.get(module);
        if (moduleAction ==null) {
            throw new RuntimeException(module+" module not exists");
        }
        Method method = moduleAction.getActionMethodMap().get(action);
        if (method==null) {
            throw new RuntimeException("In "+module+" module, "+action+" action not exists");
        }
        try {
            Object result = method.invoke(moduleAction.getInvokeObject(), null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e.getLocalizedMessage(), e);
        }
    }
}
