# configuration
It's a configuration service to inject constructor parameters and instance variables with all configuration file contents.

## usage

@see src/test/java: ConfigurationTest.java

### load configurations

It loads all configuration files contents within class path.

Configuration.loadConfigurations();

### construct new configuration injected instance

A a = Configuration.createWithConfigurations(A.class);

### refresh configurations

If new configurations available: load configurations

Configuration.refreshConfigurations(a);