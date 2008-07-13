<%@ include file="/WEB-INF/jsp/include.jsp" %>
<html lang="en" xml:lang="en" xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>CLM > Posts</title>
<%@ include file="/WEB-INF/jsp/header.jsp" %>
<style>
.hidden {
	display: none;
}
div#column-accounts.column ul li.selected {
	background:#FFFFFF none repeat scroll 0% 0%;
	cursor:default;
	margin:0pt;
	padding:7px 10px 10px;
}
table#flex1 tbody td {
	border-bottom:1px solid #FFFFFF;
	border-right:1px solid #EEEEEE;
	font-size:8pt;
}
div.hDiv thead th {
	font-size:12px
}
h1 {
	font-size: 26px;
	font-weight: normal;
	margin: 0px;
	color: #0099FF;
}
#byline {
	font-size:14px
}
.flexigrid div.bDiv div.post-flags {
	white-space: nowrap;
	padding: 0px;
}
.flexigrid div.bDiv div.post-viewed, .flexigrid div.bDiv div.post-not-viewed, .flexigrid div.bDiv div.post-fav, .flexigrid div.bDiv div.post-not-fav, .flexigrid div.bDiv div.post-flag, .flexigrid div.bDiv div.post-not-flag, .flexigrid div.bDiv div.post-pic, .flexigrid div.bDiv div.post-not-pic, .flexigrid div.bDiv div.post-res, .flexigrid div.bDiv div.post-not-res {
	height: 16px;
	width: 16px;
	padding: 0px;
	float: left;
}
.flexigrid div.cDrag div:hover, .flexigrid div.cDrag div.dragging {
	background:transparent none repeat scroll;
}
.flexigrid div.cDrag div {
	float: left;
	background: none;
	display: block;
	position: absolute;
	height: 24px;
	width: 5px;
	cursor: default;
}
.flexigrid div.bDiv tr:hover td, .flexigrid div.bDiv tr:hover td.sorted, .flexigrid div.bDiv tr.trOver td.sorted, .flexigrid div.bDiv tr.trOver td {
	background:#F1FFE6 none repeat scroll 0% 0%;
}
.flexigrid div.bDiv tr.trSelected:hover td, .flexigrid div.bDiv tr.trSelected:hover td.sorted, .flexigrid div.bDiv tr.trOver.trSelected td.sorted, .flexigrid div.bDiv tr.trOver.trSelected td, .flexigrid tr.trSelected td.sorted, .flexigrid tr.trSelected td {
	background:#007934 url(images/effects/gloss.png) repeat-x scroll 0pt 50% !important;
	color:#FFFFFF !important;
}
div#column-transactions div#controls-bottom.controls {
	-moz-border-radius-bottomleft: 0px;
	-moz-border-radius-bottomright: 0px;
}
div#column-transactions div#controls-bottom.controls {
	margin: 0pt 0pt 0px;
	height: 25px;
	border-bottom: 0px;
}
.fav-img {
	margin-right: 10px;
}
.city-add {
	margin-right: 10px;
}
.cat-add {
	margin-right: 10px;
}
.city-remove {
	margin-left: 10px;
}
.cat-remove {
	margin-left: 10px;
}
div#post-contents-wrapper {
	height: 320px;
	width: 518px;
	border-color: #EEEEEE;
	border-style: solid solid solid;
	border-width: 0px 1px 1px;
}
div#post-contents {
	margin: 5px;
	overflow: auto;
	height: 230px;
}
#post-slideshow {
	overflow:auto;
	height:766px;
}
div#post-detail-wrapper {
	background:#F6F6F5 none repeat scroll 0% 0%;
	width: 518px;
	height: 85px;
}
div#post-detail table {
	margin: 5px;
}
div#post-detail td {
	font-size: 10px;
}
div#column-accounts.column ul a.city, a.cat {
	font-size: 9pt;
}
div.post-not-fav {
	background: url(images/icons/post-not-fav.png)
}
div.post-fav {
	background: url(images/icons/post-fav.png)
}
div.post-not-flag {
	background: url(images/icons/post-not-flag.png)
}
div.post-flag {
	background: url(images/icons/post-flag.png)
}
div.post-not-pic {
	background: url(images/icons/post-not-pic.png)
}
div.post-pic {
	background: url(images/icons/post-pic.png)
}
div.post-not-res {
	background: url(images/icons/post-not-res.png)
}
div.post-res {
	background: url(images/icons/post-res.png)
}
div.post-not-viewed {
	background: url(images/icons/post-not-viewed.png)
}
div.post-viewed {
	background: url(images/icons/post-viewed.png)
}
div#reply-dialog {
	padding: 0px;
	margin: 0px;
	margin-bottom: 35px;
	margin-top: 10px;
	margin-left: 15px;
	margin-right: 15px;
}
div#reply-dialog-content {
	width: 100%;
	height: 100%;
}
#reply-dialog-text {
	text-wrap: normal;
	width: 100%;
	height: 100%;
}
div#reply-dialog-file-upload {
	width: 100%;
	height: auto;
	margin-bottom: 15px;
}
#reply-dialog-file-list-div {
	float: left;
	overflow: auto;
}
div#reply-dialog-file-list {
	float: left;
	height: auto;
}
div#reply-dialog-saved-div {
	margin-left: 10px;
	width: 40%;
	float: left;
	overflow: auto;
}
a#reply-dialog-attach-another-a {
	float: bottom;
}
#reply-dialog-subject {
	margin-left: 10px;
	width: 80%;
}
#reply-dialog-subject-label {
	width: 20%;
}
#reply-dialog-saved-list text {
	color: #5572BC;
}
#reply-dialog-saved-list input img a {
}
form#reply-dialog-form, form#reply-dialog-form fieldset {
	width: 100%;
	height: 60%;
}
a.sf {
}
div#reply-dialog-subject-div {
	float: left;
	width: 100%;
}
div#reply-dialog-mt-wrapper {
	width: 100%;
	margin-bottom: 10px;
}
div#reply-dialog-mt-div {
	float: left;
	width: 50%;
}
div#reply-dialog-mt-save-div {
	float: right;
	width: 50%;
}
div.area {
	margin-left: 26px;
	width: 97px;
}
a.area {
	font-size: 8pt;
	float: left;
	padding: 0px 3px 0px;
}
div#column-accounts.column ul li a.area:hover {
	color:#685C53;
}
div#column-accounts.column ul li a.area {
	color:#AFAFAF;
}
div#column-accounts.column ul li a.selected {
	color:#007934;
}
div#column-accounts.column ul li a.selected:hover {
	color:#007934;
}
div.invalid {
	color: #CC3300;
	float: left;
	font-size: 14pt;
	margin-top: 10px;
	width: 10px;
}
div.spacer {
	width: 10px;
	float: left;
}
</style>
<!-- End Post table goo -->
<script language="javascript" type="text/javascript">
	  var altDown = false;
	  var shiftDown = false;
	  
	  $(document).ready(function() {
	  	$(document).keydown(function(evt) {
		  if (evt.which == 18) {
		    altDown = true;
		  } else if (evt.which == 16) {
		    shiftDown = true;
		  }
		});
		$(document).keyup(function(evt) {
		  if (evt.which == 18) {
		    altDown = false;
		  } else if (evt.which == 16) {
		    shiftDown = false;
		  }
		});
		
		$("#reply-dialog-attach-another-a").click(function (s) {
			replyDialogAddFile();
		});
		
		
		$("#reply-dialog-form").ajaxForm(function (reply) {
			if (reply.substring(0,5) == "error") {
				emailSettingsError = reply
				window.alert("Check email settings, error sending reply (" + reply + ").");
			} else {
				$("div#res-"+reply).removeClass("post-not-res");
				$("div#res-"+reply).addClass("post-res");
			}
		});
		
		var emailSettingsOptions = {
			beforeSubmit:  validateEmailSettings,
			success:       emailSettingsOk
		};
		$("#email-settings-form").ajaxForm(emailSettingsOptions);
		
		$(".city-remove").each(function () {
			$(this).click(function (s) {
				removeCity(s);
			});
		});
		$(".cat-remove").each(function () {
			$(this).click(function (s) {
				removeCategory(s);
			});
		});
	  });
	  
	  function validateEmailSettings(fd, jqForm, options) {
	  	var retValue = true;
	  	for (var i=0; i < fd.length; i++) {
        	if (fd[i].name == "smtpServer") {
				if (fd[i].value == "") {
					$("#email-settings-server-invalid").removeClass("hidden");
            		retValue = false; 
				} else {
					$("#email-settings-server-invalid").addClass("hidden");
				}
        	} else if (fd[i].name == "smtpPort") {
				if (fd[i].value != "" && !fd[i].value.match(/^[0-9]+$/)) {
					$("#email-settings-server-port").removeClass("hidden");
            		retValue = false;
				} else {
					$("#email-settings-server-port").addClass("hidden");
				}
			} else if (fd[i].name == "smtpUser") {
				if (!fd[i].value.match(/^.+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/)) {
					$("#email-settings-server-user").removeClass("hidden");
            		retValue = false; 
				} else {
					$("#email-settings-server-user").addClass("hidden");
				}
			} else if (fd[i].name == "smtpPassword") {
				if (fd[i].value == "") {
					$("#email-settings-server-password").removeClass("hidden");
            		retValue = false; 
				} else {
					$("#email-settings-server-password").addClass("hidden");
				}
        	} else if (fd[i].name == "fromEmailAddress") {
				if (fd[i].value != "" && !fd[i].value.match(/^.+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/)) {
					$("#email-settings-server-from").removeClass("hidden");
            		retValue = false; 
				} else {
					$("#email-settings-server-from").addClass("hidden");
				}
			} else if (fd[i].name == "bccEmailAddress") {
				if (fd[i].value != "" && !fd[i].value.match(/^.+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,6}$/)) {
					$("#email-settings-server-bcc").removeClass("hidden");
            		retValue = false; 
				} else {
					$("#email-settings-server-bcc").addClass("hidden");
				}
			}
    	}
		if (!retValue) return false;
	  }
	  
	  var idFromEmailSettings;
	  var clidFromEmailSettings;
	  var titleFromEmailSettings;
	  function emailSettingsOk(responseText, statusText) {
	  	console.log(responseText);
	  	if (responseText == "ok") {
		  $("#email-settings-dialog").dialog("close");
		  $("#email-settings-dialog").addClass("hidden");
		  emailSettingsSet = "true";
		  executeReply(idFromEmailSettings, clidFromEmailSettings, titleFromEmailSettings);
		  emailSettingsError = null;
		}
	  }
	  
	  var replyDialogAddFileCt = 0;
	  function replyDialogAddFile() {
	  	$("#reply-dialog-file-list").append('<li id="'+replyDialogAddFileCt+'-reply-dialog-li"><input name="save-'+replyDialogAddFileCt+'" value="save" type="checkbox" title="Select to save file for later" checked="true"/><img title="" src="images/icons/paperclip.png"/><input title="Attachment" id="'+replyDialogAddFileCt+'-reply-dialog-file" name="file-'+replyDialogAddFileCt+'" type="file"/><a title="Remove attachment" id="'+replyDialogAddFileCt+'-reply-dialog-file-remove">Remove</a></li>');
		$("#"+replyDialogAddFileCt+"-reply-dialog-file-remove").click(function (s) {
		  $(s.currentTarget).parent().remove();
		});
		replyDialogAddFileCt++;
	  }
	  
	  function toggleFav(data, id) {
	  	console.log(data);
	  	if (data == 'ok') {
			$("div#fav-"+id).toggleClass("post-fav");
			$("div#fav-"+id).toggleClass("post-not-fav");
		}
	  }
	  
	  function setPostViewed(data, id) {
	  	if (data == 'ok') {
			$("div#viewed-"+id).removeClass("post-not-viewed");
			$("div#viewed-"+id).addClass("post-viewed");
		}
	  }
	  function reply(id, clid, title) {
	  	var hasNotReplied = $("#res-"+id).hasClass("post-not-res");
	  	if (altDown && shiftDown) {
			if (hasNotReplied) {
				autoReply(id, clid, title);
			} else {
				editAutoReply(id, clid, title);
			}
		} else if (altDown) {
			editAutoReply(id, clid, title);
		} else {
		  replyNew(id, clid, title);
		}
	  }
	  function autoReply(id, clid, title) {
	  	
	  }
	  function editAutoReply(id, clid, title) {
	  	initReply(id, clid, title);
		initSavedFiles(id);
	  }
	  function initSavedFiles(id) {
	  	$("#reply-dialog-saved-list").children().remove();
	  	$.post("sf", {id: id}, function(data) {
			if (data.substring(5,0) != "error") {
				$("#reply-dialog-saved-list").append(data);
				$("#reply-dialog-saved-list > li > a").each(function () {
					console.log($(this));
					$(this).click(function (s) {
						$(s.currentTarget).prev().prev().each(function () {
							this.checked = !this.checked;
						});
					});
				});
			}
			return true;
		});
	  }
	  function initReply(id, clid, title) {
	    $("#reply-dialog-text").val("");
		$("#reply-dialog-id").attr("value", id);
		$("#reply-dialog-subject").attr("value", "Re: " + title + " (CL:"+ clid + ")");
		$("#reply-dialog-file-list").children().remove();
		replyDialogAddFile();
		initSavedFiles();
	  }
	  function replyNew(id, clid, title) { 
	  	initReply(id, clid, title);
	  	executeReply(id, clid, title);
	  }
	  var emailSettingsError = "${model.emailSettingsError}";
	  var emailSettingsSet = "${model.emailSettingsSet}";
	  function executeReply(id, clid, title) {
	    if (emailSettingsSet == "true") {
		    
			$("#reply-dialog")
				.attr("title", "Post Reply (" + clid + ") - " + title)
				.addClass('flora')
				.removeClass("hidden")
				.dialog({ 
				buttons: { 
					"Send": function() { 
						$("#reply-dialog-form").submit();
						$(this).dialog("close");
						$("#reply-dialog").addClass("hidden");
					}, 
					"Cancel": function() { 
						$(this).dialog("close"); 
						$("#reply-dialog").addClass("hidden");
					} 
				},
				modal: true,
				width: 600,
				height: 500,
				overlay: { 
					opacity: 0.5, 
					background: "grey" 
				} 
			});
		} else {
			promptForEmailSettings(id, clid, title);
		}
	  }
	  function promptForEmailSettings(id, clid, title) {
	    idFromEmailSettings = id;
		clidFromEmailSettings = clid;
		titleFromEmailSettings = title;
		
	  	$("#email-settings-dialog")
			.attr("title", "Email Settings")
			.addClass('flora')
			.removeClass("hidden")
			.dialog({ 
			buttons: { 
				"Ok": function() { 
					$("#email-settings-form").submit();
				}, 
				"Cancel": function() { 
					$(this).dialog("close"); 
					$("#email-settings-dialog").addClass("hidden");
				} 
			},
			modal: true,
			width: 366,
			height: 440,
			overlay: { 
				opacity: 0.5, 
				background: "grey" 
			} 
		});
	  }
	  function addCity() {
	  	$.post("cities", {}, function(data) {
			$("#select-cities-wrapper").append(data);
		});
	  }
	  function addCategory() {
	  	$.post("cats", {}, function(data) {
			console.log(data);
			$("#select-cats-wrapper").append(data);
		});
	  }
	  function removeCity(s) {
		var cityId = $(s.currentTarget).attr("id").substring(12);
		$.post("unsubcity", {id: cityId}, function(id) {
			if ($("#city-"+id).hasClass("selected")) {
				$("#city-all").addClass("selected");
			}
			$("#city-"+id).remove();
			$("#flex1").flexReload();
			return true;
		});
	  }
	  function removeCategory(s) {
	  	var catId = $(s.currentTarget).attr("id").substring(11);
		$.post("unsubcat", {id: catId}, function(id) {
			if ($("#cat-"+id).hasClass("selected")) {
				$("#cat-all").addClass("selected");
			}
			$("#cat-"+id).remove();
			$("#flex1").flexReload();
			return true;
		});
	  }
</script>
</head>
<body id="body-mint" class="mint summary">
<div id="body-header">
  <%@ include file="/WEB-INF/jsp/links.jsp" %>
  <ul id="tabs-production" class="tabs">
    <li id="main" class="current"><a href="main">Main</a></li>
    <li id="email"><a href="email">Email Settings</a></li>
    <!--li id="mt" class=""><a href="mt">Message Templates</a></li-->
  </ul>
</div>
<div id="body-container">
  <div id="main">
    <div id="column-accounts" class="column">
      <!-- @group - Account Selector -->
      <ul id="fav-list" class="kill-9">
        <c:choose>
          <c:when test="${model.fav != null}">
            <li id="fav" class="selected"> 
          </c:when>
          <c:otherwise>
            <li id="fav"> 
          </c:otherwise>
        </c:choose>
        <a title="Favorites" href="fav"><img class="fav-img" src="images/icons/post-fav.png"/>Favorites</a>
        </li>
      </ul>
      <ul id="city-list" class="kill-9">
        <form id="city-selector-form" action="selectCity">
          <input id="city-selector-input" name="city" type="hidden" value="all"/>
          <input id="area-selector-input" name="area" type="hidden" value=""/>
          <c:choose>
            <c:when test="${model.fav == 'true' or model.city != null}">
              <li id="city-all"> 
            </c:when>
            <c:otherwise>
              <li id="city-all" class="selected"> 
            </c:otherwise>
          </c:choose>
          <img title="Add a city" class="city-add" src="images/icons/plus.png" onClick="addCity(); return true;"/> <a title="All Cities" onClick="$('#city-selector-form').submit(); return true;">All Cities</a>
          </li>
          <c:forEach var="cs" items="${model.prefs.citySubscriptions}">
            <c:if test="${cs.subscribed}">
              <c:set var="city" value="${cs.city}"/>
              <c:choose>
                <c:when test="${model.fav != 'true' and model.city == city.id}">
                  <li id="city-${city.id}" class="city selected"> 
                </c:when>
                <c:otherwise>
                  <li id="city-${city.id}" class="city"> 
                </c:otherwise>
              </c:choose>
              <img id="remove-city-${city.id}" title="Remove ${city.name}" class="city-remove" src="images/icons/minus.png"/> <a title="${city.name}" class="city" onClick="$('#city-selector-input').val('${city.id}'); $('#city-selector-form').submit(); return true;">
              <c:out value="${city.properName}"/>
              </a>
              <c:set var="areas" value="${city.areas}"/>
              <%
					double cityAreaCount = ((net.deuce.clmanager.domain.City)pageContext.getAttribute("city")).getAreaCount();
					int areaHeight = 12 * Double.valueOf(Math.ceil(cityAreaCount / 4.0)).intValue();
				%>
              <c:if test="${city.areaCount > 0}">
                <div class="area" style="height: <%=areaHeight%>px;">
                  <c:forEach var="area" items="${areas}">
                    <c:choose>
                      <c:when test="${model.fav != 'true' and model.area == area.id}">
                        <c:set var="areaSelected" value="selected"/>
                      </c:when>
                      <c:otherwise>
                        <c:set var="areaSelected" value=""/>
                      </c:otherwise>
                    </c:choose>
                    <a title="${area.name}" class="area ${areaSelected}" onClick="$('#city-selector-input').val('${city.id}'); $('#area-selector-input').val('${area.id}'); $('#city-selector-form').submit(); return true;">${area.externalName}</a> </c:forEach>
                </div>
              </c:if>
              </li>
            </c:if>
          </c:forEach>
        </form>
      </ul>
      <ul id="cat-list" class="kill-9">
        <form id="cat-selector-form" action="selectCat">
          <input id="cat-selector-input" name="cat" type="hidden" value="all"/>
          <c:choose>
            <c:when test="${model.fav == 'true' or model.cat != null}">
              <li id="cat-all"> 
            </c:when>
            <c:otherwise>
              <li id="cat-all" class="selected"> 
            </c:otherwise>
          </c:choose>
          <img title="Add a category" class="cat-add" src="images/icons/plus.png" onClick="addCategory(); return true;"/> <a title="All Categories" onClick="$('#cat-selector-form').submit(); return true;">All Categories</a>
          </li>
          <c:forEach var="cs" items="${model.prefs.categorySubscriptions}">
            <c:if test="${cs.subscribed}">
              <c:set var="cat" value="${cs.category}"/>
              <c:choose>
                <c:when test="${model.fav != 'true' and model.cat == cat.id}">
                  <li id="cat-${cat.id}" class="selected"> 
                </c:when>
                <c:otherwise>
                  <li id="cat-${cat.id}"> 
                </c:otherwise>
              </c:choose>
              <img id="remove-cat-${cat.id}" title="Remove ${cat.name}" class="cat-remove" src="images/icons/minus.png"/> <a title="${cat.name}" class="cat" onClick="$('#cat-selector-input').val('${cat.id}'); $('#cat-selector-form').submit(); return true;">
              <c:out value="${cat.externalName}"/>
              </a>
              </li>
            </c:if>
          </c:forEach>
        </form>
      </ul>
      <div class="clearfix"> </div>
    </div>
    <div id="column-transactions" class="column">
      <div id="controls-top" class="controls">
        <!--input type="submit" value="Flag" title="Flag selected post" id="controls-flag" class="submit"/>
        <input type="submit" value="New Reply" title="Construct a new reply to the selected post" id="controls-new-reply" class="submit"/>
        <input type="submit" value="Edit/Reply" title="Edit the selected message template before sending to the selected post" id="controls-edit-reply" class="submit"/>
        <input type="submit" value="Reply" title="Auto-reply to the selected post using the selected message template" id="controls-auto-reply" class="submit"/-->
        <label for="controls-message-templates">Message Template</label>
        <select title="Classify &amp; categorize with multiple custom labels. Label business or medical expenses, birthdays or vacations." id="controls-message-templates">
          <option value="">Category Default</option>
        </select>
      </div>
      <table id="flex1">
      </table>
      <script type="text/javascript">
	  
  $("#flex1").flexigrid
    (
    {
    url: 'posts',
    dataType: 'json',
    colModel : [
	  {display: 'ID', name : 'id', width : 0, sortable : false, align: 'left', hide: true},
      {display: '', name : 'flags', width : 80, sortable : false, align: 'left'},
      {display: 'Date', name : 'date', width : 30, sortable : false, align: 'left'},
      {display: 'Age', name : 'age', width : 20, sortable : false, align: 'left'},
      {display: 'Title', name : 'title', width : 327, sortable : false, align: 'left'},
	  {display: 'ClId', name : 'clid', width : 0, sortable : false, align: 'left', hide: true},
	  {display: 'Message', name : 'msg', width : 0, sortable : false, align: 'left', hide: true},
	  {display: 'City', name : 'city', width : 0, sortable : false, align: 'left', hide: true},
	  {display: 'Category', name : 'cat', width : 0, sortable : false, align: 'left', hide: true},
	  {display: 'CityArea', name : 'cityArea', width : 0, sortable : false, align: 'left', hide: true},
	  {display: 'Photos', name : 'photos', width : 0, sortable : false, align: 'left', hide: true},
    ],
    searchitems : [
      {display: 'All', name : 'all'}
    ],
    sortname: "clid",
    sortorder: "desc",
	sortable: false,
    usepager: true,
	resizable: false,
	resizeCol: false,
    title: false,
    useRp: true,
    rp: 25,
	rpOptions: [25,50,75,100],
	showToggleColBtn: true,
    showTableToggleBtn: false,
    width: 'auto',
    height: 300,
	singleRowSelection: true,
	minwidth: 0,
	pagestat: '{from} to {to} of {total}',
	onSubmit:
		function clearPost() {
			$("#post-detail").addClass("hide");
			$("#post-contents").html("");
			$("#post-photos").html("");
			return true;
		},
	onSuccess:
		function setFirstPost() {
			$("div.bDiv tbody tr:first").click();
			$("div.bDiv").scrollTop(0);
			$("div.post-fav").click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(4);
				$.post("tf", {id: id}, function(data) {
					toggleFav(data, id);
					return true;
				});
				return true;
			});
			$("div.post-not-fav").click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(4);
				$.post("tf", {id: id}, function(data) {
					toggleFav(data, id);
					return true;
				});
				return true;
			});
			$("div.post-not-res").click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(4);
				var clid = $(s.currentTarget).parent().parent().parent().parent().find("td:nth-child(6)").find("div").html();
				var title = $(s.currentTarget).parent().parent().parent().parent().find("td:nth-child(5)").find("div").html();
				reply(id, clid, title);
				return true;
			});
			$("div.post-res").click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(4);
				var clid = $(s.currentTarget).parent().parent().parent().parent().find("td:nth-child(6)").find("div").html();
				var title = $(s.currentTarget).parent().parent().parent().parent().find("td:nth-child(5)").find("div").html();
				replyNew(id, clid, title);
				return true;
			});
			$("div.post-not-flag").click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(5);
				$.post("flag", {id: id}, function(data) {
					return true;
				});
				return true;
			});
		},
	onSelectRow:
		function showPost(tr) {		
			$("#post-contents").html($(tr).find("td:nth-child(7)").find("div").html());
			$("#post-photos").html($(tr).find("td:nth-child(11)").find("div").html());
			$("#post-detail").removeClass("hide");
			$("#post-detail-clid").html($(tr).find("td:nth-child(6)").find("div").html());
			$("#post-detail-date").html($(tr).find("td:nth-child(3)").find("div").html());
			
			var cityArea = $.trim($(tr).find("td:nth-child(10)").find("div").html());
			if (cityArea != '' && cityArea != '&nbsp;') {
				$("#post-detail-city").html($(tr).find("td:nth-child(8)").find("div").html()+" ("+cityArea+")");
			} else {
				$("#post-detail-city").html($(tr).find("td:nth-child(8)").find("div").html());
			}
			$("#post-detail-cat").html($(tr).find("td:nth-child(9)").find("div").html());
			$("#post-detail-age").html($(tr).find("td:nth-child(4)").find("div").html());
			$("#post-detail-title").html($(tr).find("td:nth-child(5)").find("div").html());
			
			$(tr).find("div.post-not-viewed").each(function() {
				var id = $(tr).find("td:nth-child(1)").find("div").html();
				$.post("viewed", {id: id}, function(data) {
					setPostViewed(data, id);
					return true;
				});
				return true;
			});
		}
    }
  );
</script>
      <!--div id="controls-bottom" class="controls">
        <input type="submit" value="Flag" title="Flag selected post" id="controls-flag" class="submit"/>
        <input type="submit" value="New Reply" title="Construct a new reply to the selected post" id="controls-new-reply" class="submit"/>
        <input type="submit" value="Edit/Reply" title="Edit the selected message template before sending to the selected post" id="controls-edit-reply" class="submit"/>
        <input type="submit" value="Reply" title="Auto-reply to the selected post using the selected message template" id="controls-auto-reply" class="submit"/>
        <label for="controls-message-templates">Message Template</label>
        <select title="Classify &amp; categorize with multiple custom labels. Label business or medical expenses, birthdays or vacations." id="controls-message-templates">
          <option value="">Category Default</option>
        </select>
      </div-->
      <div id="post-contents-wrapper">
        <div id="post-detail-wrapper">
          <div id="post-detail" class="hide">
            <table>
              <tr>
                <td id="post-detail-clid"></td>
                <td></td>
              </tr>
              <tr>
                <td>Date:</td>
                <td id="post-detail-date"></td>
              </tr>
              <tr>
                <td>City:</td>
                <td id="post-detail-city"></td>
              </tr>
              <tr>
                <td>Category:</td>
                <td id="post-detail-cat"></td>
              </tr>
              <tr>
                <td>Age:</td>
                <td id="post-detail-age"></td>
              </tr>
              <tr>
                <td>Title:</td>
                <td id="post-detail-title"></td>
              </tr>
            </table>
          </div>
        </div>
        <div id="post-contents" class="scroll"></div>
      </div>
    </div>
    <div id="post-slideshow" class="scroll">
      <div id="post-photos" class="scroll">
        <ul>
        </ul>
      </div>
    </div>
  </div>
</div>
<div id="email-settings-dialog" title="SMTP Email Settings" class="hidden">
  <form id="email-settings-form" action="setemail" method="post">
    <fieldset>
    <ol>
      <li>
        <label for="email-settings-auth">SSL</label>
        <input id="email-settings-auth" type="checkbox" name="smtpAuth" value="true"/>
      </li>
      <li>
        <label for="email-settings-server">
        Server Name
        <div class="invalid">
          <div id="email-settings-server-invalid" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-server" class="text required" type="text" size="50" maxlength="256" name="smtpServer" />
      </li>
      <li>
        <label for="email-settings-port">
        Server Port
        <div class="invalid">
          <div id="email-settings-server-port" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-port" class="text number" type="text" size="50" maxlength="5" name="smtpPort" />
      </li>
      <li>
        <label for="email-settings-user">
        SMTP User
        <div class="invalid">
          <div id="email-settings-server-user" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-user" class="text required email" type="text" size="50" maxlength="256" name="smtpUser" />
      </li>
      <li>
        <label for="email-settings-password">
        SMTP Password
        <div class="invalid">
          <div id="email-settings-server-password" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-password" class="text required" type="password" size="50" maxlength="256" name="smtpPassword" />
      </li>
      <li>
        <label for="email-settings-from">
        From Email (Optional)
        <div class="invalid">
          <div id="email-settings-server-from" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-from" class="text" type="text email" size="50" maxlength="256" name="fromEmailAddress" />
      </li>
      <li>
        <label for="email-settings-bcc">
        Bcc Email  (Optional)
        <div class="invalid">
          <div id="email-settings-server-bcc" class="hidden">*</div>
        </div>
        </label>
        <input id="email-settings-bcc" class="text" type="text email" size="50" maxlength="256" name="bccEmailAddress" />
      </li>
    </ol>
    </fieldset>
  </form>
</div>
<div id="reply-dialog" title="Post Reply" class="hidden">
  <div id="reply-dialog-contents">
    <form id="reply-dialog-form" action="sendmail" method="post">
      <fieldset>
      <input id="reply-dialog-id" type="hidden" name="id" value="">
      </input>
      <!--div id="reply-dialog-mt-wrapper">
        <div id="reply-dialog-mt-div">
          <label title="" for="reply-dialog-mt-select">Message Template</label>
          <select class="text" "Select a message template" id="reply-dialog-mt-select" name="mt">
          </select>
        </div>
        <div id="reply-dialog-mt-save-div">
          <label title="" for="reply-dialog-mt-save">Save</label>
          <input  class="text" title="Save message template" type="checkbox" name="mtsave"/>
          <div class="spacer"></div>
          <label title="" for="reply-dialog-mt-save-name">Name</label>
          <input class="text" title="Enter template name" name="mtname" type="text" size="30" maxlength="256">
          </input>
        </div>
      </div-->
      <div id="reply-dialog-subject-div">
        <label id="reply-dialog-subject-label" title="" for="reply-dialog-subject">Subject:</label>
        <input title="Enter message subject" class="text" id="reply-dialog-subject" name="subject" type="text" size="60" maxlength="256">
        </input>
      </div>
      <textarea class="text" title="Enter message content" id="reply-dialog-text" name="message"></textarea>
      <div id="reply-dialog-file-upload">
        <div id="reply-dialog-file-list-div">
          <ul id="reply-dialog-file-list">
          </ul>
          <a id="reply-dialog-attach-another-a">Attach another file</a> </div>
        <div id="reply-dialog-saved-div">
          <p>Saved Files</p>
          <ul id="reply-dialog-saved-list">
          </ul>
        </div>
      </div>
      </fieldset>
    </form>
  </div>
</div>
<div id="select-cities-wrapper"> </div>
<div id="select-cats-wrapper"> </div>
<%@ include file="/WEB-INF/jsp/footer.jsp" %>
</body>
</html>
