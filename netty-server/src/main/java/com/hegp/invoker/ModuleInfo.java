package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModuleInfo {
    private Object invokeObject;
    private Map<String, Method> actionMethodMap = new HashMap();

    public ModuleInfo() { }

    public ModuleInfo(Object invokeObject) {
        this.invokeObject = invokeObject;
    }

    public Object getInvokeObject() {
        return invokeObject;
    }

    public void setInvokeObject(Object invokeObject) {
        this.invokeObject = invokeObject;
    }

    public Map<String, Method> getActionMethodMap() {
        return actionMethodMap;
    }

    public void setActionMethodMap(Map<String, Method> actionMethodMap) {
        this.actionMethodMap = actionMethodMap;
    }
}
