package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class ModuleAction {
    /** invokeObject : 调用对象，就是每个@Module注解的类的实例对象 */
    private Object invokeObject;
    /** actionMethodMap : 在@Module注解的实例对象中，存储@Action注解字符串与目标方法的Map映射 */
    private Map<String, Method> actionMethodMap = new HashMap();

    public ModuleAction() { }

    public ModuleAction(Object invokeObject) {
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
