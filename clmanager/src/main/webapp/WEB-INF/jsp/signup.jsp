<%@ include file="/WEB-INF/jsp/include.jsp" %>

<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml"><head>

<meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/><title>CLM</title><link type="text/css" title="css" rel="stylesheet" media="screen" href="css/library.css" charset="utf-8"/><link type="text/css" title="css" rel="stylesheet" media="screen" href="css/login.css" charset="utf-8"/><!--[if lt IE 8]>
    <link rel="stylesheet" type="text/css" href="css/iehacks.css" />
    <![endif]--><meta http-equiv="Content-Type" content="application/xhtml+xml; charset=utf-8"/><link type="text/x-icon" rel="icon" href="images/icons/cl.ico"/><link type="shortcut text/x-icon" rel="icon" href="images/icons/cl.ico"/><style charset="utf-8" type="text/css">/* See license.txt for terms of usage */

.firebugHighlight {
    z-index: 2147483647;
    position: absolute;
    background-color: #3875d7;
}

.firebugLayoutBoxParent {
    z-index: 2147483647;
    position: absolute;
    background-color: transparent;
    border-right: 1px dashed #BBBBBB;
    border-bottom: 1px dashed #BBBBBB;
}

.firebugRulerH {
    position: absolute;
    top: -15px;
    left: 0;
    width: 100%;
    height: 14px;
    background: url(chrome://firebug/skin/rulerH.png) repeat-x;
    border-top: 1px solid #BBBBBB;
    border-right: 1px dashed #BBBBBB;
    border-bottom: 1px solid #000000;
}

.firebugRulerV {
    position: absolute;
    top: 0;
    left: -15px;
    width: 14px;
    height: 100%;
    background: url(chrome://firebug/skin/rulerV.png) repeat-y;
    border-left: 1px solid #BBBBBB;
    border-right: 1px solid #000000;
    border-bottom: 1px dashed #BBBBBB;
}

.overflowRulerX > .firebugRulerV {
    left: 0;
}

.overflowRulerY > .firebugRulerH {
    top: 0;
}

/* * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * */

.firebugLayoutBoxOffset {
    z-index: 2147483647;
    position: absolute;
    opacity: 0.8;
}

.firebugLayoutBoxMargin {
    background-color: #EDFF64;
}

.firebugLayoutBoxBorder {
    background-color: #666666;
}

.firebugLayoutBoxPadding {
    background-color: SlateBlue;
}

.firebugLayoutBoxContent {
    background-color: SkyBlue;
}

/*.firebugHighlightGroup .firebugLayoutBox {
    background-color: transparent;
}

.firebugHighlightBox {
    background-color: Blue !important;
}*/

.firebugLayoutLine {
    z-index: 2147483647;
    background-color: #000000;
    opacity: 0.4;
}

.firebugLayoutLineLeft,
.firebugLayoutLineRight {
    position: fixed;
    width: 1px;
    height: 100%;
}

.firebugLayoutLineTop,
.firebugLayoutLineBottom {
    position: absolute;
    width: 100%;
    height: 1px;
}

.firebugLayoutLineTop {
    margin-top: -1px;
    border-top: 1px solid #999999;
}

.firebugLayoutLineRight {
    border-right: 1px solid #999999;
}

.firebugLayoutLineBottom {
    border-bottom: 1px solid #999999;
}

.firebugLayoutLineLeft {
    margin-left: -1px;
    border-left: 1px solid #999999;
}
</style>
<link rel="stylesheet" href="css/validate.css" type="text/css" media="screen"/>
<script src="js/jquery.min.js"></script>
<script src="js/jquery.validate.min.js"></script>
<script>
  $(document).ready(function(){
    $("#form-signup").validate();
  });
  </script>

</head><body id="body-mint" class="mint login"><div id="wrapper"><div id="body-container">
    <!-- |  @group Body Header  | -->

<div id="layer">
    <div id="mask" class="hide"> </div>
<div class="hide tooltip">This is a tooltip.</div></div>

<div id="ajaxMsg" class="hide">Saving ...</div>

<div id="body-header"><a name="top" id="logo-link" href="/"/><ul class="links hide"><li id="tell-a-friend"><a href="javascript:void(null);">Tell A Friend!</a></li><li><a target="_blank" href="contact.event">Get Help</a></li><li><a href="profile.event">Your Profile</a> : </li><li id="username"/><li><a href="logout.event">Logout</a></li></ul><ul id="tabs-login" class="tabs"><li id="signup" class="selected"><a rel="formbox-signup" href="">Signup</a></li><li id="login" class=""><a rel="formbox-login" href="login">Login</a></li></ul></div>

<!-- |  @end Body Header  | -->


    <div id="main">

        
        

        <div id="formbox-signup" class="formbox">
        
            <div class="title">
                <h1 id="text-signup">Create a CLM Account</h1>
            </div>
            

            <form method="post" id="form-signup" action="create"><fieldset>

                <ol>
                    <li class=""><label for="form-signup-username">Your E-Mail</label><input type="text" value="" tabindex="1" size="25" name="username" maxlength="255" id="form-signup-username" defaultvalue="" class="text required email" autocomplete="on"/>
                    <c:if test="${model.emailTaken}">
                        <div><label class="error">Email taken.</label></div>
                    </c:if>
                    </li>
                    <li class=""><label for="form-signup-password">Password</label><input type="password" value="" tabindex="4" size="25" name="password" maxlength="16" id="form-signup-password" defaultvalue="" class="required text" autocomplete="on"/><span class="msg"> </span></li>
                    <li style="background: transparent none repeat scroll 0% 0%; -moz-background-clip: -moz-initial; -moz-background-origin: -moz-initial; -moz-background-inline-policy: -moz-initial;"><input type="submit" value="Sign Up" tabindex="7" class="submit"/><input type="checkbox" tabindex="6" name="terms"/>
                            I agree to the CLM <a target="_blank" href="">Terms of Use</a><input type="hidden" value="S" name="task"/><div id="" class="ajax">-</div></li></ol>

            </fieldset></form>

        </div>
    </div>

<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body></html>
