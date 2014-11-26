<%@ include file="/common/taglibs.jsp" %>

<head>
    <title><fmt:message key="menu.dataCollectionPhase.title"/></title>
    <meta name="menu" content="DataCollectionPhaseMenu"/>
</head>

<c:if test="${not empty searchError}">
    <div class="alert alert-error fade in">
        <a href="#" data-dismiss="alert" class="close">&times;</a>
        <c:out value="${searchError}"/>
    </div>
</c:if>

<div class="span10">
    <h2><fmt:message key="measurementList.heading"/></h2>

    <form method="get" action="${ctx}/measurements" id="searchForm" class="form-search">
    <div id="search" class="input-append">
        <input type="text" size="20" name="q" id="query" value="${param.q}"
               placeholder="<fmt:message key="search.enterTerms"/>" class="input-medium search-query"/>
        <button id="button.search" class="btn" type="submit">
            <i class="icon-search"></i> <fmt:message key="button.search"/>
        </button>
    </div>
    </form>

    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/measurementform?method=Add&from=list'/>">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>

        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>

    <display:table name="measurementList" cellspacing="0" cellpadding="0" requestURI=""
                   defaultsort="1" id="measurements" pagesize="25" class="table table-condensed table-striped table-hover" export="true">
        <display:column property="id" escapeXml="true" sortable="true" titleKey="measurement.id" style="width: 25%"
                        url="/measurementform?from=list" paramId="id" paramProperty="id"/>
		<display:column property="metricCode" sortable="true" titleKey="metric.code"/>        
		<display:column property="metric.name" sortable="true" titleKey="metric.name"/>
		<display:column property="value" sortable="true" titleKey="measurement.value"/>
		<display:column property="collectingDate" sortable="true" titleKey="measurement.collectingDate"/>
		<display:column property="collectingTime" sortable="true" titleKey="measurement.collectingTime"/>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="measurementList.measurement"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="measurementList.measurements"/></display:setProperty>
        <display:setProperty name="export.excel.filename" value="User List.xls"/>
        <display:setProperty name="export.csv.filename" value="User List.csv"/>
        <display:setProperty name="export.pdf.filename" value="User List.pdf"/>
    </display:table>
</div>
