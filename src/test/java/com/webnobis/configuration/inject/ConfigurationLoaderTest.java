package com.webnobis.configuration.inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.experimental.theories.DataPoints;
import org.junit.experimental.theories.Theories;
import org.junit.experimental.theories.Theory;
import org.junit.runner.RunWith;

@RunWith(Theories.class)
public class ConfigurationLoaderTest {

	private static Path tmpFolder;

	private ConfigurationLoader configurationLoader;
	
	@DataPoints
	public static final String[] FILE_EXTENSIONS = ConfigurationLoader.class.getAnnotation(SupportedConfigurationRules.class).fileExtensions();

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		tmpFolder = Files.createTempDirectory(ConfigurationLoaderTest.class.getSimpleName());
		Path tmpFile = Files.createTempFile(tmpFolder, ConfigurationLoaderTest.class.getSimpleName(), ".tmp");

		Files.write(tmpFile, System.getenv().entrySet().stream()
				.map(entry -> Stream.of(entry.getKey(), entry.getValue())
						.collect(Collectors.joining(ConfigurationLoader.class.getAnnotation(SupportedConfigurationRules.class).keyValueSeparator()))
				).collect(Collectors.toList())
				, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		Files.walk(tmpFolder)
			.filter(file -> !tmpFolder.equals(file))
			.forEach(file -> {
			try {
				Files.delete(file);
			} catch (IOException e) {
				throw new UncheckedIOException(e);
			}
		});
		Files.delete(tmpFolder);
	}

	@Before
	public void setUp() throws Exception {
		configurationLoader = new ConfigurationLoader(tmpFolder.toString(), StandardCharsets.UTF_8);
	}

	@Theory
	public void testLoadConfiguration(String fileExtensionToUse) throws IOException {
		Path file = Files.walk(tmpFolder).filter(Files::isRegularFile).findAny().get();
		Files.move(file, tmpFolder.resolve(String.format("%s.%s", file.getFileName().toString(), fileExtensionToUse)), StandardCopyOption.ATOMIC_MOVE);
		
		Map<String,String> configurations = configurationLoader.loadConfiguration();
		assertNotNull(configurations);
		assertEquals(System.getenv().size(), configurations.size());
		assertEquals(System.getenv(), configurations);
	}

}
