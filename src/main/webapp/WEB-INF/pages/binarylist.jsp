<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="binaryTable.title"/></title>
    <meta name="menu" content="InterpretationPhaseMenu"/>
</head>
<div class="span10">
    <h2><fmt:message key='binaryTable.heading'/></h2>
    
    <display:table name="goalList" class="table table-condensed table-striped table-hover" requestURI="" id="goalList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="/binarytable" media="html" paramId="id" paramProperty="id" titleKey="goal.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="goal.id" />
        <display:column property="description" sortable="true" titleKey="goal.description"/>
        <display:column property="scope" sortable="true" titleKey="goal.scope"/>        
        <display:column property="focus" sortable="true" titleKey="goal.focus"/>
        <display:column property="goalOwner.fullName" sortable="true" titleKey="goal.go"/>
        <display:column property="goalEnactor.fullName" sortable="true" titleKey="goal.ge"/>
        <display:column property="typeAsString" sortable="true" titleKey="goal.type"/>
        <display:column property="status" sortable="true" titleKey="goal.status"/>	    
        <display:setProperty name="paging.banner.item_name"><fmt:message key="goalList.goal"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="goalList.goals"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="goalList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="goalList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="goalList.title"/>.pdf</display:setProperty>
    </display:table>
</div>