package ru.boksh.moneytransfer.config;

import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Provides;
import com.google.inject.servlet.GuiceFilter;
import java.util.EnumSet;
import java.util.Properties;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.DispatcherType;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class HttpModule extends AbstractModule {

  @Override
  protected void configure() {
    install(new SettingsModule());
    install(new JerseyModule());
  }

  @Provides
  @Singleton
  public Server getJettyServerWithConfifuredJersey(@Named(SettingsModule.APPLICATION_PROPERTIES) Properties properties, Injector injector) {
    Server server = new Server(Integer.valueOf(properties.getProperty("jetty.port")));

    ServletContextHandler jettyServletContextHandler = new ServletContextHandler(ServletContextHandler.NO_SESSIONS);
    jettyServletContextHandler.setContextPath("/");
    server.setHandler(jettyServletContextHandler);
    jettyServletContextHandler.addEventListener(new GuiceServletConfig(injector));
    jettyServletContextHandler.addFilter(GuiceFilter.class, "/*", EnumSet.allOf(DispatcherType.class));

    ServletContainer jerseyServletContainer = new ServletContainer();
    ServletHolder jerseyServletHolder = new ServletHolder(jerseyServletContainer);

    jettyServletContextHandler.addServlet(jerseyServletHolder, "/*");

    return server;
  }

}
