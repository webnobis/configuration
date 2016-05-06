package com.webnobis.configuration;

import java.nio.charset.Charset;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.webnobis.configuration.inject.ConfigurationFilesRootPath;
import com.webnobis.configuration.inject.ConfigurationLoader;
import com.webnobis.configuration.inject.ConstructorInjector;
import com.webnobis.configuration.inject.FieldInjector;
import com.webnobis.configuration.inject.SupportedConfigurationRules;

/**
 * Configuration service to create and inject classes with configuration values
 * or refresh injected values
 * 
 * @author steffen
 * 
 * @see SupportedConfigurationRules
 * @see ConfigurationFilesRootPath
 *
 */
public abstract class Configuration {

	private static final Map<String, String> configurations = new ConcurrentHashMap<>();

	private static final ConstructorInjector parameterInjector = new ConstructorInjector(configurations);

	private static final FieldInjector fieldInjector = new FieldInjector(configurations);

	private Configuration() {
	}

	/**
	 * Loads or refresh all configuration pairs.
	 * 
	 * @see SupportedConfigurationRules#defaultCharset()
	 */
	public static void loadConfigurations() {
		loadConfigurations(null);
	}

	/**
	 * Loads or refresh all configuration pairs.
	 * 
	 * @param charset
	 *            the charset of the configuration files
	 */
	public static void loadConfigurations(Charset charset) {
		configurations.clear();
		configurations.putAll(new ConfigurationLoader(charset).loadConfiguration());
	}

	/**
	 * 
	 * @param type
	 *            the class to create a instance from and inject with
	 *            configuration values
	 * @return injected instance
	 */
	public static <T> T createWithConfigurations(Class<T> type) {
		T instance = parameterInjector.injectConstructorConfigurationsAndCreateInstance(type);
		return refreshConfigurations(instance);
	}

	/**
	 * 
	 * @param instance
	 *            the instance to refresh the injected values
	 * @return instance with the refreshed injections
	 */
	public static <T> T refreshConfigurations(T instance) {
		return fieldInjector.injectFieldConfigurations(instance);
	}
	
	/**
	 * Clears all loaded configurations.
	 */
	static void emptyConfigurations() {
		configurations.clear();
	}

}
