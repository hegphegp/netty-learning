package com.hegp.invoker;

import java.lang.reflect.Method;
import java.util.Map;

import com.hegp.annotation.Action;
import com.hegp.annotation.Module;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InitAllModuleInfo implements ApplicationListener<ContextRefreshedEvent>, Ordered{

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {

		// 查找所有bean当中，包含Module主机的bean
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Module.class);

		Map<String, ModuleInfo> moduleInfoMap = ModuleInfoInvoker.moduleInfoMap;
		for(String key:beans.keySet()) {
			Object bean = beans.get(key);
			if (bean.getClass().isAnnotationPresent(Module.class)) { // 假如方法上面存在Module注解
				String clazzName = bean.getClass().getName();
				String moduleName = bean.getClass().getAnnotation(Module.class).module();
				if (StringUtils.isEmpty(moduleName)) { // 不允许Module注解的内容为空
					throw new RuntimeException("In " + clazzName + " not allowed the value of @Module empty");
				}
				ModuleInfo moduleInfo = moduleInfoMap.get(moduleName);
				if (moduleInfo != null) { // 同一个项目，不允许有Module注解内容相同的类
					throw new RuntimeException(moduleInfo.getInvokeObject().getClass().getName() + " And " + clazzName + " not allowed same value @Module=" + moduleName);
				}
				moduleInfo = new ModuleInfo(bean);
				moduleInfoMap.put(moduleName, moduleInfo);
				Map<String, Method> actionMap = moduleInfo.getActionMethodMap();
				Method[] methods =  bean.getClass().getDeclaredMethods();
				for(Method method:methods) {
					if(method.isAnnotationPresent(Action.class)) { // 假如方法上面存在Action注解
						Action action = method.getAnnotation(Action.class);
						String actionName = action.action();
						if (StringUtils.isEmpty(actionName)) { // 不允许Action注解的内容为空
							throw new RuntimeException("In "+clazzName+", "+method.getName()+" not allowed the value of @Action empty");
						}
						if (actionMap.containsKey(actionName)) { // 同一个类里面，不允许Action注解的内容相同
							throw new RuntimeException("In "+clazzName+", "+actionMap.get(actionName).getName()+" And "+method.getName()+" methods Not allowed same value @Action="+actionName);
						}
						actionMap.put(actionName, method);
					}
				}
			}
			
		}
	}
}