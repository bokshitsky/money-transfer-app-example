package ru.boksh.moneytransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.boksh.moneytransfer.config.MainModule;

public class Main {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new MainModule());
    injector.getInstance(MoneyTransferApp.class).start().join();
  }

}
