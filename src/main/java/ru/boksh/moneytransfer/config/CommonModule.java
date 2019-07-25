package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import ru.boksh.moneytransfer.model.AccountStorage;
import ru.boksh.moneytransfer.model.MoneyTransferService;
import ru.boksh.moneytransfer.model.SimpleInMemoryAccountStorage;

public class CommonModule extends AbstractModule {

  @Override
  protected void configure() {
    bind(AccountStorage.class).to(SimpleInMemoryAccountStorage.class).asEagerSingleton();
    bind(MoneyTransferService.class);
  }
}
