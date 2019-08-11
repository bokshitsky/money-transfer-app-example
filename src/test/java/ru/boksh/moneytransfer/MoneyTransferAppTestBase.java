package ru.boksh.moneytransfer;

import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.name.Names;
import com.google.inject.util.Modules;
import java.io.IOException;
import java.net.ServerSocket;
import java.util.Properties;
import org.junit.jupiter.api.TestInstance;
import ru.boksh.moneytransfer.config.CommonModule;
import ru.boksh.moneytransfer.config.MainModule;
import static ru.boksh.moneytransfer.config.SettingsModule.APPLICATION_PROPERTIES;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public abstract class MoneyTransferAppTestBase {

  protected static final int TEST_MONEY_AMOUNT = 100000;
  private static Injector injector;

  protected Injector getInjector() {
    if (injector == null) {
      injector = Guice.createInjector(Modules.override(new MainModule()).with(new TestModule()));
    }
    return injector;
  }

  public static class TestModule implements Module {

    @Override
    public void configure(Binder binder) {
      binder.bind(Properties.class).annotatedWith(Names.named(APPLICATION_PROPERTIES)).toInstance(getTestAppProperties());
    }

    private Properties getTestAppProperties() {
      try {
        Properties properties = new Properties();
        properties.setProperty("jetty.port", String.valueOf(new ServerSocket(0).getLocalPort()));
        return properties;
      } catch (IOException e) {
        throw new RuntimeException("Failed to find jetty port");
      }
    }
  }

}