package ru.boksh.moneytransfer;

import com.google.inject.Guice;
import com.google.inject.Injector;
import ru.boksh.moneytransfer.config.MoneyTransferMainModule;

public class App {

  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new MoneyTransferMainModule());
    injector.getInstance(MoneyTransferApp.class).start();
//    Properties properties = injector.getInstance(Key.get(Properties.class, Names.named(Qualifiers.APPLICATION_PROPERTIES)));
//
//    ServletContextHandler servletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
//    servletContextHandler.addServlet(ServletContainer.class, "/");
//
//    new Server(8080);


  }

}
