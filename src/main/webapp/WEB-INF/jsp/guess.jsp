<%@ taglib prefix="portlet" uri="http://java.sun.com/portlet" %>

<h2>Hello ${model.username}. Can you guess a number
from ${model.minimum} to ${model.maximum}?</h2>

<form method="POST" action="<portlet:actionURL/>">
	<img src="/SimpleFormGuessNumber/images/guess.png"/>
	<input type="text" name="answer" value=""/>
	<input type="submit" name="submit" value="submit"/>
</form>

<i>Hint: The correct answer is <b>${model.number}</b></i>
