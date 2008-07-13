<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>CLM > Posts</title>
<%@ include file="/WEB-INF/jsp/header.jsp" %>

<style>
div#form-div {
margin-left: 150px;
}
li.settings {
color: #CC3300;
float: left;
}
</style>
<script>
	$(document).ready(function(){
    	$("#email-settings-form").validate();
  	});
</script>
</head>
<body id="body-mint" class="mint summary">
<div id="body-header">
  <%@ include file="/WEB-INF/jsp/links.jsp" %>
  <ul id="tabs-production" class="tabs">
    <li id="main" class=""><a href="main">Main</a></li>
    <li id="email" class="current"><a href="email">Email Settings</a></li>
    <!--li id="mt" class=""><a href="mt">Message Templates</a></li-->
  </ul>
</div>
<div id="body-container">
  <div id="main">
    <div id="form-div">
      <form id="email-settings-form" action="setemail" method="post">
        <fieldset>
        <input name="fromEmailSettingsPage" value="true" type="hidden"/>
        <ol>
          <c:if test="${model.settingsSaved == 'true'}">
            <li class="settings">Settings Saved</li>
          </c:if>
          <li>
            <c:choose>
            	<c:when test="${model.smtpAuth == 'true'}">
                	<c:set var="smtpAuthVal" value="checked='true'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="smtpAuthVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-auth">SSL</label>
            <input id="email-settings-auth" tabindex="1" type="checkbox" name="smtpAuth" value="true" ${smtpAuthVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.smtpServer != ''}">
                	<c:set var="smtpServerVal" value="value='${model.smtpServer}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="smtpServerVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-server">Server Name</label>
            <input id="email-settings-server" tabindex="2" class="text required" type="text" size="50" maxlength="256" name="smtpServer" ${smtpServerVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.smtpPort != ''}">
                	<c:set var="smtpPortVal" value="value='${model.smtpPort}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="smtpPortVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-port">Server Port</label>
            <input id="email-settings-port" tabindex="3" class="text number" type="text" size="50" maxlength="5" name="smtpPort" ${smtpPortVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.smtpUser != ''}">
                	<c:set var="smtpUserVal" value="value='${model.smtpUser}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="smtpUserVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-user">SMTP User</label>
            <input id="email-settings-user" tabindex="4" class="text required email" type="text" size="50" maxlength="256" name="smtpUser" ${smtpUserVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.smtpPassword != ''}">
                	<c:set var="smtpPasswordVal" value="value='${model.smtpPassword}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="smtpPasswordVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-password">SMTP Password</label>
            <input id="email-settings-password" tabindex="5" class="text required" type="password" size="50" maxlength="256" name="smtpPassword" ${smtpPasswordVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.fromEmailAddress != ''}">
                	<c:set var="fromEmailAddressVal" value="value='${model.fromEmailAddress}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="fromEmailAddressVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-from">From Email (Optional)</label>
            <input id="email-settings-from" tabindex="6" class="text" type="text" size="50" maxlength="256" name="fromEmailAddress" ${fromEmailAddressVal}/>
          </li>
          <li>
            <c:choose>
            	<c:when test="${model.bccEmailAddress != ''}">
                	<c:set var="bccEmailAddressVal" value="value='${model.bccEmailAddress}'"/>
                </c:when>
                <c:otherwise>
                	<c:set var="bccEmailAddressVal" value=""/>
                </c:otherwise>
            </c:choose>
            <label for="email-settings-bcc">Bcc Email  (Optional)</label>
            <input id="email-settings-bcc" tabindex="7" class="text" type="text" size="50" maxlength="256" name="bccEmailAddress" ${bccEmailAddressVal}/>
          </li>
          <li><input type="submit" value="Save" tabindex="8" class="submit"/></li>
        </ol>
        </fieldset>
      </form>
    </div>
  </div>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>
