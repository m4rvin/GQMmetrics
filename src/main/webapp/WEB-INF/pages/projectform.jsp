<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="projectDetail.title"/></title>
    <meta name="menu" content="ProjectMenu"/>
</head>
 
<div class="span2">
    <h2><fmt:message key='projectDetail.heading'/></h2>
</div>
<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="project" method="post" action="projectform" id="projectForm"
               cssClass="well form-horizontal">
    <form:hidden path="id"/>

       
    <spring:bind path="project.name">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="project.name"/>
        <div class="controls">
            <form:input path="name" id="name" maxlength="255"/>
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="project.description">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="project.description"/>
        <div class="controls">
            <form:input path="description" id="description" maxlength="255"/>
            <form:errors path="description" cssClass="help-inline"/>
        </div>
    </div>

    <div class="control-group">
        <appfuse:label styleClass="control-label" key="project.po"/>
        <div class="controls">      
            <form:select path="projectOwner.id" onchange="">
					<form:option value="${project.projectOwner.id}" label="${project.projectOwner.fullName}"/>
			</form:select>            	
            <form:errors path="projectOwner" cssClass="help-inline"/>
        </div>
    </div>   
        
    <div class="control-group">
        <appfuse:label styleClass="control-label" key="project.projectManagers"/>
        <div class="controls">        	
		    <form:select path="projectManagers" multiple="true"  size="6">
		    	<form:options items="${availableUsers}" itemLabel="fullName" itemValue="id"/>	
		    </form:select>       
            <form:errors path="projectManagers" cssClass="help-inline"/>
        </div>
    </div>   

    <div class="control-group">
        <appfuse:label styleClass="control-label" key="project.projectTeam"/>
        <div class="controls">        	
		    <form:select path="projectTeam" multiple="true"  size="6">
		    	<form:options items="${availableUsers}" itemLabel="fullName" itemValue="id"/>	
		    </form:select>       
            <form:errors path="projectTeam" cssClass="help-inline"/>
        </div>
    </div>  

    <div class="control-group">
        <appfuse:label styleClass="control-label" key="project.gqm"/>
        <div class="controls">        	
		    <form:select path="GQMTeam" multiple="true"  size="6">
		    	<form:options items="${availableUsers}" itemLabel="fullName" itemValue="id"/>	
		    </form:select>       
            <form:errors path="GQMTeam" cssClass="help-inline"/>
        </div>
    </div>  

    
    <div class="form-actions">
        <button type="submit" class="btn btn-primary" name="save">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </button>
        <c:if test="${not empty project.id}">
          <button type="submit" class="btn" name="delete">
              <i class="icon-trash"></i> <fmt:message key="button.delete"/>
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
        $("input[type='text']:visible:enabled:first", document.forms['projectForm']).focus();
    });
</script>