package com.hegp;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import com.hegp.annotation.Action;
import com.hegp.annotation.Module;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class InitAllModuleAction implements ApplicationListener<ContextRefreshedEvent>, Ordered{

	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}


	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		// 保存每个module取值对应的类名全路径，每个Module的注解的module值不允许为空，不允许相同
		Map<String, String> map = new HashMap();

		// 查找所有bean当中，包含Module主机的bean
		Map<String, Object> beans = event.getApplicationContext().getBeansWithAnnotation(Module.class);
		
		for(String key:beans.keySet()) {
			Object bean = beans.get(key);
			if (bean.getClass().isAnnotationPresent(Module.class)) {
				String clazzName = bean.getClass().getName();
				String moduleValue = bean.getClass().getAnnotation(Module.class).module();
				if (StringUtils.isEmpty(moduleValue)) {
					throw new RuntimeException("In " + clazzName + " not allowed the value of @Module empty");
				}
				if (map.get(moduleValue) != null) {
					throw new RuntimeException(map.get(moduleValue) + " And " + clazzName + " not allowed same value @Module=" + moduleValue);
				} else {
					map.put(moduleValue, clazzName);
				}
				AllModuleAction.moduleMap.put(moduleValue, bean);
				Map<String, String> actionMap = new HashMap();
				Method[] methods =  bean.getClass().getDeclaredMethods();
				for(Method m:methods) {
					//假如方法上面存在Action注解
					if(m.isAnnotationPresent(Action.class)){
						Action action = m.getAnnotation(Action.class);
						String actionValue = action.action();
						if (StringUtils.isEmpty(actionValue)) {
							throw new RuntimeException("In "+clazzName+", "+m.getName()+" not allowed the value of @Action empty");
						}
						if (actionMap.get(actionValue)!=null) {
							throw new RuntimeException("In "+clazzName+", "+actionMap.get(actionValue)+" And "+m.getName()+" Not allowed same value @Action="+actionValue);
						} else {
							actionMap.put(actionValue, m.getName());
						}
						Map<String, Method> methodMap = AllModuleAction.servicesMethods.get(moduleValue);
						if (methodMap==null) {
							methodMap = new HashMap();
							AllModuleAction.servicesMethods.put(moduleValue, methodMap);
						}
						methodMap.put(actionValue, m);
					}
				}
			}
			
		}
	}
}