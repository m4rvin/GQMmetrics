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
 <p><fmt:message key="metric.owner.message"/></p><b>&nbsp;&nbsp;&nbsp;${combinedMetric.metricOwner.fullName}</b>
	
	<p><fmt:message key="questionMetric.question.message"/></p>	
	<ul>
		<c:forEach var="m" items="${combinedMetric.questions}">
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
		<div>Exponentiation = <fmt:message key="exponentiation"/></div>
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
		<div>Ratio = <fmt:message key="ratio"/></div>
		<div>Sine = <fmt:message key="sine"/></div>
		<div>Square root = <fmt:message key="square_root"/></div>
		<div>Subtraction = <fmt:message key="subtraction"/></div>
		<div>Tangent = <fmt:message key="tangent"/></div>
	</div>
	<br>
	<div style="color:#0000FF;">Where <b>y</b> refers to a valid value or to an existing metric.</div>	
</div>


<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="combinedMetric" method="post" action="combinedmetricform" id="combinedMetricForm" cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="combinedMetric.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange=""  disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}">
					<form:option value="${combinedMetric.project.id}" label="${combinedMetric.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>
    <spring:bind path="combinedMetric.code">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.code"/>
        <div class="controls">
            <form:input path="code" id="code" maxlength="50" readonly="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}"/>
            <form:errors path="code" cssClass="help-inline"/>        
        </div>
    </div>
             
    <spring:bind path="combinedMetric.name">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.name"/>
        <div class="controls">
            <form:input path="name" id="name" maxlength="255" readonly="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}"/>
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>	    
    
    <spring:bind path="combinedMetric.type">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.type"/>
        <div class="controls">
   			<form:select path="type" multiple="false" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}">
   				<form:option value="" label="None"/>
		    	<form:options items="${availablesTypes}"/>
		    </form:select>		
            <form:errors path="type" cssClass="help-inline"/>
        </div>
    </div> 
    
    <spring:bind path="combinedMetric.measurementScale">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.measurementScale"/>
        <div class="controls">
   			<form:select path="measurementScale" onchange="loadAvailableMetricComposers();" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}">
   			   	<form:option value="" label="None"/>
		    	<form:options items="${measurementScales}" itemValue="id" itemLabel="name"/>
		    </form:select>		
            <form:errors path="measurementScale" cssClass="help-inline"/>
        </div>
    </div>  
  
    <spring:bind path="combinedMetric.hypothesis">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.hypothesis"/>
        <div class="controls">
            <form:input path="hypothesis" id="hypothesis" maxlength="255" readonly="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}"/>
            <form:errors path="hypothesis" cssClass="help-inline"/>
        </div>
    </div>
    
    <spring:bind path="combinedMetric.collectingType">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.collectingType"/>
        <div class="controls">
			<form:select path="collectingType" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}" >
				<form:option value="SINGLE_VALUE" label="Single Value"/>
		    </form:select>		
            <form:errors path="collectingType" cssClass="help-inline"/>            
        </div>
    </div>

    <spring:bind path="combinedMetric.satisfyingConditionOperation">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.satisfyingConditionOperation"/>
        <div class="controls">
			<form:select path="satisfyingConditionOperation" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}" >
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

    <spring:bind path="combinedMetric.satisfyingConditionValue">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.satisfyingConditionValue"/>
        <div class="controls">
            <form:input path="satisfyingConditionValue" id="satisfyingConditionValue"  readonly="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}"/>
            <form:errors path="satisfyingConditionValue" cssClass="help-inline"/>
        </div>
    </div>
   <!--  
    <spring:bind path="combinedMetric.outputValueType">
    	<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
        	<appfuse:label styleClass="control-label" key="metric.outputValueType"/>
       		<div class="controls">
           		<form:select path="outputValueType" disabled="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}" >
           			<form:option value="" label="None" />
           			<form:option value="NUMERIC" label="NUMERIC" />
           			<form:option value="BOOLEAN" label="BOOLEAN" />
          			</form:select>
           		<form:errors path="outputValueType" cssClass="help-inline"/><br>          
       		</div>
   		</div>
    </spring:bind>
     -->
    <spring:bind path="combinedMetric.formula">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.formula"/>
        <div class="controls">
            <form:input path="formula" id="Metricformula"  readonly="${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)}"/>
            <form:errors path="formula" cssClass="help-inline"/><br>
            <a onclick="showFormulaInputInstructions()">Instructions</a>
            
        </div>
    </div>
    
         
         <div class="control-group">
        <appfuse:label styleClass="control-label" key="metric.availableMetricComposers"/>
        <div class="controls">
			<select id="availableMetricComposersList" multiple onclick="putSelectedMetricIntoFormulaField()" ${(combinedMetric.metricOwner ne currentUser && not empty combinedMetric.id) || ( used)} ? 'disabled' : '' >
					<c:forEach items="${availableMetricComposers}" var="availablecomposer">
						<c:set var="found" value="false"></c:set>
							<c:forEach items="${combinedMetric.composedBy}" var="savedcomposer">
								<c:choose>
									<c:when test="${availablecomposer == savedcomposer.name}">
										<c:set var="found" value="true"></c:set>
									</c:when>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${found}">
									<option value="${availablecomposer.name}"
										label="${availablecomposer.name}" selected="selected" />
								</c:when>
								<c:otherwise>
									<option value="${availablecomposer.name}"
										label="${availablecomposer.name}" />
								</c:otherwise>
							</c:choose>
					</c:forEach>
		    </select>		
        </div>
        </div>
            
	<c:if test="${not empty combinedMetric.id}">
		<spring:bind path="combinedMetric.questions">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    <appfuse:label styleClass="control-label" key="metric.questions"/>
	        	<div class="controls">
	        		<select id="questions" name="questions" multiple="multiple"  style="width:500px;" onchange="showIMBox()">
						<option value="">-- None --</option>
						<c:forEach var="item" items="${availableQuestions}">
							<c:set var="itemSelected" value="false" />
							<c:forEach var="item2" items="${combinedMetric.questions}">	
								<c:choose>							
									<c:when test="${item2.question eq item}">
										<c:set var="itemSelected" value="true" />
									</c:when>
								</c:choose>							
							</c:forEach>|
							<c:choose>
								<c:when test="${itemSelected eq true}">
									<option value="${item.id}|${combinedMetric.id}" selected="selected">${item.text}</option>
								</c:when>
								<c:otherwise>
									<option value="${item.id}|${combinedMetric.id}">${item.text}</option>
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
<%--         <c:if test="${combinedMetric.metricOwner eq currentUser || empty combinedMetric.id}">
			<button type="submit" class="btn btn-primary" name="save">
			    <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
			</button>
			<c:if test="${combinedMetric.metricOwner eq currentUser}">	
				 <button type="submit" class="btn" name="delete">
				     <i class="icon-trash"></i> <fmt:message key="button.delete"/>
				</button>        
			</c:if>
        </c:if>
      <button type="submit" class="btn" name="cancel">
      	<i class="icon-remove"></i> <fmt:message key="button.cancel"/>
      </button>  --%>  
      
      <c:if test="${(combinedMetric.metricOwner eq currentUser || empty combinedMetric.id) && (not used)}">
			<button type="submit" class="btn btn-primary" name="save">
				<i class="icon-ok icon-white"></i>
				<fmt:message key="button.save" />
			</button>
				<c:if test="${(combinedMetric.metricOwner eq currentUser)&& (not used)}">	
					<button type="submit" class="btn" name="delete">
						<i class="icon-trash"></i>
						<fmt:message key="button.delete" />
					</button>
				</c:if>
	</c:if>
	<button type="submit" class="btn" name="cancel">
		<i class="icon-remove"></i>
		<fmt:message key="button.cancel" />
	</button>     
    </div>
    </form:form>
</div>
<script type="text/javascript">

	$(document).ready(function() {
	    $("input[type='text']:visible:enabled:first", document.forms['combinedMetricForm']).focus();
	});
	

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
	
	// Return an array of the selected option values
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
	
    
    
    function loadAvailableMetricComposers(){
    	$('#Metricformula').val("");
    	var currentLength = document.getElementById('availableMetricComposersList').length;
		if (currentLength > 0) //devo rimuovere le opzioni correnti prima di listare quelle nuove
		{
			while (currentLength > 0) {
				$('#availableMetricComposersList option:eq(0)').remove();
				currentLength = currentLength - 1;
			}
		}

		var msrmntScaleId = $("#measurementScale").val();
		var metricId = GetURLParameter("id");
		if (msrmntScaleId != "") //valore non nullo
		{
			$.ajax({
				type : "GET",
				url : "combinedmetricformAjax",
				data : {
					measurementScaleId : msrmntScaleId,
					metricToExcludeId : metricId
				},
				contentType : "application/json",
				success : function(response) {
					var JSONMetricComposerNames = JSON.parse(response);

					$.each(JSONMetricComposerNames, function() {
						$('#availableMetricComposersList').append(
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
    
    // Retrieve the value associated to the selected metric ant put it into the formula text area field
    function putSelectedMetricIntoFormulaField(){
		var selectBox = document.getElementById("availableMetricComposersList");
		var selectedValue = selectBox.options[selectBox.selectedIndex].value;

		var selectBox = document.getElementById("availableMetricComposersList");
		var formulaInputArea =  document.getElementById("Metricformula");
		var formulaValue= formulaInputArea.value;
		formulaValue += "_" + selectBox.value + "_";
		formulaInputArea.value = formulaValue;
		$('#Metricformula').focus();
    }
    
	function showFormulaInputInstructions(){
		jQuery('<div/>', {
		    id: 'dialogInstructions',
		    title:"Formula input instructions",
		    text: "Insert your formula operations as a text.Add an existing metric to the formula by clicking on it on the list of available metrics displayed below. Refer to the current measured value as _this_"
		}).dialog();
	}
	
	// get param from the URL
	function GetURLParameter(sParam)
	{
	    var sPageURL = window.location.search.substring(1);
	    var sURLVariables = sPageURL.split('&');
	    for (var i = 0; i < sURLVariables.length; i++)
	    {
	        var sParameterName = sURLVariables[i].split('=');
	        if (sParameterName[0] == sParam)
	        {
	            return sParameterName[1];
	        }
	    }
	    return null;
	}
</script>