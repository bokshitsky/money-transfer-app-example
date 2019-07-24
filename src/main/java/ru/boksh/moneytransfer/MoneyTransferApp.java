package ru.boksh.moneytransfer;

import javax.inject.Inject;
import org.eclipse.jetty.server.Server;

public class MoneyTransferApp {

  private final Server jettyServer;

  @Inject
  public MoneyTransferApp(Server jettyServer) {
    this.jettyServer = jettyServer;
  }

  public void start() {
    try {
      jettyServer.start();
    } catch (Exception e) {
      throw new RuntimeException("Exception during jetty start", e);
    }

    try {
      jettyServer.join();
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
      throw new RuntimeException(e);
    }
  }
}
