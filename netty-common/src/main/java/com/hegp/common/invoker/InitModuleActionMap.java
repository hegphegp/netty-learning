package com.hegp.common.invoker;

import com.hegp.common.annotation.Action;
import com.hegp.common.annotation.Module;

import java.lang.reflect.Method;
import java.util.Map;

public class InitModuleActionMap {

    public static void initModuleAction(Object bean) {
        if (bean.getClass().isAnnotationPresent(Module.class)) { // 假如方法上面存在Module注解
            String clazzName = bean.getClass().getName();
            Byte moduleName = bean.getClass().getAnnotation(Module.class).module();
            if (moduleName==null) { // 不允许Module注解的内容为空
                throw new RuntimeException("In " + clazzName + " not allowed the value of @Module empty");
            }
            ModuleAction moduleAction = ModuleActionInvoker.moduleActionMap.get(moduleName);
            if (moduleAction != null) { // 同一个项目，不允许有Module注解内容相同的类
                throw new RuntimeException(moduleAction.getInvokeObject().getClass().getName() + " And " + clazzName + " not allowed same value @Module=" + moduleName);
            }
            moduleAction = new ModuleAction(bean);
            ModuleActionInvoker.moduleActionMap.put(moduleName, moduleAction);
            assemblyActionMethod(bean, moduleAction);
        }
    }

    public static void assemblyActionMethod(Object bean, ModuleAction moduleAction) {
        String clazzName = bean.getClass().getName();
        Method[] methods =  bean.getClass().getDeclaredMethods();
        Map<Byte, Method> actionMethodMap = moduleAction.getActionMethodMap();
        for(Method method:methods) {
            if(method.isAnnotationPresent(Action.class)) { // 假如方法上面存在Action注解
                Action action = method.getAnnotation(Action.class);
                Byte actionName = action.action();
                if (actionName==null) { // 不允许Action注解的内容为空
                    throw new RuntimeException("In "+clazzName+", "+method.getName()+" not allowed the value of @Action empty");
                }
                if (actionMethodMap.containsKey(actionName)) { // 同一个类里面，不允许Action注解的内容相同
                    throw new RuntimeException("In "+clazzName+", "+ actionMethodMap.get(actionName).getName()+" And "+method.getName()+" methods Not allowed same value @Action="+actionName);
                }
                actionMethodMap.put(actionName, method);
            }
        }
    }
}
