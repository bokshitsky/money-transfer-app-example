package ru.boksh.moneytransfer.config;

import org.glassfish.jersey.server.ResourceConfig;

public class MoneyTransferAppJerseyConfig extends ResourceConfig {

  public MoneyTransferAppJerseyConfig() {
    packages("ru.boksh.moneytransfer.httpresources");
  }

}
