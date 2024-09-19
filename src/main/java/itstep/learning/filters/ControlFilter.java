package itstep.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ControlFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        try {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpServletResponse res = (HttpServletResponse) response;

            String control = req.getParameter("control");

            if (control != null) {
                req.setAttribute("controlPassed", true);
            } else {
                req.setAttribute("controlPassed", false);
            }
            chain.doFilter(request, response);
        }
        catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("error in filter",e);
        }
    }

    @Override
    public void destroy() {
        filterConfig = null;
    }
}

