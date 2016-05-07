package com.webnobis.configuration.transform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Transforms date or time
 * 
 * @author steffen
 *
 */
abstract class DateAndTimeTransformer {

	private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;

	private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

	private DateAndTimeTransformer() {
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as date
	 */
	public static LocalDate toDate(String value) {
		return LocalDate.parse(value, DATE_FORMATTER);
	}

	/**
	 * 
	 * @param value
	 *            the value
	 * @return value as time
	 */
	public static LocalDateTime toTime(String value) {
		return LocalDateTime.parse(value, TIME_FORMATTER);
	}

}
