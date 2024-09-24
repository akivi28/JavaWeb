package itstep.learning.Ioc;

import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.CharsetFilter;
import itstep.learning.filters.ControlFilter;
import itstep.learning.filters.auth.SessionAuthFilter;
import itstep.learning.servlets.*;

public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(ControlFilter.class);
        filter("/*").through(CharsetFilter.class);
        filter("/*").through(SessionAuthFilter.class);


        serve("/").with(HomeServlet.class);

        serve("/file/*").with(DownloadServlet.class);

        serve("/servlets").with(ServletsServlet.class);
        serve("/aboutJsp").with(JspServlet.class);

        serve("/signup").with(SignupServlet.class);
        serve("/spa").with(SpaServlet.class);
        serve("/auth").with(AuthServlet.class);


    }

}
