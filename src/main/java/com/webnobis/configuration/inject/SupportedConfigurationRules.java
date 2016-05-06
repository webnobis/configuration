package com.webnobis.configuration.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Supported configuration rules
 * 
 * @author steffen
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface SupportedConfigurationRules {

	/**
	 * 
	 * @return used file extensions
	 */
	String[] fileExtensions();

	/**
	 * 
	 * @return used separator between key and value
	 */
	String keyValueSeparator();

	/**
	 * 
	 * @return start of comment line
	 */
	String commentStartToken();

	/**
	 * 
	 * @return default charset
	 */
	String defaultCharset();
}
