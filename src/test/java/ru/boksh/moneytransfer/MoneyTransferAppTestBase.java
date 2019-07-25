package ru.boksh.moneytransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.jupiter.api.TestInstance;
import ru.boksh.moneytransfer.config.CommonModule;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MoneyTransferAppTestBase {

  private static Injector injector;

  protected Injector getInjector() {
    if (injector == null) {
      injector = Guice.createInjector(new CommonModule());
    }
    return injector;
  }

}