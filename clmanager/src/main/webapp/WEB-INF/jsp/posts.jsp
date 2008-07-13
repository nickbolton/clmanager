<%@ include file="/WEB-INF/jsp/include.jsp" %>

<c:out escapeXml="false" value="{"/>
<c:out escapeXml="false" value="page: ${model.page},"/>
<c:out escapeXml="false" value="total: ${model.total},"/>
<c:out escapeXml="false" value="rows: ["/>

<c:set var="count" value="0"/>
<c:forEach var="p" items="${model.posts}">
    <c:if test="${count > 0}">
        <c:out escapeXml="false" value=","/>
    </c:if>
    <c:set var="count" value="${count + 1}"/>
    <c:out escapeXml="false" value="{"/>
        <c:out escapeXml="false" value="id:'${p.id}',"/>
        <c:out escapeXml="false" value="cell:["/>
            <c:out escapeXml="false" value="'${p.id}'"/>

            <c:out escapeXml="false" value=",'"/><c:out escapeXml="false" value="<div id=\"flags-${p.id}\" class=\"post-flags\">"/><c:choose><c:when test="${p.viewed}"><c:out escapeXml="false" value="<div title=\"Post previously viewed\" id=\"viewed-${p.id}\" class=\"post-viewed\"><img src=\"images/spacer.gif\"/></div>"/></c:when><c:otherwise><c:out escapeXml="false" value="<div title=\"Post not yet viewed\" id=\"viewed-${p.id}\" class=\"post-not-viewed\"><img src=\"images/spacer.gif\"/></div>"/></c:otherwise></c:choose><c:choose><c:when test="${p.favorite}"><c:out escapeXml="false" value="<div id=\"fav-${p.id}\" class=\"post-fav\"><img src=\"images/spacer.gif\"/></div>"/></c:when><c:otherwise><c:out escapeXml="false" value="<div id=\"fav-${p.id}\" class=\"post-not-fav\"><img src=\"images/spacer.gif\"/></div>"/></c:otherwise></c:choose><c:choose><c:when test="${p.responded}"><c:out escapeXml="false" value="<div id=\"res-${p.id}\" class=\"post-res\"><img src=\"images/spacer.gif\"/></div>"/></c:when><c:otherwise><c:out escapeXml="false" value="<div id=\"res-${p.id}\" class=\"post-not-res\"><img src=\"images/spacer.gif\"/></div>"/></c:otherwise></c:choose><c:choose><c:when test="${p.flagged}"><c:out escapeXml="false" value="<div id=\"flag-${p.id}\" class=\"post-flag\"><img src=\"images/spacer.gif\"/></div>"/></c:when><c:otherwise><c:out escapeXml="false" value="<div id=\"flag-${p.id}\" class=\"post-not-flag\"><img src=\"images/spacer.gif\"/></div>"/></c:otherwise></c:choose><c:choose><c:when test="${p.pic}"><c:out escapeXml="false" value="<div id=\"pic-${p.id}\" class=\"post-pic\"><img src=\"images/spacer.gif\"/></div>"/></c:when><c:otherwise><c:out escapeXml="false" value="<div id=\"pic-${p.id}\" class=\"post-not-pic\"><img src=\"images/spacer.gif\"/></div>"/></c:otherwise></c:choose></div><c:out escapeXml="false" value="'"/>

            <c:out escapeXml="false" value=",'"/><fmt:formatDate value="${p.date}" pattern="MM/dd"/><c:out escapeXml="false" value="'"/>

            <c:out escapeXml="false" value=",'"/><c:out escapeXml="false" value="${p.age}"/><c:out escapeXml="false" value="'"/>

            <c:out escapeXml="false" value=",'${p.title}'"/>

            <c:out escapeXml="false" value=",'${p.clId}'"/>

            <c:out escapeXml="false" value=",'${p.content}'"/>
            <c:out escapeXml="false" value=",'${p.city}'"/>
            <c:out escapeXml="false" value=",'${p.category}'"/>
            <c:out escapeXml="false" value=",'${p.cityArea}'"/>

			<c:set var="server"><%=request.getServerName()%></c:set>
            
            <c:out escapeXml="false" value=",'<ul>"/><c:forEach var="img" items="${p.photos}"><c:out escapeXml="false" value="<li><img src=\"http://${server}/data/images/${img.path}\" /></li>"/></c:forEach><c:out escapeXml="false" value="</ul>'"/>
            
    <c:out escapeXml="false" value="]}"/>
</c:forEach>

<c:out escapeXml="false" value="]}"/>
