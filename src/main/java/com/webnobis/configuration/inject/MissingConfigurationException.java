package com.webnobis.configuration.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Parameter;

/**
 * Configuration is missed or the configuration key is unknown.
 * 
 * @author steffen
 *
 */
public class MissingConfigurationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * 
	 * @param injectableClass
	 *            class
	 * @param parameter
	 *            named constructor parameter
	 * @param configurationKey
	 *            value of Named annotation
	 */
	public MissingConfigurationException(Class<?> injectableClass, Parameter parameter, String configurationKey) {
		super(String.format("missing configuration of key '%s' on class %s, at constructor parameter %s of type %s",
				configurationKey, injectableClass.getSimpleName(), parameter.getName(), parameter.getType().getSimpleName()));
	}

	/**
	 * 
	 * @param injectableClass
	 *            class
	 * @param field
	 *            named field
	 * @param configurationKey
	 *            value of Named annotation
	 */
	public MissingConfigurationException(Class<?> injectableClass, Field field, String configurationKey) {
		super(String.format("missing configuration of key '%s' on class %s, at contained field %s of type %s",
				configurationKey, injectableClass.getSimpleName(), field.getName(), field.getType().getSimpleName()));
	}

}
