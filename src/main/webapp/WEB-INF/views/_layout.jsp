<%@ page contentType="text/html;charset=UTF-8"%>
<%
    String pageName = (String) request.getAttribute("page");
    String contextPath = request.getContextPath();
%>
<html>
<head>
    <title>Java Web</title>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!-- Compiled and minified CSS -->
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/css/materialize.min.css">
    <link rel="stylesheet" href="<%=contextPath%>/css/site.css">

</head>
<body>
    <header>
        <nav>
            <div class="nav-wrapper deep-purple">
                <a href="<%=contextPath%>/" class="site-logo left">pv222</a>
                <ul id="nav-mobile" class="left">
                    <li><a href="<%=contextPath%>/aboutJsp">Jsp</a></li>
                    <li><a href="<%=contextPath%>/servlets">Servlets</a></li>
<%--                    <li><a href="collapsible.html">JavaScript</a></li>--%>
                </ul>
                <a class="nav-addon right" href="<%=contextPath%>/signup"><i class="material-icons">account_circle</i></a>
            </div>
        </nav>
    </header>

    <main class="container"><jsp:include page='<%=pageName + ".jsp"%>'/></main>

    <div class="spacer"></div>

    <footer class="page-footer deep-purple">
        <div class="container">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Footer Content</h5>
                    <p class="grey-text text-lighten-4">You can use rows and columns here to organize your footer content.</p>
                </div>
                <div class="col l4 offset-l2 s12">
                    <h5 class="white-text">Links</h5>
                    <ul>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 1</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 2</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 3</a></li>
                        <li><a class="grey-text text-lighten-3" href="#!">Link 4</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <div class="footer-copyright">
            <div class="container">
                © 2024 pv222
                <a class="grey-text text-lighten-4 right" href="#!">More Links</a>
            </div>
        </div>
    </footer>

    <!-- Compiled and minified JavaScript -->
    <script src="https://cdnjs.cloudflare.com/ajax/libs/materialize/1.0.0/js/materialize.min.js"></script>
<script src="<%=contextPath%>/js/site.js"></script>
</body>
</html>
