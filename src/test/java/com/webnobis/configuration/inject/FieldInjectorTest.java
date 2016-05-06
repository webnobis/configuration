package com.webnobis.configuration.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FieldInjectorTest {

	private static final String STRING_FIELD = "Soße";

	private static final int INT_FIELD = 99;

	private static final long LONG_FIELD = Long.MIN_VALUE;

	private static Map<String, String> testConfigurations;

	private FieldInjector injector;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testConfigurations = new HashMap<>();
		testConfigurations.put(TestField.class.getDeclaredField("s").getAnnotation(Named.class).value(), STRING_FIELD);
		testConfigurations.put(SubTestField.class.getDeclaredField("i").getAnnotation(Named.class).value(), String.valueOf(INT_FIELD));
		testConfigurations.put(CircularField.class.getDeclaredField("b").getAnnotation(Named.class).value(), Boolean.TRUE.toString());
		testConfigurations.put(StaticField.class.getDeclaredField("l").getAnnotation(Named.class).value(), String.valueOf(LONG_FIELD));
	}

	@Before
	public void setUp() throws Exception {
		injector = new FieldInjector(testConfigurations);
	}

	@Test
	public void testInjectFieldConfigurations() {
		TestField tf = injector.injectFieldConfigurations(new TestField());
		assertNotNull(tf);
		assertEquals(STRING_FIELD, tf.s);
		assertEquals(0, tf.l);
		assertNotNull(tf.stf);
		assertEquals(INT_FIELD, tf.stf.i);
		assertNull(tf.stf.b);
	}

	@Test(expected=MaxClassDepthReachedException.class)
	public void testInjectFieldConfigurationsMaxDepthReached() {
		injector.injectFieldConfigurations(new CircularField());
	}

	@Test
	public void testInjectFieldConfigurationsStatic() {
		StaticField sf = injector.injectFieldConfigurations(new StaticField());
		assertNotNull(sf);
		assertEquals(LONG_FIELD, StaticField.l);
	}

	@Test(expected=MissingConfigurationException.class)
	public void testInjectFieldConfigurationsMissingConfiguration() {
		injector.injectFieldConfigurations(new MissingConfigurationField());
	}

	private class TestField {

		@Inject
		@Named("stringValue")
		private String s;
		
		// null, because missing @Inject
		@Named("longValue")
		private long l;

		@Inject
		private SubTestField stf = new SubTestField();

	}

	private class SubTestField {

		@Inject
		@Named("intValue")
		private int i;

		private Boolean b;

	}

	private static class CircularField {
		
		private static int i = 0;

		@Inject
		@Named("booleanValue")
		private boolean b;

		@SuppressWarnings("unused")
		@Inject
		private CircularField cf = (i++ < 20)? new CircularField(): null;

	}

	private static class StaticField {

		@Inject
		@Named("longValue")
		private static long l;

	}
	
	private class MissingConfigurationField {

		@Inject
		@Named("booleanValue")
		private boolean b;
		
		@Inject
		@Named("missing configuration")
		private String s;
		
	}

}
