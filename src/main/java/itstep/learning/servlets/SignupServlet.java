package itstep.learning.servlets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;
import com.google.inject.Singleton;
import itstep.learning.dal.dao.UserDao;
import itstep.learning.dal.dto.User;
import itstep.learning.models.form.UserSignupFormModel;
import itstep.learning.rest.RestResponse;
import itstep.learning.services.files.FileService;
import itstep.learning.services.formparse.FormParseResult;
import itstep.learning.services.formparse.FormParseService;
import org.apache.commons.fileupload.FileItem;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.logging.Logger;
import java.util.regex.Pattern;

@Singleton
public class SignupServlet extends HttpServlet {
    private final FormParseService formParseService;
    private final FileService fileService;
    private final UserDao userDao;
    private final Logger logger;
    @Inject
    public SignupServlet(FormParseService formParseService, FileService fileService, UserDao userDao, Logger logger) {
        this.formParseService = formParseService;
        this.fileService = fileService;
        this.userDao = userDao;
        this.logger = logger;
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        switch (req.getMethod().toUpperCase()) {
            case "PATCH": doPatch(req, resp); break;
            default: super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("page", "signup");
        req.getRequestDispatcher("WEB-INF/views/_layout.jsp").forward(req, resp);

    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userLogin = req.getParameter("user-email");
        String userPassword = req.getParameter("user-password");
        logger.info("userLogin: "+ userLogin + " userPassword: "+ userPassword);
        RestResponse restResponse = new RestResponse();
        resp.setContentType("application/json");
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        if( userLogin == null || userLogin.isEmpty() ||
                userPassword == null || userPassword.isEmpty() ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( "Missing or empty credentials" );
            resp.getWriter().print( gson.toJson( restResponse ) );
            return;
        }

        try {
            User user = userDao.authenticate(userLogin, userPassword);
            if( user == null ) {
                restResponse.setStatus( "Error" );
                restResponse.setData( "Credentials rejected" );
                resp.getWriter().print( gson.toJson( restResponse ) );
                return;
            }
            HttpSession session = req.getSession();
            session.setAttribute("userId", user.getId());
            restResponse.setStatus("Ok");
            restResponse.setData(user);
            resp.getWriter().print(gson.toJson(restResponse));
        }
        catch (Exception e) {
            restResponse.setStatus( "Error" );
            restResponse.setData( e );
            resp.getWriter().print( gson.toJson( restResponse ) );
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RestResponse restResponse = new RestResponse();
        resp.setContentType( "application/json" );

        UserSignupFormModel model;
        try {
            model = getModelFromRequest( req );
        }
        catch( Exception ex ) {
            restResponse.setStatus( "Error" );
            restResponse.setData( ex.getMessage() );
            resp.getWriter().print(
                    new Gson().toJson( restResponse )
            );
            return;
        }

        User user = userDao.signUp( model );
        if( user == null ) {
            restResponse.setStatus( "Error" );
            restResponse.setData("500 Db Error");
            resp.getWriter().print(new Gson().toJson( restResponse ) );
        }

        restResponse.setStatus( "Ok" );
        restResponse.setData( model );
        resp.getWriter().print(
                new Gson().toJson( restResponse )
        );
    }

    private UserSignupFormModel getModelFromRequest( HttpServletRequest req ) throws Exception {
        SimpleDateFormat dateParser =
                new SimpleDateFormat("yyyy-MM-dd");
        FormParseResult res = formParseService.parse( req );

        UserSignupFormModel model = new UserSignupFormModel();

        model.setName( res.getFields().get("user-name") );
        if( model.getName() == null || model.getName().isEmpty() ) {
            throw new Exception( "Missing or empty required field: 'user-name'" );
        }

        model.setEmail( res.getFields().get("user-email") );

        try {
            model.setBirthdate(
                    dateParser.parse(
                            res.getFields().get("user-birthdate")
                    )
            );
        }
        catch( ParseException ex ) {
            throw new Exception( ex.getMessage() );
        }

        String uploadedName = null;
        FileItem avatar = res.getFiles().get( "user-avatar" );
        if( avatar.getSize() > 0 ) {
            try{
                uploadedName = fileService.uploadAvatar( avatar );
            }catch( Exception ex ) {
                throw new Exception( ex.getMessage() );
            }
            model.setAvatar( uploadedName );
        }
        System.out.println( uploadedName );

        model.setPassword( res.getFields().get( "user-password" ) );
        return model;
    }
}
