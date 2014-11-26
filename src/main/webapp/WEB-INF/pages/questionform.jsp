<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="questionDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 

<div class="span2">
    <h2><fmt:message key='questionDetail.heading'/></h2>
    <p><fmt:message key="questionDetail.message"/></p>	
	<p><fmt:message key="question.goals.message"/></p>	
	<ul>
		<c:forEach var="g" items="${question.goals}">
			<li><a href="/goalquestionform?goalId=${g.goal.id}&questionId=${g.question.id}">${g.goal.description} (${g.status})</a></li>			
		</c:forEach>		    	
	</ul>
	
	<p><fmt:message key="question.owner.message"/><b>&nbsp;&nbsp;&nbsp;${question.questionOwner.fullName}</b></p>
</div>


<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="question" method="post" action="questionform" id="questionForm"
               cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="question.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="question.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange=""  disabled="${question.questionOwner ne currentUser && not empty question.id}">
					<form:option value="${question.project.id}" label="${question.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>
         
    <div class="control-group">
        <appfuse:label styleClass="control-label" key="question.name"/>
        <div class="controls">
            <form:input path="name" id="name" maxlength="255" readonly="${question.questionOwner ne currentUser && not empty question.id}"/>
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>
    <div class="control-group">
        <appfuse:label styleClass="control-label" key="question.text"/>
        <div class="controls">
            <form:input path="text" id="text" maxlength="255" readonly="${question.questionOwner ne currentUser && not empty question.id}"/>
            <form:errors path="text" cssClass="help-inline"/>
        </div>
    </div>
	
	<c:if test="${not empty question.id}">
		<spring:bind path="question.goals">
	    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	    </spring:bind>
	        <appfuse:label styleClass="control-label" key="question.goals"/>
	        <div class="controls">
	        	<select id="goals" name="goals" multiple="multiple"  style="width:500px;" >
					<option value="">-- None --</option>					
					<c:forEach var="item" items="${availableGoals}">
						<c:set var="itemSelected" value="false" />
						<c:forEach var="item2" items="${question.goals}">	
							<c:choose>							
								<c:when test="${item2.goal eq item}">
									<c:set var="itemSelected" value="true" />
								</c:when>
							</c:choose>							
						</c:forEach>|
						<c:choose>
							<c:when test="${itemSelected eq true}">
								<option value="${item.id}|${question.id}" selected="selected">${item.description}</option>
							</c:when>
							<c:otherwise>
								<option value="${item.id}|${question.id}">${item.description}</option>
							</c:otherwise>
						</c:choose>					
						
					</c:forEach>        		
	        	</select>
	            <form:errors path="goals" cssClass="help-inline"/>   
	            * <br/> * This list contains the goals for which the user logged in is Question Stakeholder.                    
	        </div> 
	            
	    </div>    
    </c:if>
   
    <div class="form-actions">
        <c:if test="${question.questionOwner eq currentUser || empty question.id}">
			<button type="submit" class="btn btn-primary" name="save">
			    <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
			</button>
			<c:if test="${question.questionOwner eq currentUser && empty question.goals}">	
				 <button type="submit" class="btn" name="delete">
				     <i class="icon-trash"></i> <fmt:message key="button.delete"/>
				</button>        
			</c:if>
        </c:if>
      <button type="submit" class="btn" name="cancel">
      	<i class="icon-remove"></i> <fmt:message key="button.cancel"/>
      </button>
        
    </div>
    </form:form>
</div>
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['questionForm']).focus();
    });
</script>