package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class AllModuleAction {
    public static Map<String, Object> moduleMap = new HashMap();
    public static Map<String, Map<String, Method>> servicesMethods = new HashMap();

    public static Object execute(String module, String action) {
        Map methodMap = servicesMethods.get(module);
        Object moduleObject = moduleMap.get(module);
        if (methodMap==null || moduleObject==null) {
            throw new RuntimeException(module+" module not exists");
        }
        if (methodMap.get(action)==null) {
            throw new RuntimeException("In "+module+" module, "+action+" action not exists");
        }
        try {
            Method method = servicesMethods.get(module).get(action);
            Object result = method.invoke(moduleObject, null);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
}
