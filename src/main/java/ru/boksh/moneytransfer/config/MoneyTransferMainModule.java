package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import ru.boksh.moneytransfer.MoneyTransferApp;

public class MoneyTransferMainModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new HttpModule());
    bind(MoneyTransferApp.class);
  }

}
