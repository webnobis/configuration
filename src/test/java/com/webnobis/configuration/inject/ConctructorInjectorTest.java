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

public class ConctructorInjectorTest {

	private static final String STRING_FIELD = "Quatsch mit Soße";

	private static final int INT_FIELD = Integer.MIN_VALUE;

	private static Map<String, String> testConfigurations;

	private ConstructorInjector injector;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		testConfigurations = new HashMap<>();
		testConfigurations.put("stringValue", STRING_FIELD);
		testConfigurations.put("intValue", String.valueOf(INT_FIELD));
	}

	@Before
	public void setUp() throws Exception {
		injector = new ConstructorInjector(testConfigurations);
	}

	@Test
	public void testInjectConstructorConfigurationsAndCreateInstanceZeroParameters() {
		ZeroParametersConstructor zpc = injector.injectConstructorConfigurationsAndCreateInstance(ZeroParametersConstructor.class);
		assertNotNull(zpc);
	}

	@Test
	public void testInjectConstructorConfigurationsAndCreateInstanceZeroDepthParameters() {
		ZeroDepthParametersConstructor zdpc = injector.injectConstructorConfigurationsAndCreateInstance(ZeroDepthParametersConstructor.class);
		assertNotNull(zdpc);
		assertEquals(STRING_FIELD, zdpc.s);
		assertEquals(INT_FIELD, zdpc.i);
		assertNull(zdpc.l);
	}

	private static class ZeroParametersConstructor {

	}

	private static class ZeroDepthParametersConstructor {

		private final int i;

		private final String s;

		private Long l;

		@Inject
		private ZeroDepthParametersConstructor(@Named("intValue") int i, @Named("stringValue") String s) {
			this.i = i;
			this.s = s;
		}

	}

}
