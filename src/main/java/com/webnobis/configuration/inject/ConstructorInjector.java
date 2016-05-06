package com.webnobis.configuration.inject;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;

import javax.inject.Inject;
import javax.inject.Named;

import com.webnobis.configuration.transform.ConfigurationTransformer;

/**
 * Injects all non-final constructor parameters with the current configurations,
 * where the key matched by Named annotations value and the constructor has the
 * Inject annotation.
 * 
 * @author steffen
 * 
 * @see Named
 * @see Inject
 * @see ConfigurationLoader#loadConfiguration()
 *
 */
public class ConstructorInjector {

	private static final int MAX_DEPTH = 10;

	private final Map<String, String> configurations;

	/**
	 * 
	 * @param configurations
	 *            all known configuration pairs
	 */
	public ConstructorInjector(Map<String, String> configurations) {
		this.configurations = Objects.requireNonNull(configurations,
				"configurations is null");
	}

	/**
	 * 
	 * @param type
	 *            the class to create a new injected instance from
	 * @return injected instance
	 * @throws MissingConfigurationException
	 *             if the annotated parameters configuration key not found
	 * @throws MaxClassDepthReachedException
	 *             if the max class depth reached
	 * @throws IllegalStateException
	 *             if injection failed
	 */
	public <T> T injectConstructorConfigurationsAndCreateInstance(Class<T> type) {
		return injectConstructorConfigurationsAndCreateInstance(type, 0);
	}

	private <T> T injectConstructorConfigurationsAndCreateInstance(Class<T> type, int depth) {
		if (depth > MAX_DEPTH) {
			throw new IllegalStateException("max depth of classes reached");
		}

		return Arrays.stream(type.getDeclaredConstructors())
				.filter(this::isInjectableConstructor)
				.findFirst()
				.<T> map(constructor -> injectConstructorConfigurationsAndCreateInstance(constructor, type, depth))
				.orElseThrow(() ->
						new IllegalStateException(String.format("missing constructor on %s, annotated with %s",
								type.getName(), Inject.class.getName())));
	}

	private boolean isInjectableConstructor(Constructor<?> constructor) {
		return constructor.getParameterCount() < 1 || constructor.isAnnotationPresent(Inject.class);
	}

	@SuppressWarnings("unchecked")
	private <T> T injectConstructorConfigurationsAndCreateInstance(Constructor<?> constructor, Class<?> type, int depth) {
		boolean access = constructor.isAccessible();
		try {
			constructor.setAccessible(true);
			if (constructor.getParameterCount() < 1) {
				return (T) constructor.newInstance();
			}

			return (T) constructor.newInstance(handleInjectableParameters(constructor, type, depth));
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException e) {
			throw new IllegalStateException(e.getMessage(), e);
		} finally {
			constructor.setAccessible(access);
		}
	}

	private Object[] handleInjectableParameters(Constructor<?> constructor, Class<?> type, int depth) {
		return Arrays.stream(constructor.getParameters())
				.map(parameter -> handleInjectableParameter(parameter, type, depth))
				.toArray();
	}

	private Object handleInjectableParameter(Parameter parameter, Class<?> type, int depth) {
		if (parameter.isAnnotationPresent(Named.class)) {
			String configurationKey = parameter.getAnnotation(Named.class).value();
			if (configurations.containsKey(configurationKey)) {
				return ConfigurationTransformer.transform(parameter.getType(), configurations.get(configurationKey));
			} else {
				throw new MissingConfigurationException(type, parameter, configurationKey);
			}
		} else if (depth < MAX_DEPTH) {
			return injectConstructorConfigurationsAndCreateInstance(parameter.getType(), depth + 1);
		} else {
			throw new MaxClassDepthReachedException(MAX_DEPTH);
		}
	}

}
