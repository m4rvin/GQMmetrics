<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="strategyList.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
<div class="span10">
    <h2><fmt:message key='strategyList.heading'/></h2>
    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/strategyform'/>">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>
        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>
    <display:table name="strategyList" class="table table-condensed table-striped table-hover" requestURI="" id="strategyList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="/strategyform" media="html"
            paramId="id" paramProperty="id" titleKey="strategy.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="strategy.id" />
        <display:column property="name" sortable="true" titleKey="strategy.name"/>
        <display:column property="assumption" sortable="true" titleKey="strategy.assumption"/>
        <display:column property="strategyOwner.fullName" sortable="true" titleKey="strategy.strategyOwner"/>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="strategyList.strategy"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="strategyList.strategies"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="goalList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="goalList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="goalList.title"/>.pdf</display:setProperty>
    </display:table>
</div>