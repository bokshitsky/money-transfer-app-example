package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.ProvisionException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;

public class SettingsModule extends AbstractModule {

  public static final String APPLICATION_PROPERTIES = "qualifier-application-properties";

  @Provides
  @Singleton
  @Named(APPLICATION_PROPERTIES)
  public Properties getAppProperties() {
    String settingsDir = Optional.ofNullable(System.getProperty("settingsDir"))
        .orElseThrow(() -> new ProvisionException("'settingsDir' property is not specified"));

    try {
      Properties properties = new Properties();
      properties.load(new ByteArrayInputStream(Files.readAllBytes(Paths.get(settingsDir, "moneytransfer.properties"))));
      return properties;
    } catch (IOException e) {
      throw new ProvisionException(String.format("Can't read file 'moneytransfer.properties' from %s", settingsDir), e);
    }

  }

}
