package com.webnobis.configuration.inject;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Named;

import com.webnobis.configuration.transform.ConfigurationTransformer;

/**
 * Injects or overwrite all non-final and non-abstract fields with the current
 * configurations, where the key matched by Named annotations value.
 * 
 * @author steffen
 * 
 * @see Named
 * @see ConfigurationLoader#loadConfiguration()
 *
 */
public class FieldInjector {

	private static final int MAX_DEPTH = 10;

	private static final String SUPPORTED_TYPES = ConfigurationTransformer.getSupportedTypes().toLowerCase();

	private final Map<String, String> configurations;

	/**
	 * 
	 * @param configurations
	 *            all known configuration pairs
	 */
	public FieldInjector(Map<String, String> configurations) {
		this.configurations = Objects.requireNonNull(configurations,
				"configurations is null");
	}

	/**
	 * 
	 * @param instance
	 *            the instance to inject
	 * @return injected instance
	 * @throws MissingConfigurationException
	 *             if the annotated fields configuration key not found
	 * @throws MaxClassDepthReachedException
	 *             if the max class depth reached
	 * @throws IllegalStateException
	 *             if injection failed
	 */
	public <T> T injectFieldConfigurations(T instance) {
		injectFieldConfigurations(instance, 0);
		return instance;
	}

	private void injectFieldConfigurations(Object instance, int depth) {
		Optional.ofNullable(instance)
				.map(t -> t.getClass())
				.ifPresent(c -> Arrays.stream(c.getDeclaredFields())
						.forEach(field -> handleInjectableField(field, instance, depth))
				);
	}

	private boolean isInjectableField(Field field) {
		int modifier = field.getModifiers();
		return field.isAnnotationPresent(Inject.class) && field.isAnnotationPresent(Named.class) &&
				!Modifier.isFinal(modifier) && !Modifier.isAbstract(modifier);
	}

	private void handleInjectableField(Field field, Object instance, int depth) {
		boolean access = field.isAccessible();
		try {
			field.setAccessible(true);
			if (isInjectableField(field)) {
				String configurationKey = field.getAnnotation(Named.class).value();
				if (configurations.containsKey(configurationKey)) {
					injectFieldConfigurations(field, instance, configurationKey);
				} else {
					throw new MissingConfigurationException(instance.getClass(), field, configurationKey);
				}
			} else if (!SUPPORTED_TYPES.contains(field.getType().getName().toLowerCase())) {
				if (depth < MAX_DEPTH) {
					try {
						injectFieldConfigurations(field.get(instance), depth + 1);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException(e.getMessage(), e);
					}
				} else {
					throw new MaxClassDepthReachedException(MAX_DEPTH);
				}
			}
		} finally {
			field.setAccessible(access);
		}
	}

	private void injectFieldConfigurations(Field field, Object instance, String configurationKey) {
		try {
			field.set(instance, ConfigurationTransformer.transform(field.getType(),
					configurations.get(configurationKey)));
		} catch (IllegalArgumentException | IllegalAccessException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

}
