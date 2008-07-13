<%@ include file="/WEB-INF/jsp/include.jsp" %><c:forEach var="mt" items="${model.mt}"><li id="mt-${mt.id}" class="mt">${mt.name}</li></c:forEach>
