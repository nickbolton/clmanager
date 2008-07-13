<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>Untitled Document</title>
<script src="js/jquery.js" type="text/javascript"></script>
<script src="js/ui.core.js" type="text/javascript"></script>
<script src="js/ui.dialog.js" type="text/javascript"></script>
<script type="text/javascript" src="js/jquery.form.js"></script> 
<script type="text/javascript"> 
    $(document).ready(function() 
    { 
        $("#reply-dialog").addClass('flora').dialog({ 
			buttons: { 
				"Send": function() { 
					alert("Ok"); 
				}, 
				"Cancel": function() { 
					$(this).dialog("close"); 
				} 
			},
			modal: true, 
			overlay: { 
				opacity: 0.5, 
				background: "grey" 
			} 
		});
    } 
<script> 
</head>

<body>
<div id="reply-dialog" title="Post Reply"><textarea id="reply-dialog-text"/></div>
</body>
</html>
