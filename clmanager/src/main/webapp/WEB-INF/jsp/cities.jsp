<%@ include file="/WEB-INF/jsp/include.jsp" %>


<script type="text/javascript">
	$("#city-selection-list").children().each(function () {
		$(this).children().each(function () {
			$(this).click(function(s) {
				var id = $(s.currentTarget).attr("id").substring(11);
				$.post("subcity", {id: id}, function(id) {
					$("#add-city-"+id).remove();
					return true;
				});
			});
		});
	});
	$("#select-cities-dialog")
		.attr("title", "City Selection")
		.addClass('flora')
		.dialog({ 
		buttons: { 
			"Ok": function() { 
				$("#select-cities-form").submit();
				$(this).dialog("close");
				$("#select-cities-dialog").remove();
			}, 
		},
		modal: true,
		width: 300,
		height: 300,
		overlay: { 
			opacity: 0.5, 
			background: "grey" 
		} 
	});
	$("#select-cities-searchfield").keypress(function (evt) {
		if (evt.which == 13) {
			return false;
		}
		return true;
	});
	$("#select-cities-searchfield").keyup(function (s) {
		$(s.currentTarget).next("ul").children().each(function () {
			var searchVal = $('#select-cities-searchfield').val();
			var objRegExp = new RegExp("^.*"+searchVal+".*$");
			if (objRegExp.test($(this).text())) {
				$(this).removeClass("hidden");
			} else {
				$(this).addClass("hidden");
			}
		});
	});
</script>

<div id="select-cities-dialog" style="overflow: auto;">
	<form id="select-cities-form" action="main" method="get">
    	<input id="select-cities-searchfield" name="" type="text" size="50" maxlength="256" />
		<ul id="city-selection-list">
			<c:forEach var="c" items="${model.cities}">
				<li id="add-city-${c.id}"><a id="add-city-a-${c.id}" title="${c.name}"><img id="add-city-img-${c.id}" title="Add ${c.name}" class="city-add" src="images/icons/plus.png" />${c.name}</a></li>
			</c:forEach>
		</ul>
	</form>
</div>