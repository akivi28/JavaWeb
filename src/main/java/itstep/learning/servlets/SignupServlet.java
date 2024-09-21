package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.models.form.UserSignupFormModel;
import itstep.learning.rest.RestResponse;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.regex.Pattern;

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
        SimpleDateFormat dateParser =
                new SimpleDateFormat("yyyy-MM-dd");
        RestResponse restResponse = new RestResponse();
        resp.setContentType( "application/json" );
        FormParseResult res = formParseService.parse( req );
        System.out.println( res.getFields().size() + " " + res.getFiles().size() );
        System.out.println( res.getFields().toString() );
        UserSignupFormModel model = new UserSignupFormModel();
        model.setName( res.getFields().get("user-name") );
        if( model.getName() == null || model.getName().isEmpty() ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( "Missing or empty required field: 'user-name'" );
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }
        model.setEmail( res.getFields().get("user-email") );
        if( model.getEmail() == null || model.getEmail().isEmpty() ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( "Missing or empty required field: 'user-email'" );
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }

        String emailTmp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";
        Pattern pattern = Pattern.compile( emailTmp );
        if (!pattern.matcher(model.getEmail()).matches()) {
            restResponse.setStatus("Error");
            restResponse.setData("Invalid email format");
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }

        try {
            model.setBirthdate(
                    dateParser.parse(
                            res.getFields().get("user-birthdate")
                    )
            );
            Date currentDate = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

            if (model.getBirthdate().after(currentDate)) {
                restResponse.setStatus("Error");
                restResponse.setData("Birthdate cannot be in the future.");
                resp.getWriter().print(new Gson().toJson(restResponse));
                return;
            }
        }
        catch( ParseException ex ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( ex.getMessage() );
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }

        if (res.getFields().get("user-password") == null || res.getFields().get("user-password").isEmpty() || res.getFields().get("user-password").length() < 8 ) {
            restResponse.setStatus( "Error" );
            restResponse.setData("Missing or empty required field: 'user-password'" );
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }else {
            if(!Objects.equals(res.getFields().get("user-password"), res.getFields().get("user-repeat"))){
                restResponse.setStatus( "Error" );
                restResponse.setData("Passwords do not match");
                resp.getWriter().print(
                        new Gson().toJson( restResponse )
                );
                return;
            }
        }

        restResponse.setStatus( "Ok" );
        restResponse.setData( model );
        resp.getWriter().print(
                new Gson().toJson( restResponse )
        );
    }
}
