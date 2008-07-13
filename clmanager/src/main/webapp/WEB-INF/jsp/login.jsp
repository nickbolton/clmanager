<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
<title>CLM</title>
<link type="text/css" title="css" rel="stylesheet" media="screen" href="css/library.css" charset="utf-8"/>
<link type="text/css" title="css" rel="stylesheet" media="screen" href="css/login.css" charset="utf-8"/>
<!--[if lt IE 8]>
    <link rel="stylesheet" type="text/css" href="css/iehacks.css" />
    <link rel="stylesheet" href="css/validate.css" type="text/css" media="screen"/>
    <![endif]-->
<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/>
<link type="text/x-icon" rel="icon" href="images/icons/cl.ico"/>
<link type="shortcut text/x-icon" rel="icon" href="images/icons/cl.ico"/>
</head>
<body id="body-mint" class="mint login">
<div id="wrapper">
<div id="body-container">
<!-- |  @group Body Header  | -->
<div id="layer">
  <div id="mask" class="hide"> </div>
  <div class="hide tooltip">This is a tooltip.</div>
</div>
<div id="ajaxMsg" class="hide">Saving ...</div>
<div id="body-header"><a name="top" id="logo-link" href="/"/>
  <ul class="links hide">
    <li id="tell-a-friend"><a href="javascript:void(null);">Tell A Friend!</a></li>
    <li><a target="_blank" href="contact.event">Get Help</a></li>
    <li><a href="profile.event">Your Profile</a> : </li>
    <li id="username"/>
    <li><a href="logout.event">Logout</a></li>
  </ul>
  <ul id="tabs-login" class="tabs">
    <li id="signup"><a rel="formbox-signup" href="signup">Signup</a></li>
    <li id="login" class="selected"><a rel="formbox-login" href="">Login</a></li>
  </ul>
</div>
<!-- |  @end Body Header  | -->
<div id="main">
  <div id="formbox-login" class="formbox">
    <div class="title">
      <h1 id="text-login">CLM Login</h1>
    </div>
    <form method="post" id="form-login" action="auth">
      <fieldset>
      <ol>
        <li>
          <label for="form-login-username">E-Mail</label>
          <input type="text" value="" tabindex="8" size="25" name="username" maxlength="255" id="form-login-username" defaultvalue="" class="text" autocomplete="on"/>
          <c:if test="${model.invalidEmail}">
            <div>
              <label class="error">Invalid.</label>
            </div>
          </c:if>
        </li>
        <li>
          <label for="form-login-password">Password</label>
          <input type="password" value="" tabindex="9" size="25" name="password" maxlength="16" id="form-login-password" defaultvalue="" class="text" autocomplete="on"/>
        </li>
        <!-- li>
          <input type="checkbox" tabindex="10" name="remember"/>Remember me
        </li-->
        <li>
          <input type="submit" value="Log In" tabindex="11" class="submit"/>
          <ul>
            <li>Forget your password? <a href="recovery.event">Recover it.</a></li>
            <li>Need an account? <a href="signup">Sign Up!</a></li>
          </ul>
          <input type="hidden" value="L" name="task"/>
          <input type="hidden" value="" name="nextPage" id="login-next"/>
        </li>
      </ol>
      </fieldset>
    </form>
  </div>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>

