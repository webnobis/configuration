package com.webnobis.configuration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.BeforeClass;
import org.junit.Test;

import com.webnobis.configuration.inject.MissingConfigurationException;

public class ConfigurationTest {
	
	@BeforeClass
	public static void beforeClass() {
		Configuration.loadConfigurations();
	}

	@Test
	public void testCreateWithConfigurations() {
		A a = Configuration.createWithConfigurations(A.class);
		assertEquals(17, a.getA());
		assertTrue(a.isB());
		assertEquals("Wasser", a.getC1());
		assertTrue(LocalDate.of(2015, 12, 3).isEqual(a.getC2()));
		B b = a.getC3();
		assertNotNull(b);
		assertEquals(44, b.getA());
		assertEquals(Integer.valueOf(300), b.getB1());
		C c = b.getB3();
		assertNotNull(c);
		assertTrue(c.getA());
		assertNull(c.getUnset());
	}

	@Test(expected=MissingConfigurationException.class)
	public void testCreateWithNotLoadedConfigurations() {
		Configuration.emptyConfigurations();
		
		Configuration.createWithConfigurations(A.class);
	}

	@Test
	public void testRefreshConfigurations() {
		A a = new A(null, null, new B(null, false, new C()));
		assertNotNull(a);
		B b = a.getC3();
		assertNotNull(b);
		C c = b.getB3();
		assertNotNull(c);
		assertNull(c.getA());

		Boolean value = Boolean.FALSE;
		c.overwriteA(value);
		assertFalse(c.getA());
		
		Configuration.refreshConfigurations(a);
		
		assertTrue(c.getA());
	}

}
