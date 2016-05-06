package com.webnobis.configuration.inject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Loads configuration of all supported configuration files.
 * 
 * @author steffen
 * @see ConfigurationFilesRootPath
 * @see SupportedConfigurationRules
 */
@ConfigurationFilesRootPath("")
@SupportedConfigurationRules(fileExtensions = { "txt", "properties", "cfg" }, keyValueSeparator = "=", commentStartToken = "#", defaultCharset = "UTF-8")
public class ConfigurationLoader {

	private final String configurationFilesRootPath;

	private final Charset charset;

	private final Set<String> supportedFileExtensions;

	private final String separator;

	private final String commentStartToken;

	/**
	 * 
	 * @param charset
	 *            the charset of configuration files, otherwise the default
	 *            charset
	 * @see SupportedConfigurationRules#defaultCharset()
	 */
	public ConfigurationLoader(Charset charset) {
		this(null, charset);
	}

	/**
	 * Only used from test
	 * 
	 * @param configurationFilesRootPath
	 * @param charset
	 */
	ConfigurationLoader(String configurationFilesRootPath, Charset charset) {
		this.configurationFilesRootPath = Optional.ofNullable(configurationFilesRootPath).orElse(ConfigurationLoader.class.getAnnotation(ConfigurationFilesRootPath.class).value());
		SupportedConfigurationRules supportedFileExtensions = ConfigurationLoader.class.getAnnotation(SupportedConfigurationRules.class);
		this.charset = Optional.ofNullable(charset).orElse(Charset.forName(supportedFileExtensions.defaultCharset()));
		this.supportedFileExtensions = Arrays.stream(supportedFileExtensions.fileExtensions()).map(ext -> String.format(".%s", ext)).collect(Collectors.toSet());
		separator = supportedFileExtensions.keyValueSeparator();
		commentStartToken = supportedFileExtensions.commentStartToken();
	}

	/**
	 * 
	 * @return all found configuration pairs
	 * @throws IllegalStateException
	 *             if no configuration found or reading failed
	 */
	public Map<String, String> loadConfiguration() {
		try {
			return Files.walk(getConfigurationFilesRootPath()).filter(this::isSupportedFileExtension)
					.flatMap(file -> {
						try {
							return Files.readAllLines(file, charset).stream();
						} catch (IOException e) {
							throw new IllegalStateException(e.getMessage(), e);
						}
					})
					.filter(line -> !line.startsWith(commentStartToken))
					.filter(line -> line.contains(separator))
					.collect(Collectors.toMap(line -> line.substring(0, line.indexOf(separator)), line -> line.substring(line.indexOf(separator) + 1)));
		} catch (IOException e) {
			throw new IllegalStateException(e.getMessage(), e);
		}
	}

	private Path getConfigurationFilesRootPath() {
		URL classPathUrl = Thread.currentThread().getContextClassLoader().getResource(configurationFilesRootPath);
		if (classPathUrl != null) {
			try {
				return Paths.get(classPathUrl.toURI());
			} catch (URISyntaxException e) {
				throw new IllegalStateException(e.getMessage(), e);
			}
		} else {
			return Paths.get(configurationFilesRootPath);
		}
	}

	private boolean isSupportedFileExtension(Path file) {
		String fileName = file.getFileName().toString();
		return Files.isRegularFile(file) && fileName.contains(".") && supportedFileExtensions.contains(fileName.substring(fileName.lastIndexOf('.')));
	}

}
