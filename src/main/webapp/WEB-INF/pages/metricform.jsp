<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="metricDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 

<div class="span2">
    <h2><fmt:message key='metricDetail.heading'/></h2>
    <p><fmt:message key="metricDetail.message"/></p>	
	<p><fmt:message key="metric.goals.message"/></p>		
	<p><fmt:message key="metric.owner.message"/></p><b>&nbsp;&nbsp;&nbsp;${metric.metricOwner.fullName}</b>
	
	<p><fmt:message key="questionMetric.question.message"/></p>	
	<ul>
		<c:forEach var="m" items="${metric.questions}">
			<li><a href="/questionmetricform?questionId=${m.question.id}&metricId=${m.metric.id}">${m.question.name} (${m.status})</a></li>			
		</c:forEach>		    	
	</ul>
</div>


<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="metric" method="post" action="metricform" id="metricForm" cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="metric.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange=""  disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
					<form:option value="${metric.project.id}" label="${metric.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>
    <spring:bind path="metric.code">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.code"/>
        <div class="controls">
            <form:input path="code" id="code" maxlength="50" readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="code" cssClass="help-inline"/>        
        </div>
    </div>
             
    <spring:bind path="metric.name">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.name"/>
        <div class="controls">
            <form:input path="name" id="name" maxlength="255" readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>	    
    
    <spring:bind path="metric.type">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.type"/>
        <div class="controls">
   			<form:select path="type" multiple="false" disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
   				<form:option value="" label="None"/>
		    	<form:options items="${availablesTypes}"/>
		    </form:select>		
            <form:errors path="type" cssClass="help-inline"/>
        </div>
    </div> 
    
    <spring:bind path="metric.unit">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.unit"/>
        <div class="controls">
			<form:select path="unit.id" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
   				<form:option value="" label="None"/>
		    	<form:options items="${units}" itemValue="id" itemLabel="name" />
		    </form:select>		
            <form:errors path="unit" cssClass="help-inline"/>
        </div>
    </div>    

    <spring:bind path="metric.measurementScale">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.measurementScale"/>
        <div class="controls">
   			<form:select path="measurementScale"  disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
   			   	<form:option value="" label="None"/>
		    	<form:options items="${measurementScales}" itemValue="id" itemLabel="name"/>
		    </form:select>		
            <form:errors path="measurementScale" cssClass="help-inline"/>
        </div>
    </div>  

    <spring:bind path="metric.metricA.id">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.metricA"/>
        <div class="controls">
			<form:select path="metricA.id" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
				<form:option value="" label="None"/>
				<form:options items="${availableMetrics}" itemValue="id" itemLabel="code"/>
		    </form:select>		
            <form:errors path="metricA" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="metric.operation">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.operation"/>
        <div class="controls">
			<form:select path="operation" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
				<form:option value="" label="None"/>
				<form:option value="ADDITION"/>
				<form:option value="DIVISION"/>
				<form:option value="SUBTRACTION"/>
				<form:option value="MULTIPLICATION"/>
		    </form:select>		
            <form:errors path="operation" cssClass="help-inline"/>            
        </div>
    </div>


    <spring:bind path="metric.metricB.id">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.metricB"/>
        <div class="controls">
			<form:select path="metricB.id" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
				<form:option value="" label="None"/>
				<form:options items="${availableMetrics}" itemValue="id" itemLabel="code"/>
		    </form:select>		
            <form:errors path="metricB" cssClass="help-inline"/>            
        </div>
    </div>

            
    <spring:bind path="metric.hypothesis">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.hypothesis"/>
        <div class="controls">
            <form:input path="hypothesis" id="hypothesis" maxlength="255" readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="hypothesis" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="metric.actualValue">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.actualValue"/>
        <div class="controls">
            <form:input path="actualValue" id="actualValue"  readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="actualValue" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="metric.collectingType">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.collectingType"/>
        <div class="controls">
			<form:select path="collectingType" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
				<form:option value="SINGLE_VALUE" label="Single Value"/>
				<form:option value="MULTIPLE_VALUE" label="Multiple Value"/>
		    </form:select>		
            <form:errors path="collectingType" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="metric.satisfyingConditionOperation">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.satisfyingConditionOperation"/>
        <div class="controls">
			<form:select path="satisfyingConditionOperation" disabled="${metric.metricOwner ne currentUser && not empty metric.id}" >
				<form:option value="NONE" label="None"/>
				<form:option value="LESS" label="<"/>
				<form:option value="LESS_OR_EQUAL" label="<="/>
				<form:option value="EQUAL" label="="/>
				<form:option value="GREATHER" label=">"/>
				<form:option value="GREATER_OR_EQUAL" label=">="/>
		    </form:select>		
            <form:errors path="satisfyingConditionOperation" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="metric.satisfyingConditionValue">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.satisfyingConditionValue"/>
        <div class="controls">
            <form:input path="satisfyingConditionValue" id="satisfyingConditionValue"  readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="satisfyingConditionValue" cssClass="help-inline"/>
        </div>
    </div>
            
	<c:if test="${not empty metric.id}">
		<spring:bind path="metric.questions">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    <appfuse:label styleClass="control-label" key="metric.questions"/>
	        	<div class="controls">
	        		<select id="questions" name="questions" multiple="multiple"  style="width:500px;" onchange="showIMBox()">
						<option value="">-- None --</option>
						<c:forEach var="item" items="${availableQuestions}">
							<c:set var="itemSelected" value="false" />
							<c:forEach var="item2" items="${metric.questions}">	
								<c:choose>							
									<c:when test="${item2.question eq item}">
										<c:set var="itemSelected" value="true" />
									</c:when>
								</c:choose>							
							</c:forEach>|
							<c:choose>
								<c:when test="${itemSelected eq true}">
									<option value="${item.id}|${metric.id}" selected="selected">${item.text}</option>
								</c:when>
								<c:otherwise>
									<option value="${item.id}|${metric.id}">${item.text}</option>
								</c:otherwise>
							</c:choose>												
						</c:forEach>
											
	        		</select>
	        		* <br/> * This list contains the questions associated to the goal for which the user logged in is Measurement Model Design Manager.
	        	</div>
	    </spring:bind>
	</c:if>	
	
	<div id="imbox" title="Interpretation model help box" hidden="true">
		<c:set var="map" value="${map}"></c:set>
		<c:forEach var="question" items="${availableQuestions}">
			<c:set var="goalset" value="${map[question.id]}"></c:set>
				<c:forEach var="goal" items="${goalset}">
	
	        		<div id="${question.id}" hidden="true"><b>OG: ${goal.id}</b> 
						<div>Magnitude: ${goal.timeframe}</div>
						<div>Timeframe: ${goal.magnitude}</div>
					</div>
	
	        	</c:forEach>
  		</c:forEach>
	</div>
       
    <div class="form-actions">
        <c:if test="${metric.metricOwner eq currentUser || empty metric.id}">
			<button type="submit" class="btn btn-primary" name="save">
			    <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
			</button>
			<c:if test="${metric.metricOwner eq currentUser}">	
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

	function showIMBox(){
		
		var e = document.getElementById("questions");
		var selectedOptions = getSelectValues(e);
		console.log(selectedOptions);
		if(selectedOptions[0] != "") {
			
			if(selectedOptions.length > 0){
				
				//reimposto tutte le opzioni della select a hidden
				for(var i=0, optLen= e.length; i<optLen; i++) {
					$('#'+e.options[i].value.substring(0,1)).attr('hidden', 'true');
				}
				
				$('#imbox').dialog( {
				      resizable: true,
				      modal: false
				    });
				
				//$('#imbox').removeAttr('hidden');
				
				for(var i=0, optLen= selectedOptions.length; i<optLen; i++) {
					$('#'+selectedOptions[i].substring(0,1)).removeAttr('hidden');
				}
				
			
			} else { //if no options are selected
				$('#imbox').dialog( "destroy" );
			}
		} else { //If NONE is selected
			$('#imbox').dialog( "destroy" );
		}
	}
	
	// Return an array of the selected opion values
	// select is an HTML select element
	function getSelectValues(select) {
	  var result = [];
	  var options = select && select.options;
	  var opt;

	  for (var i=0, iLen=options.length; i<iLen; i++) {
	    opt = options[i];

	    if (opt.selected) {
	      result.push(opt.value);// || opt.text);
	    }
	  }
	  return result;
	}
	
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['metricForm']).focus();
    });
</script>