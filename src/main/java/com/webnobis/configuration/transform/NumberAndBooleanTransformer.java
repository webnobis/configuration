package com.webnobis.configuration.transform;

import java.util.Optional;

/**
 * Transforms numbers or boolean values
 * 
 * @author steffen
 *
 */
abstract class NumberAndBooleanTransformer {

	private NumberAndBooleanTransformer() {
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as integer
	 */
	public static Integer toInteger(String value) {
		return Optional.ofNullable(value).map(Integer::valueOf).orElse(0);
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as long
	 */
	public static Long toLong(String value) {
		return Optional.ofNullable(value).map(Long::valueOf).orElse(0L);
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as double
	 */
	public static Double toDouble(String value) {
		return Optional.ofNullable(value).map(Double::valueOf).orElse(0.0);
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as float
	 */
	public static Float toFloat(String value) {
		return Optional.ofNullable(value).map(Float::valueOf).orElse(0.0f);
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as boolean
	 */
	public static Boolean toBoolean(String value) {
		return Optional.ofNullable(value).map(Boolean::valueOf).orElse(false);
	}

}
