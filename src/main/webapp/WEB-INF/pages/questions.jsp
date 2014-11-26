<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="questionList.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
<div class="span10">
    <h2><fmt:message key='questionList.heading'/></h2>
    <div id="actions" class="form-actions">
        <a class="btn btn-primary" href="<c:url value='/questionform'/>">
            <i class="icon-plus icon-white"></i> <fmt:message key="button.add"/></a>
        <a class="btn" href="<c:url value='/mainMenu'/>">
            <i class="icon-ok"></i> <fmt:message key="button.done"/></a>
    </div>
    <display:table name="questionList" class="table table-condensed table-striped table-hover" requestURI="" id="questionList" export="true" pagesize="25">
        <display:column property="id" sortable="true" href="questionform" media="html"
            paramId="id" paramProperty="id" titleKey="question.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="question.id"/>
        <display:column property="name" sortable="true" titleKey="question.name"/>
        <display:column property="text" sortable="true" titleKey="question.text"/>
        <display:column property="questionOwner.fullName" sortable="true" titleKey="question.questionOwner"/>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="questionList.question"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="questionList.questions"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="questionList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="questionList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="questionList.title"/>.pdf</display:setProperty>
    </display:table>
</div>