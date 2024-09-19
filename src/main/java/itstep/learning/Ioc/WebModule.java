package itstep.learning.Ioc;

import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.ServletModule;
import itstep.learning.filters.CharsetFilter;
import itstep.learning.filters.ControlFilter;
import itstep.learning.servlets.HomeServlet;
import itstep.learning.servlets.JspServlet;
import itstep.learning.servlets.ServletsServlet;
import itstep.learning.servlets.SignupServlet;

public class WebModule extends ServletModule {
    @Override
    protected void configureServlets() {
        filter("/*").through(ControlFilter.class);
        filter("/*").through(CharsetFilter.class);


        serve("/").with(HomeServlet.class);
        serve("/servlets").with(ServletsServlet.class);
        serve("/signup").with(SignupServlet.class);
        serve("/aboutJsp").with(JspServlet.class);
    }

}
