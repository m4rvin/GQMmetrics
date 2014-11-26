<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="questionMetricDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 
<div class="span2">
    <h2><fmt:message key='questionMetricDetail.heading'/></h2>
    <c:choose>
    	<c:when test="${questionMetric.metric.metricOwner eq currentUser}">You are <b>Metric Owner</b> for this Metric.
    		<c:set var="metricOwner" value="true"/>
    	</c:when>	
    	 <c:when test="${questionMetric.question.questionOwner eq currentUser}">You are <b>Question Stakeholder</b> for this Question</c:when>	
	</c:choose>
    
</div>

<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="questionMetric" method="post" action="questionmetricform" id="questionmetricform"
               cssClass="well form-horizontal">
    
    <form:hidden path="pk.metric.id"/>
    <form:hidden path="pk.question.id"/>
    
    <spring:bind path="questionMetric.question">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="questionMetric.question.text"/>
        <div class="controls">
			<form:input path="question.text" id="question.text" maxlength="255" disabled="true" readonly="true"/>
            <form:errors path="question" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="questionMetric.metric">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="questionMetric.metric.name"/>
        <div class="controls">
            <form:input path="metric.name" id="metric.name" maxlength="255" disabled="true" readonly="true"/>
            <form:errors path="metric" cssClass="help-inline"/>
        </div>
    </div>

    <spring:bind path="status">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="questionMetric.status"/>
        <div class="controls">
			<form:select path="status" multiple="false" >
		    	<form:options items="${availableStatus}"/>
		    </form:select>		
        </div>
    </div>    
    
     <c:if test="${((metricOwner eq true && questionMetric.status eq 'FOR_REVIEW') || (questionMetric.status eq 'PROPOSED'))}">
	    <spring:bind path="refinement">
	    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	    </spring:bind>
	        <appfuse:label styleClass="control-label" key="questionMetric.refinement"/>
	        <div class="controls">
	            <form:input path="refinement" id="refinement" maxlength="255" readonly="${goal.goalOwner eq currentUser}"	/>
	            <form:errors path="refinement" cssClass="help-inline"/>
	        </div>
	    </div>
    </c:if>
    
    <div class="form-actions">
    	<c:if test="${not empty questionMetric.pk.question.id && not empty questionMetric.pk.metric.id}">
	        <button type="submit" class="btn btn-primary" name="save">
	            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
	        </button>        
        </c:if>
        <button type="submit" class="btn" name="cancel">
            <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
        </button>
    </div>
    </form:form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['questionMetricForm']).focus();
    });
</script>