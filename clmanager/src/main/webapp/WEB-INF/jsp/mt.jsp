<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>CLM > Posts</title>

<%@ include file="/WEB-INF/jsp/header.jsp" %>

</head>
<body id="body-mint" class="mint summary">
<div id="body-header">
  <%@ include file="/WEB-INF/jsp/links.jsp" %>
  <ul id="tabs-production" class="tabs">
    <li id="main" class=""><a href="main">Main</a></li>
    <li id="email" class=""><a href="email">Email Settings</a></li>
    <li id="mt" class="current"><a href="mt">Message Templates</a></li>
  </ul>
</div>
<div id="body-container">
  <div id="main">
  </div>
</div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>