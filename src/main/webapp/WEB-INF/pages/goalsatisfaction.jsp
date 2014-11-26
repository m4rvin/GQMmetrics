<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="Goal Satisfaction"/></title>
    <meta name="menu" content="InterpretationPhaseMenu"/>
</head>


<div class="span10">
    <h2><fmt:message key='goalSatisfaction.goal.heading'/></h2>
	
    <display:table name="goalList" class="table table-condensed table-striped table-hover" requestURI="" id="goalList" export="true" pagesize="5">
        <display:column property="id" sortable="true" href="/goalsatisfaction" media="html"
            paramId="g" paramProperty="id" titleKey="goal.id"/>
        <display:column property="id" media="csv excel xml pdf" titleKey="goal.id" />
        <display:column property="description" sortable="true" titleKey="goal.description"/>
        <display:column property="type" sortable="true" titleKey="goal.type"/>      
        <display:column property="focus" sortable="true" titleKey="goal.focus"/>
        <display:setProperty name="paging.banner.item_name"><fmt:message key="goalList.goal"/></display:setProperty>
        <display:setProperty name="paging.banner.items_name"><fmt:message key="goalList.goals"/></display:setProperty>
        <display:setProperty name="export.excel.filename"><fmt:message key="goalList.title"/>.xls</display:setProperty>
        <display:setProperty name="export.csv.filename"><fmt:message key="goalList.title"/>.csv</display:setProperty>
        <display:setProperty name="export.pdf.filename"><fmt:message key="goalList.title"/>.pdf</display:setProperty>
    </display:table>
   
   <c:if test="${not empty currentGoal.id}">
	   <h2>Current Goal: ${currentGoal.id} - <fmt:message key='goalSatisfaction.metric.heading'/></h2>
	   <display:table name="metrics" class="table table-condensed table-striped table-hover" requestURI="" 
	   	id="metrics" export="true" pagesize="25">
       

        	<display:column property="id" sortable="true" href="#" url="/charts/draw" media="html"
            		paramId="id" paramProperty="id" titleKey="metric.id" />	 
	        <display:column property="id" media="csv excel xml pdf" titleKey="metric.id"/>
	        <c:choose>
	        	<c:when test="${metrics.measuredValue.toString() eq 'NaN'}">
					<display:column sortable="true" style="background-image: url('../images/gray.gif');width:20px;"/>	        	
	        	</c:when>
	        	<c:otherwise>
	        		<c:choose>
	        			<c:when test="${metrics.conditionReached}">
	        				<display:column sortable="true" style="background-image: url('../images/green.gif');width:20px;"/>
	        			</c:when>
	        			<c:otherwise>
	        				<display:column sortable="true" style="background-image: url('../images/red.gif');width:20px;"/>
	        			</c:otherwise>
	        		</c:choose>
	        	</c:otherwise>
	        </c:choose> 
	        <display:column property="code" sortable="true" titleKey="metric.code"/>
	        <display:column property="name" sortable="true" titleKey="metric.name"/>
	        <display:column property="collectingType" sortable="true" titleKey="metric.collectingType"/>
	        <display:column property="formula" sortable="true" titleKey="metric.formula"/>       
	        <display:column property="measuredValue" sortable="true" titleKey="metric.measuredValue"/>
	        <display:column property="satisfyingConditionOperation" sortable="true" titleKey="metric.satisfyingConditionOperation"/>
	        <display:column property="satisfyingConditionValue" sortable="true" titleKey="metric.satisfyingConditionValue"/>      
	        <display:setProperty name="paging.banner.item_name"><fmt:message key="metrics.metric"/></display:setProperty>
	        <display:setProperty name="paging.banner.items_name"><fmt:message key="metrics.metrics"/></display:setProperty>
	        <display:setProperty name="export.excel.filename"><fmt:message key="metrics.title"/>.xls</display:setProperty>
	        <display:setProperty name="export.csv.filename"><fmt:message key="metrics.title"/>.csv</display:setProperty>
	        <display:setProperty name="export.pdf.filename"><fmt:message key="metrics.title"/>.pdf</display:setProperty>
	    </display:table>
    </c:if>
</div>

<div class="span2">
    <h2><fmt:message key="Chart"/></h2>
    <img alt="GQM+Strategies" src="images/GQM+Strategies_logo.jpg"/>
    Please, select a metric for draw a chart:
    <select id="goals" name="goals" onchange="location = this.options[this.selectedIndex].value;">
    	<option value="">None</option>
		<c:forEach var="item" items="${availableMetrics}">
			<option value="/charts/draw?id=${item.id}">[${item.id}] - ${item.code}</option>
		</c:forEach>
	</select>
</div>