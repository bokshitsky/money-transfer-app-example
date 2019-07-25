package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import ru.boksh.moneytransfer.MoneyTransferApp;

public class MainModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new HttpModule());
    install(new CommonModule());
    bind(MoneyTransferApp.class);
  }

}
