<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.json.JSONObject"%>

<head>
    <title><fmt:message key="goalDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
<body>

<c:set var="graph" scope="page" value="${graph}"/>
<div id="json_graph">
	<% 
	JSONObject jsonT = (JSONObject) pageContext.getAttribute("graph");
	//System.out.println(jsonT.toString());
	out.println(jsonT.toString());
	%>
</div>
</body>

