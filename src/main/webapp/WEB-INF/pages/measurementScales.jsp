<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="measurementScaleList.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
</head>
<div class="span10">
	<h2>
		<fmt:message key='measurementScaleList.heading' />
	</h2>
	<div id="actions" class="form-actions">
		<a class="btn btn-primary"
			href="<c:url value='/measurementScaleform'/>"> <i
			class="icon-plus icon-white"></i> <fmt:message key="button.add" /></a> <a
			class="btn" href="<c:url value='/mainMenu'/>"> <i class="icon-ok"></i>
			<fmt:message key="button.done" /></a>
	</div>
	<display:table name="measurementScaleList"
		class="table table-condensed table-striped table-hover" requestURI=""
		id="measurementScaleList" export="true" pagesize="25">
		<display:column property="id" sortable="true" href="measurementScaleform"
			media="html" paramId="id" paramProperty="id" titleKey="measurementScale.id" />
		<display:column property="id" media="csv excel xml pdf"
			titleKey="measurementScale.id" />

		<display:column property="name" sortable="true" titleKey="measurementScale.name" />
		<display:column property="rangeOfValues" sortable="true" titleKey="measurementScale.rangeOfValues" />
		<display:column property="operations" sortable="true" titleKey="measurementScale.operations" />
		
		<display:setProperty name="paging.banner.item_name">
			<fmt:message key="measurementScaleList.measurementScale" />
		</display:setProperty>
		<display:setProperty name="paging.banner.items_name">
			<fmt:message key="measurementScaleList.measurementScales" />
		</display:setProperty>
		<display:setProperty name="export.excel.filename">
			<fmt:message key="measurementScaleList.title" />.xls</display:setProperty>
		<display:setProperty name="export.csv.filename">
			<fmt:message key="measurementScaleList.title" />.csv</display:setProperty>
		<display:setProperty name="export.pdf.filename">
			<fmt:message key="measurementScaleList.title" />.pdf</display:setProperty>
	</display:table>
</div>