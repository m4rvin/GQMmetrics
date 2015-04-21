<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="satisfyingConditionList.title" /></title>
<meta name="menu" content="InterpretationPhaseMenu" />
</head>
<div class="span10">
	<h2>
		<fmt:message key='satisfyingConditionList.heading' />
	</h2>
	<div id="actions" class="form-actions">
		<a class="btn btn-primary" href="<c:url value='/satisfyingConditionform'/>"> 
			<i class="icon-plus icon-white"></i> 
			<fmt:message key="button.add" />
		</a> 
		<a class="btn" href="<c:url value='/mainMenu'/>">
			<i class="icon-ok"></i>
			<fmt:message key="button.done" />
		</a>
	</div>

	<display:table name="satisfyingConditionList" class="table table-condensed table-striped table-hover" requestURI="" id="satisfyingConditionList" export="true" pagesize="25">
		<display:column property="id" sortable="true" href="satisfyingConditionform" media="html" paramId="id" paramProperty="id" titleKey="satisfyingConditionList.id" />
		<display:column property="id" media="csv excel xml pdf" titleKey="satisfyingConditionList.id" />
		<display:column property="satisfyingConditionOperation" sortable="true" titleKey="satisfyingConditionList.satisfyingConditionOperation" />
		<display:column property="satisfyingConditionValue" sortable="true" titleKey="satisfyingConditionList.satisfyingConditionValue" />
		<display:setProperty name="paging.banner.item_name">
			<fmt:message key="satisfyingConditionList.satisfyingCondition" />
		</display:setProperty>
		<display:setProperty name="paging.banner.items_name">
			<fmt:message key="satisfyingConditionList.satisfyingCondition" />
		</display:setProperty>
		<display:setProperty name="export.excel.filename">
			<fmt:message key="satisfyingConditionList.title" />.xls</display:setProperty>
		<display:setProperty name="export.csv.filename">
			<fmt:message key="satisfyingConditionList.title" />.csv</display:setProperty>
		<display:setProperty name="export.pdf.filename">
			<fmt:message key="satisfyingConditionList.title" />.pdf</display:setProperty>
	</display:table>
</div>