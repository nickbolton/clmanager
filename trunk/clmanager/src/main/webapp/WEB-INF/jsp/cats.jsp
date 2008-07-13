<%@ include file="/WEB-INF/jsp/include.jsp" %>


<script type="text/javascript">
	$("#cat-selection-list").children().each(function () {
		$(this).children().each(function () {
			$(this).click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(10);
				$.post("subcat", {id: id}, function(id) {
					$("#add-cat-"+id).remove();
					return true;
				});
			});
		});
	});
	$("#select-cats-dialog")
		.attr("title", "Category Selection")
		.addClass('flora')
		.dialog({ 
		buttons: { 
			"Ok": function() { 
				$("#select-cats-form").submit();
				$(this).dialog("close");
				$("#select-cats-dialog").remove();
			}, 
		},
		modal: true,
		width: 300,
		height: 300,
		closeButton: false,
		overlay: { 
			opacity: 0.5, 
			background: "grey" 
		} 
	});
	$("#select-cats-searchfield").keypress(function (evt) {
		if (evt.which == 13) {
			return false;
		}
		return true;
	});
	$("#select-cats-searchfield").keyup(function (s) {
		$(s.currentTarget).next("ul").children().each(function () {
			var searchVal = $('#select-cats-searchfield').val();
			var objRegExp = new RegExp("^.*"+searchVal+".*$");
			if (objRegExp.test($(this).text()) || objRegExp.test($(this).find("a").attr("title"))) {
				$(this).removeClass("hidden");
			} else {
				$(this).addClass("hidden");
			}
		});
	});
</script>

<div id="select-cats-dialog" style="overflow: auto;">
	<form id="select-cats-form" action="main" method="get">
    	<input id="select-cats-searchfield" name="" type="text" size="50" maxlength="256" />
		<ul id="cat-selection-list">
			<c:forEach var="c" items="${model.cats}">
				<li id="add-cat-${c.id}"><a id="add-cat-a-${c.id}" title="${c.externalName}"><img id="add-cat-img-${c.id}" title="Add ${c.name}" class="cat-add" src="images/icons/plus.png" />${c.name}</a></li>
			</c:forEach>
		</ul>
	</form>
</div>