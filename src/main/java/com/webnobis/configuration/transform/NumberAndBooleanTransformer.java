package com.webnobis.configuration.transform;

import java.util.Optional;

abstract class NumberAndBooleanTransformer {

	private NumberAndBooleanTransformer() {
	}

	public static Integer toInteger(String value) {
		return Optional.ofNullable(value).map(Integer::valueOf).orElse(0);
	}

	public static Long toLong(String value) {
		return Optional.ofNullable(value).map(Long::valueOf).orElse(0L);
	}

	public static Double toDouble(String value) {
		return Optional.ofNullable(value).map(Double::valueOf).orElse(0.0);
	}

	public static Float toFloat(String value) {
		return Optional.ofNullable(value).map(Float::valueOf).orElse(0.0f);
	}

	public static Boolean toBoolean(String value) {
		return Optional.ofNullable(value).map(Boolean::valueOf).orElse(false);
	}

}
