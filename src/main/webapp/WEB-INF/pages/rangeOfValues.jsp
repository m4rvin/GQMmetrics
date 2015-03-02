<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="rangeOfValuesList.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
</head>
<div class="span10">
	<h2>
		<fmt:message key='rangeOfValuesList.heading' />
	</h2>
	<div id="actions" class="form-actions">
		<a class="btn btn-primary"
			href="<c:url value='/rangeOfValuesform'/>"> <i
			class="icon-plus icon-white"></i> <fmt:message key="button.add" /></a> <a
			class="btn" href="<c:url value='/mainMenu'/>"> <i class="icon-ok"></i>
			<fmt:message key="button.done" /></a>
	</div>

	<display:table name="rangeOfValuesList"
		class="table table-condensed table-striped table-hover" requestURI=""
		id="rangeOfValuesList" export="true" pagesize="25">
		<display:column property="id" sortable="true" href="rangeOfValuesform"
			media="html" paramId="id" paramProperty="id" titleKey="rangeOfValues.id" />
		<display:column property="id" media="csv excel xml pdf"
			titleKey="rangeOfValues.id" />

		<display:column property="name" sortable="true" titleKey="rangeOfValues.name" />
		<display:column property="rangeValues" sortable="true" titleKey="rangeOfValues.values" />
		<display:column property="defaultRange" sortable="true" titleKey="rangeOfValuesList.isDefault" />
		<display:column property="range" sortable="true" titleKey="rangeOfValuesList.range" />
		<display:column property="numeric" sortable="true" titleKey="rangeOfValuesList.isNumeric" />
		<display:column property="numberType" sortable="true" titleKey="rangeOfValues.numberType" />
		<display:column property="measurementScaleType" sortable="true" titleKey="rangeOfValues.measurementScaleType" />
		
		
		
		<display:setProperty name="paging.banner.item_name">
			<fmt:message key="rangeOfValuesList.rangeOfValues" />
		</display:setProperty>
		<display:setProperty name="paging.banner.items_name">
			<fmt:message key="rangeOfValuesList.rangesOfValues" />
		</display:setProperty>
		<display:setProperty name="export.excel.filename">
			<fmt:message key="rangeOfValuesList.title" />.xls</display:setProperty>
		<display:setProperty name="export.csv.filename">
			<fmt:message key="rangeOfValuesList.title" />.csv</display:setProperty>
		<display:setProperty name="export.pdf.filename">
			<fmt:message key="rangeOfValuesList.title" />.pdf</display:setProperty>
	</display:table>
</div>