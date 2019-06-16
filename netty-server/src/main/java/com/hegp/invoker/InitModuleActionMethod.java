package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.Map;

import com.hegp.annotation.Action;
import com.hegp.annotation.Module;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InitModuleActionMethod implements ApplicationContextAware {

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		assemblyModuleObject(applicationContext);
	}

	private void assemblyModuleObject(ApplicationContext applicationContext) {
		// 查找所有bean当中，包含Module注解的bean
		Map<String, Object> beans = applicationContext.getBeansWithAnnotation(Module.class);

		for(String key:beans.keySet()) {
			Object bean = beans.get(key);
			if (bean.getClass().isAnnotationPresent(Module.class)) { // 假如方法上面存在Module注解
				String clazzName = bean.getClass().getName();
				String moduleName = bean.getClass().getAnnotation(Module.class).module();
				if (StringUtils.isEmpty(moduleName)) { // 不允许Module注解的内容为空
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
	}

	public void assemblyActionMethod(Object bean, ModuleAction moduleAction) {
		String clazzName = bean.getClass().getName();
		Method[] methods =  bean.getClass().getDeclaredMethods();
		Map<String, Method> actionMethodMap = moduleAction.getActionMethodMap();
		for(Method method:methods) {
			if(method.isAnnotationPresent(Action.class)) { // 假如方法上面存在Action注解
				Action action = method.getAnnotation(Action.class);
				String actionName = action.action();
				if (StringUtils.isEmpty(actionName)) { // 不允许Action注解的内容为空
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