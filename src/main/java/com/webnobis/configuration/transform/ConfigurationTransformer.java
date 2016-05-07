package com.webnobis.configuration.transform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Transforms configuration value (always String) to needed type (maybe int)
 * 
 * @author steffen
 *
 */
public abstract class ConfigurationTransformer {

	private ConfigurationTransformer() {
	}

	/**
	 * 
	 * @param type
	 *            the needed type
	 * @param value
	 *            the value
	 * @return type conform transformed value
	 * @throws UnsupportedOperationException
	 *             for not supported types
	 */
	@SuppressWarnings("unchecked")
	public static <T> T transform(Class<?> type, String value) {
		if (String.class.isAssignableFrom(type)) {
			return (T) Optional.ofNullable(value).orElse("");
		}
		if (Integer.class.isAssignableFrom(type)
				|| Integer.TYPE.isAssignableFrom(type)) {
			return (T) NumberAndBooleanTransformer.toInteger(value);
		}
		if (Long.class.isAssignableFrom(type)
				|| Long.TYPE.isAssignableFrom(type)) {
			return (T) NumberAndBooleanTransformer.toLong(value);
		}
		if (Double.class.isAssignableFrom(type)
				|| Double.TYPE.isAssignableFrom(type)) {
			return (T) NumberAndBooleanTransformer.toDouble(value);
		}
		if (Float.class.isAssignableFrom(type)
				|| Float.TYPE.isAssignableFrom(type)) {
			return (T) NumberAndBooleanTransformer.toFloat(value);
		}
		if (Boolean.class.isAssignableFrom(type)
				|| Boolean.TYPE.isAssignableFrom(type)) {
			return (T) NumberAndBooleanTransformer.toBoolean(value);
		}
		if (LocalDate.class.isAssignableFrom(type)) {
			return (T) DateAndTimeTransformer.toDate(value);
		}
		if (LocalDateTime.class.isAssignableFrom(type)) {
			return (T) DateAndTimeTransformer.toTime(value);
		}
		throw new UnsupportedOperationException(String.format(
				"no transformation found for type %s. All supported types: %s",
				type.getName(), getSupportedTypes()));
	}

	/**
	 * 
	 * @return all supported types, comma separated
	 */
	public static String getSupportedTypes() {
		return Stream.concat(Stream.of(String.class.getName()),
				Stream.concat(
						getSupportedTypes(NumberAndBooleanTransformer.class),
						getSupportedTypes(DateAndTimeTransformer.class)))
				.collect(Collectors.joining(", "));
	}

	private static Stream<String> getSupportedTypes(Class<?> transformerType) {
		return Arrays.stream(transformerType.getMethods())
				.filter(method -> method.getParameterCount() == 1 && method.getName().startsWith("to"))
				.map(method -> method.getReturnType())
				.map(c -> c.getName());
	}

}
