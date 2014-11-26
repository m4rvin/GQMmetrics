<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="metricList.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
<div class="span10">
    <h2><fmt:message key='metricList.heading'/></h2>
    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/metricform'/>">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>
        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>
    <display:table name="metricList" class="table table-condensed table-striped table-hover" requestURI="" id="metricList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="metricform" media="html"
            paramId="id" paramProperty="id" titleKey="metric.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="metric.id"/>
        <display:column property="code" sortable="true" titleKey="metric.code"/>
        <display:column property="name" sortable="true" titleKey="metric.name"/>
        <display:column property="collectingType" sortable="true" titleKey="metric.collectingType"/>
        <display:column property="metricOwner.fullName" sortable="true" titleKey="metric.metricOwner"/>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="metricList.metric"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="metricList.metrics"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="metricList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="metricList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="metricList.title"/>.pdf</display:setProperty>
    </display:table>
</div>