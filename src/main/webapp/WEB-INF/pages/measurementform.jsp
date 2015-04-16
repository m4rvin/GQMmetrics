<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="menu.dataCollectionPhase.title"/></title>
    <meta name="menu" content="DataCollectionPhaseMenu"/>
    
</head>
 

<div class="span2">
	<p><fmt:message key="measurement.message"/></p>	
</div>
<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="measurement" method="post" action="measurementform" id="measurementForm"
               cssClass="well form-horizontal">
    <form:hidden path="id"/>
    
	<c:if test="${not empty measurement.id}">
		<c:set var="existingMeasurement" value="true"/>
	</c:if> 

    <spring:bind path="measurement.metric">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="measurement.metric"/>
        <div class="controls">
   			<form:select path="metric" multiple="false"  disabled="${existingMeasurement}">
   				<form:option value="" label="None"/>
		    	<form:options items="${availableMetrics}" itemValue="id" itemLabel="name"/>
		    </form:select>		
            <form:errors path="metric" cssClass="help-inline"/>
        </div>
    </div> 

    <spring:bind path="measurement.collectingDate">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="measurement.collectingDate"/>
        <div class="controls">
            <form:input path="collectingDate" id="collectingDate" cssStyle="width:200px" 
            	disabled="${existingMeasurement}" />
            <form:errors path="collectingDate" cssClass="help-inline"/>
        </div>
    </div>	

    <spring:bind path="measurement.collectingTime">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="measurement.collectingTime"/>
        <div class="controls">
            <form:input path="collectingTime" id="collectingTime" cssStyle="width:200px" 
            	disabled="${existingMeasurement}"/>
            <form:errors path="collectingTime" cssClass="help-inline"/>
        </div>
    </div>
        
    <spring:bind path="measurement.value">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="measurement.value"/>
        <div class="controls">
            <form:input path="value" id="value" maxlength="50" readonly="${existingMeasurement}"/>
            <form:errors path="value" cssClass="help-inline"/>
        </div>
    </div>	    
    
    <div class="form-actions">
        <c:if test="${empty existingMeasurement}">
			<button type="submit" class="btn btn-primary" name="save">
			    <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
			</button>
			<%-- <c:if test="${measurement.measurementOwner eq currentUser}">	
				 <button type="submit" class="btn" name="delete">
				     <i class="icon-trash"></i> <fmt:message key="button.delete"/>
				</button>        
			</c:if> --%>
        </c:if>
      <button type="submit" class="btn" name="cancel">
      	<i class="icon-remove"></i> <fmt:message key="button.cancel"/>
      </button>        
    </div>
    </form:form>
    
</div>

 
<script type="text/javascript">
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['measurementForm']).focus();
    });
    
    $(function() {
      $( "#collectingDate" ).datepicker({
        showOn: "button",
        buttonImage: "images/calendar.gif",
        buttonImageOnly: true
      });
    });  
    $('#collectingTime').timepicker();
</script>