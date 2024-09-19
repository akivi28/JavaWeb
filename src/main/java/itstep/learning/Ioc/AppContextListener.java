package itstep.learning.Ioc;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.servlet.GuiceServletContextListener;
import com.google.inject.servlet.ServletModule;
import itstep.learning.services.stream.BaosStringReader;
import itstep.learning.services.stream.StringReader;

import javax.servlet.ServletContextEvent;

public class AppContextListener extends GuiceServletContextListener {
    private StringReader stringReader = new BaosStringReader();
    @Override
    protected Injector getInjector() {
        return Guice.createInjector(
                new ServicesModule(stringReader),
                new WebModule(),
                new DbModule(stringReader)
        );
    }

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        super.contextInitialized(servletContextEvent);
        stringReader = new BaosStringReader();
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        super.contextDestroyed(servletContextEvent);
    }
}
