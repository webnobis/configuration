package com.webnobis.configuration.inject;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Root path to search for configuration files, also within the class path
 * 
 * @author steffen
 *
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ConfigurationFilesRootPath {

	/**
	 * 
	 * @return root path, also relative path is supported
	 */
	String value();

}
