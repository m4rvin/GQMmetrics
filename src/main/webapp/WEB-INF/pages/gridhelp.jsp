<%@ include file="/common/taglibs.jsp"%>
<%@ page import="org.json.JSONObject"%>

<head>
    <title><fmt:message key="goalDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
<body>

<c:set var="tree" scope="page" value="${tree}"/>
<div id="json_tree">
	<% 
	JSONObject jsonT = (JSONObject) pageContext.getAttribute("tree");
	//System.out.println(jsonT.toString());
	out.println(jsonT.toString());
	%>
</div>
</body>

