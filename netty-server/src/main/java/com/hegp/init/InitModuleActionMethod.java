package com.hegp.init;

import java.util.Map;

import com.hegp.common.annotation.Module;
import com.hegp.common.invoker.InitModuleActionMap;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

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
			InitModuleActionMap.initModuleAction(bean);
		}
	}


}