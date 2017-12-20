<%@ include file="./init.jsp" %>


<portlet:resourceURL var="pdfStatement" id="/download/pdf">
</portlet:resourceURL>

<p>
	<b><liferay-ui:message key="jasperreports-test.caption"/></b>
</p>
<aui:a href="${pdfStatement}"><liferay-ui:message key="jasperreports-test.download-pdf"></liferay-ui:message></aui:a>