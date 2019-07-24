package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class HttpModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SettingsModule());
  }

  @Provides
  @Singleton
  public Server getJettyServer(@Named(SettingsModule.APPLICATION_PROPERTIES) Properties properties) {
    Server server = new Server(Integer.valueOf(properties.getProperty("jetty.port")));

    ServletContextHandler jettyServletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    jettyServletContextHandler.setContextPath("/");
    server.setHandler(jettyServletContextHandler);

    ServletContainer jerseyServletContainer = new ServletContainer(new MoneyTransferAppJerseyConfig());
    ServletHolder jerseyServletHolder = new ServletHolder(jerseyServletContainer);
    jerseyServletHolder.setInitOrder(0);

    jettyServletContextHandler.addServlet(jerseyServletHolder, "/*");

    return server;
  }

}
