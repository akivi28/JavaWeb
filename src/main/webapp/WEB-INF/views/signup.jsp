
<%@ page contentType="text/html;charset=UTF-8"%>

<%
  Boolean auth = (Boolean) request.getAttribute("auth");
  if (auth == null) {
    auth = false;
  }
%>
<h1>Register</h1>

<%if(auth){%>
  <p>Name: <%= request.getAttribute("name")%></p>
  <p>Phone: <%= request.getAttribute("phone")%></p>
  <p>Email: <%= request.getAttribute("email")%></p>
  <p>File name: <%= request.getAttribute("fileName")%></p>
  <p>File size: <%= request.getAttribute("fileSize")%> bytes</p>
<% } %>

<form class="card-panel grey lighten-5" enctype="multipart/form-data" method="post">
  <div class="row">
    <div class="input-field col s6">
      <i class="material-icons prefix">badge</i>
      <input id="user-name" name="user-name" type="text" class="validate">
      <label for="user-name">Name</label>
    </div>
    <div class="input-field col s6">
      <i class="material-icons prefix">phone</i>
      <input id="user-phone" name="user-phone" type="tel" class="validate">
      <label for="user-phone">Phone</label>
    </div>
  </div>

  <div class="row">
    <div class="input-field col s6">
      <i class="material-icons prefix">alternate_email</i>
      <input id="user-email" name="user-email" type="email" class="validate">
      <label for="user-email">E-mail</label>
    </div>
    <div class="file-field input-field col s6">
      <div class="btn deep-purple">
        <i class="material-icons">add_photo_alternate</i>
        <input type="file" name="user-avatar">
      </div>
      <div class="file-path-wrapper">
        <input class="file-path validate" type="text">
      </div>
    </div>
  </div>

  <div class="row">
    <div class="input-field col s6">
      <i class="material-icons prefix">lock</i>
      <input id="user-password" name="user-password" type="password" class="validate">
      <label for="user-password">Password</label>
    </div>
    <div class="input-field col s6">
      <i class="material-icons prefix">lock_open</i>
      <input id="user-repeat" name="user-repeat" type="password" class="validate">
      <label for="user-repeat">Repeat Password</label>
    </div>
  </div>

  <div class="row">
    <button class="btn waves-effect waves-light  deep-purple darken-2 right" type="submit">Register
      <i class="material-icons right">send</i>
    </button>
  </div>
</form>

<div style="height: 40px"></div>
