
<%@ page contentType="text/html;charset=UTF-8" %>
<p>Control passed: <%= request.getAttribute("controlPassed") %></p>
<h1>Home</h1>
<a href="servlets">servlets</a>
<h2><%=request.getAttribute("hash")%></h2>
