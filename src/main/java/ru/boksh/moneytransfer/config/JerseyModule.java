package ru.boksh.moneytransfer.config;

import com.sun.jersey.guice.JerseyServletModule;
import com.sun.jersey.guice.spi.container.servlet.GuiceContainer;
import ru.boksh.moneytransfer.http.resources.AccountResource;
import ru.boksh.moneytransfer.http.resources.MoneyTransferResource;

public class JerseyModule extends JerseyServletModule {

  @Override
  protected void configureServlets() {
    bind(AccountResource.class);
    bind(MoneyTransferResource.class);
    serve("/*").with(GuiceContainer.class);
  }
}
