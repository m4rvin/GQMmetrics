<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="goalQuestionDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 
<div class="span2">
    <h2><fmt:message key='goalQuestionDetail.heading'/></h2>
    <c:choose>
    	<c:when test="${goalQuestion.question.questionOwner eq currentUser}">You are <b>Question Owner</b> for this Question.
    		<c:set var="questionOwner" value="true"/>
    	</c:when>		
		<c:when test="${goalQuestion.goal.QSMembers.contains(currentUser)}">You are <b>Question Stakeholder</b> for this Question.</c:when>
		<c:when test="${goalQuestion.goal.MMDMMembers.contains(currentUser)}">You are <b>Metric Model Data Manager</b> for this Question.
			<c:set var="mmdm" value="true"/>
		</c:when>		
	</c:choose>
    
</div>

<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="goalQuestion" method="post" action="goalquestionform" id="goalquestionform"
               cssClass="well form-horizontal">
    
    <form:hidden path="pk.goal.id"/>
    <form:hidden path="pk.question.id"/>
    
    <spring:bind path="goalQuestion.goal">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goalQuestion.goal.description"/>
        <div class="controls">
			<form:input path="goal.description" id="goal.description" maxlength="255" disabled="true" readonly="true"/>
            <form:errors path="goal" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="goalQuestion.question">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goalQuestion.question.text"/>
        <div class="controls">
            <form:input path="question.text" id="question.text" maxlength="255" disabled="true" readonly="true"/>
            <form:errors path="question" cssClass="help-inline"/>
        </div>
    </div>

    <spring:bind path="goal.status">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.status"/>
        <div class="controls">
			<form:select path="status" multiple="false" >
		    	<form:options items="${availableStatus}"/>
		    </form:select>		
        </div>        
    </div>
            
     <c:if test="${((questionOwner eq true && goalQuestion.status eq 'FOR_REVIEW') || (mmdm eq true &&  goalQuestion.status eq 'PROPOSED'))}">
	    <spring:bind path="goal.refinement">
	    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	    </spring:bind>
	        <appfuse:label styleClass="control-label" key="goal.refinement"/>
	        <div class="controls">
	            <form:input path="refinement" id="refinement" maxlength="255" readonly="${goal.goalOwner eq currentUser}"	/>
	            <form:errors path="refinement" cssClass="help-inline"/>
	        </div>
	    </div>
    </c:if>
    <div class="form-actions">
    	<c:if test="${not empty goalQuestion.pk.goal.id && not empty goalQuestion.pk.question.id}">
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
        $("input[type='text']:visible:enabled:first", document.forms['goalQuestionForm']).focus();
    });
</script>