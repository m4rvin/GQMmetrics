<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="metricDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 

<div class="span2">
    <h2><fmt:message key='metricDetail.heading'/></h2>
    <p><fmt:message key="metricDetail.message"/></p>	
<%-- 	
	<p><fmt:message key="metric.goals.message"/></p>		
 --%>	
 <p><fmt:message key="metric.owner.message"/></p><b>&nbsp;&nbsp;&nbsp;${simpleMetric.metricOwner.fullName}</b>
	
	<p><fmt:message key="questionMetric.question.message"/></p>	
	<ul>
		<c:forEach var="m" items="${simpleMetric.questions}">
			<li><a href="/questionmetricform?questionId=${m.question.id}&metricId=${m.metric.id}">${m.question.name} (${m.status})</a></li>			
		</c:forEach>		    	
	</ul>
	
	<p style="text-align:justify;text-justify:auto;"><fmt:message key="metric.syntax"/></p>
	<br>
	<div style="font-style:italic;">
		<div>Addition = <fmt:message key="addition"/></div>
		<div>And = <fmt:message key="and"/></div>
		<div>Arc cosine = <fmt:message key="arccosine"/></div>
		<div>Arc sine = <fmt:message key="arcsine"/></div>
		<div>Arc tangent = <fmt:message key="arctangent"/></div>
		<div>Base 2 logarithm = <fmt:message key="base2logarithm"/></div>
		<div>Base 10 logarithm = <fmt:message key="base10logarithm"/></div>
		<div>Cubic root = <fmt:message key="cbrt"/></div>
		<div>Cosine = <fmt:message key="cosine"/></div>
		<div>Equality = <fmt:message key="equality"/></div>
		<div>Euler's number power = <fmt:message key="euler's_number_power"/></div>
		<div>Greater than = <fmt:message key="greater_than"/></div>
		<div>Greater or equal than = <fmt:message key="greater_equal_than"/></div>
		<div>Hyperbolic cosine = <fmt:message key="hyperbolic_cosine"/></div>
		<div>Hyperbolic sine = <fmt:message key="hyperbolic_sine"/></div>
		<div>Hyperbolic tangent = <fmt:message key="hyperbolic_tangent"/></div>
		<div>Lower than = <fmt:message key="lower_than"/></div>
		<div>Lower or equal than = <fmt:message key="lower_equal_than"/></div>
		<div>Membership = <fmt:message key="membership"/></div>
		<div>Multiplication= <fmt:message key="multiplication"/></div>
		<div>Modulo = <fmt:message key="modulo"/></div>
		<div>Natural logarithm = <fmt:message key="natural_logarithm"/></div>
		<div>Nearest lower integer = <fmt:message key="nearest_lower_integer"/></div>
		<div>Nearest upper integer = <fmt:message key="nearest_upper_integer"/></div>
		<div>Or = <fmt:message key="or"/></div>
		<div>Power = <fmt:message key="power" /></div>
		<div>Ratio = <fmt:message key="ratio"/></div>
		<div>Sine = <fmt:message key="sine"/></div>
		<div>Square root = <fmt:message key="square_root"/></div>
		<div>Subtraction = <fmt:message key="subtraction"/></div>
		<div>Tangent = <fmt:message key="tangent"/></div>
	</div>
	<br>
	<div style="color:#0000FF;">Where <b>y</b> refers to a valid value.</div>	
</div>


<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="simpleMetric" method="post" action="simplemetricform" id="simpleMetricForm" cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="simpleMetric.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange=""  disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}">
					<form:option value="${simpleMetric.project.id}" label="${simpleMetric.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>
    <spring:bind path="simpleMetric.code">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.code"/>
        <div class="controls">
            <form:input path="code" id="code" maxlength="50" readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}"/>
            <form:errors path="code" cssClass="help-inline"/>        
        </div>
    </div>
             
    <spring:bind path="simpleMetric.name">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.name"/>
        <div class="controls">
            <form:input path="name" id="name" maxlength="255" placeholder="{a-z, A-Z, 1-9}is the allowed set of characters(no punctuation/other symbols)" readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}"/>
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>	    
    
    <spring:bind path="simpleMetric.type">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.type"/>
        <div class="controls">
   			<form:select path="type" multiple="false" disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}">
   				<form:option value="" label="None"/>
		    	<form:options items="${availablesTypes}"/>
		    </form:select>		
            <form:errors path="type" cssClass="help-inline"/>
        </div>
    </div> 
    
    <spring:bind path="simpleMetric.measurementScale">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.measurementScale"/>
        <div class="controls">
   			<form:select path="measurementScale"  disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}" onclick="retriveAggregators()">
   			   	<form:option value="" label="None"/>
		    	<form:options items="${measurementScales}" itemValue="id" itemLabel="name"/>
		    </form:select>		
            <form:errors path="measurementScale" cssClass="help-inline"/>
        </div>
    </div>  

   
            
    <spring:bind path="simpleMetric.hypothesis">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.hypothesis"/>
        <div class="controls">
            <form:input path="hypothesis" id="hypothesis" maxlength="255" readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}"/>
            <form:errors path="hypothesis" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="simpleMetric.collectingType">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.collectingType"/>
        <div class="controls">
			<form:select path="collectingType" onchange="showAggregator(); retriveAggregators()" disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}" >
				<form:option value="SINGLE_VALUE" label="Single Value"/>
				<form:option value="MULTIPLE_VALUE" label="Multiple Value"/>
		    </form:select>		
            <form:errors path="collectingType" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="simpleMetric.satisfyingConditionOperation">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.satisfyingCondishowtionOperation"/>
        <div class="controls">
			<form:select path="satisfyingConditionOperation" disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}" >
				<form:option value="NONE" label="None"/>
				<form:option value="LESS" label="<"/>
				<form:option value="LESS_OR_EQUAL" label="<="/>
				<form:option value="EQUAL" label="="/>
				<form:option value="GREATER_OR_EQUAL" label=">="/>
				<form:option value="GREATER" label=">"/>
		    </form:select>		
            <form:errors path="satisfyingConditionOperation" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="simpleMetric.satisfyingConditionValue">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        <appfuse:label styleClass="control-label" key="metric.satisfyingConditionValue"/>
        <div class="controls">
            <form:input path="satisfyingConditionValue" id="satisfyingConditionValue"  readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}"/>
            <form:errors path="satisfyingConditionValue" cssClass="help-inline"/>
        </div>
    </div>
    </spring:bind>
    <!-- 
    <spring:bind path="simpleMetric.outputValueType">
    	<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        	<appfuse:label styleClass="control-label" key="metric.outputValueType"/>
       		<div class="controls">
           		<form:select path="outputValueType" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}" >
           			<form:option value="NUMERIC" label="NUMERIC" />
           			<form:option value="BOOLEAN" label="BOOLEAN" />
          			</form:select>
           		<form:errors path="outputValueType" cssClass="help-inline"/><br>          
       		</div>
   		</div>
    </spring:bind>
     -->
    <spring:bind path="simpleMetric.formula">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.formula"/>
        <div class="controls">
            <form:input path="formula" id="Metricformula"  readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}"/>
           <form:errors path="formula" cssClass="help-inline"/><br>
            <a onclick="showFormulaInputInstructions()">Instructions</a>
            
        </div>
    </div>
    <div id="aggregatorDiv" style="display:none;">
	    <spring:bind path="simpleMetric.aggregator">
		    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
		        <appfuse:label styleClass="control-label" key="metric.aggregator"/>
		        <div class="controls">
					<form:select path="aggregator" disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || (used)}" >
					<form:option label="None" value="" />
		            <c:forEach items="${availableAggregators}" var="availableAggregator">
		            	<c:choose>
		            		<c:when test="${availableAggregator == simpleMetric.aggregator}">
		            			<option value="${availableAggregator}" label="${availableAggregator}" selected="selected" />
		            		</c:when>
		            		<c:otherwise>
		            			<option value="${availableAggregator}" label="${availableAggregator}" />
		            		</c:otherwise>
		            	</c:choose>
		            </c:forEach>
		            </form:select>
		            <form:errors path="aggregator" cssClass="help-inline"/><br>
		        </div>
		    </div>
	    </spring:bind>
    </div>
            
	<c:if test="${not empty simpleMetric.id}">
		<spring:bind path="simpleMetric.questions">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    <appfuse:label styleClass="control-label" key="metric.questions"/>
	        	<div class="controls">
	        		<select id="questions" name="questions" multiple="multiple"  style="width:500px;" onchange="showIMBox()">
						<option value="">-- None --</option>
						<c:forEach var="item" items="${availableQuestions}">
							<c:set var="itemSelected" value="false" />
							<c:forEach var="item2" items="${simpleMetric.questions}">	
								<c:choose>							
									<c:when test="${item2.question eq item}">
										<c:set var="itemSelected" value="true" />
									</c:when>
								</c:choose>							
							</c:forEach>|
							<c:choose>
								<c:when test="${itemSelected eq true}">
									<option value="${item.id}|${simpleMetric.id}" selected="selected">${item.text}</option>
								</c:when>
								<c:otherwise>
									<option value="${item.id}|${simpleMetric.id}">${item.text}</option>
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
       
       
    <div class="error-messages">
		<c:if test="${not empty duplicate_value}">
			<c:out value="${duplicate_value}"></c:out>
		</c:if>
	</div>
       
    <div class="form-actions">
    <%--     <c:if test="${simpleMetric.metricOwner eq currentUser || empty simpleMetric.id}">
			<button type="submit" class="btn btn-primary" name="save">
			    <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
			</button>
			<c:if test="${simpleMetric.metricOwner eq currentUser}">	
				 <button type="submit" class="btn" name="delete">
				     <i class="icon-trash"></i> <fmt:message key="button.delete"/>
				</button>        
			</c:if>
        </c:if> --%>
        
         <c:if test="${(simpleMetric.metricOwner eq currentUser || empty simpleMetric.id)}">
			<button type="submit" class="btn btn-primary" name="save">
				<i class="icon-ok icon-white"></i>
				<fmt:message key="button.save" />
			</button>
				<c:if test="${(simpleMetric.metricOwner eq currentUser)&& (not used)}">	
					<button type="submit" class="btn" name="delete">
						<i class="icon-trash"></i>
						<fmt:message key="button.delete" />
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
        $("input[type='text']:visible:enabled:first", document.forms['simpleMetricForm']).focus();
        showAggregator();
    });
    
    function showFormulaInputInstructions(){
		jQuery('<div/>', {
		    id: 'dialogInstructions',
		    title:"Formula syntax instructions",
		    text: "Insert your formula as a plain text. Use the syntax written in the left column to refer to one or more operations. Refer to the current measured value as _this_\n"
		    }).dialog();
	}
    
    function showAggregator() { 	
    	if($('#collectingType').val() === "MULTIPLE_VALUE")
    		$('#aggregatorDiv').show();
    	else
    		$('#aggregatorDiv').hide();
    }
    
    function retriveAggregators()
    {
    	if($('#aggregatorDiv').css('display') == 'block') //aggregator div visible
    	{
    		$.each($('#aggregator option'), function() {
    				if(this.value !== "")
    					this.remove();
    		});    		
    		var msrmntScaleId = $("#measurementScale").val();
    		
    		if (msrmntScaleId != "") //valore non nullo
    		{
    			$.ajax({
    				type : "GET",
    				url : "simplemetricformAjax",
    				data : {
    					measurementScaleId : msrmntScaleId,
    				},
    				contentType : "application/json",
    				success : function(response) {
    					var JSONAggregatorNames = JSON.parse(response);
    					console.log(JSONAggregatorNames);

    					$.each(JSONAggregatorNames, function() {
    						$('#aggregator').append(
    								$("<option></option>").attr("value", this)
    										.text(this));
    					}); 
    				},
    				error : function(error) {
    					console.log(error);
    				}
    			});
    		}

    	}
    }
    
    
</script>