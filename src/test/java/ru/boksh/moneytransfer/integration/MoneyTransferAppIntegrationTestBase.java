package ru.boksh.moneytransfer.integration;

import com.google.inject.Injector;
import org.apache.http.impl.client.HttpClientBuilder;
import org.junit.jupiter.api.BeforeAll;
import ru.boksh.moneytransfer.MoneyTransferApp;
import ru.boksh.moneytransfer.MoneyTransferAppTestBase;
import ru.boksh.moneytransfer.MoneyTransferAppTestClient;

public abstract class MoneyTransferAppIntegrationTestBase extends MoneyTransferAppTestBase {

  // assume nobody can create same id during test
  protected static int NON_EXISTING_ACCOUNT_ID = Integer.MIN_VALUE;

  private static MoneyTransferApp moneyTransferApp;
  protected static MoneyTransferAppTestClient appTestClient;

  @BeforeAll
  void setUp() {
    if (moneyTransferApp == null) {
      Injector injector = getInjector();
      moneyTransferApp = injector.getInstance(MoneyTransferApp.class).start();
      appTestClient = new MoneyTransferAppTestClient(HttpClientBuilder.create().build(), moneyTransferApp.getJettyServer().getURI());
    }
  }

}
