package com.webnobis.configuration.transform;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

abstract class DateAndTimeTransformer {
	
	public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE;
	
	public static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
	
	private DateAndTimeTransformer() {}
	
	public static LocalDate toDate(String value) {
		return LocalDate.parse(value, DATE_FORMATTER);
	}

	public static LocalDateTime toTime(String value) {
		return LocalDateTime.parse(value, TIME_FORMATTER);
	}
	
}
