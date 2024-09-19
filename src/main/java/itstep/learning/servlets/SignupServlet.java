package itstep.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;

    @Inject
    public SignupServlet(FormParseService formParseService) {
        this.formParseService = formParseService;
    }
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("page", "signup");
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        FormParseResult res = formParseService.parse(req);

        req.setAttribute("page", "signup");
        req.setAttribute("name", res.getFields().get("user-name"));
        req.setAttribute("email", res.getFields().get("user-email"));
        req.setAttribute("phone", res.getFields().get("user-phone"));
        req.setAttribute("fileName", res.getFiles().get("user-avatar").getName());
        req.setAttribute("fileSize", res.getFiles().get("user-avatar").getSize());
        req.setAttribute("auth", true);

        System.out.println(res.getFiles().toString());

        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);
        System.out.println(res.getFields().size() + " " + res.getFiles().size());
    }
}
