<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="rangeOfValuesDetail.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
</head>

<style>
.error-messages {
    color: #FF0000;

}
</style>

<div id="info" class="span2">
	<h2>
		<fmt:message key='rangeOfValuesDetail.heading' />
	</h2>
	<c:choose>
		<c:when test="${used}">You cannot modify the selected Range of Values, it is already attached to a measurement scale</c:when>
		<c:otherwise>
			<fmt:message key="rangeOfValuesDetail.message" />
		</c:otherwise>
	</c:choose>
	
	<%-- <p><fmt:message 	key="metric.goals.message"/></p>		
	<p><fmt:message key="metric.owner.message"/></p><b>&nbsp;&nbsp;&nbsp;${metric.metricOwner.fullName}</b>
	
	<p><fmt:message key="questionMetric.question.message"/></p>	
	<ul>
		<c:forEach var="m" items="${metric.questions}">
			<li><a href="/questionmetricform?questionId=${m.question.id}&metricId=${m.metric.id}">${m.question.name} (${m.status})</a></li>			
		</c:forEach>		    	
	</ul>
	--%>
</div>
<div class="span7">
	<form:errors path="*" cssClass="alert alert-error fade in"
		element="div" />
	<form:form commandName="rangeOfValues" method="post" action="rangeOfValuesform" id="rangeOfValuesForm" cssClass="well form-horizontal">
		<form:hidden path="id" />

		<spring:bind path="rangeOfValues.project">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="rangeOfValues.project" />
				<div class="controls">
					<form:select path="project.id" onchange="" disabled="${used}">
						<form:option value="${rangeOfValues.project.id}"
							label="${rangeOfValues.project.name}" />
					</form:select>
					<form:errors path="project" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="rangeOfValues.name">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="rangeOfValues.name" />
				<div class="controls">
					<form:input path="name" id="name" maxlength="255" readonly="${used}" />
					<form:errors path="name" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		
		<spring:bind path="rangeOfValues.measurementScaleType">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="rangeOfValues.measurementScaleType" />
				<div class="controls">
					<form:select path="measurementScaleType" onchange="" disabled="${used}">
						<form:option value="" label="None" />
						<form:options items="${availableMeasurementScaleTypes}" />
					</form:select>
					<form:errors path="measurementScaleType" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<div class="control-group">
			<appfuse:label styleClass="control-label" key="rangeOfValues.rangeType" />
			<div class="controls">
				<form:select  path="defaultRange" onchange="selectTypes()" disabled="${used}">
					<form:option value="true" label="Default" />
					<form:option value="false" label="Custom" />
				</form:select>
			</div>
		</div>
		<div id="defaultRangeDiv">
			<spring:bind path="rangeOfValues.numberType">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
					<appfuse:label styleClass="control-label" key="rangeOfValues.rangeValues" />
					<div class="controls">
						<form:select id="defaultRangeOfValues" path="numberType" disabled="${used}">
							<form:option value="" label="None" />
							<form:options items="${defaultRangeSets}" />
						</form:select>
						<form:errors path="numberType" cssClass="help-inline" />
					</div>
				</div>
			</spring:bind>
		</div>

		<div id="customRangeOfValuesDiv">
			<!--  tutto quello che riguarda il custom range of values va qua dentro per poter essere nascoso ad occorrenza -->

			    <!-- Numeric checkbox -->
				<div class="control-group">
					<appfuse:label styleClass="control-label" key="rangeOfValues.isNumeric" />
					<div class="controls">
						<form:checkbox path="numeric" name="isNumeric" onchange="enableNumericRange()" disabled="${used}" />
					</div>
				</div>
			
			<div id="numericRangeDiv">
			<!-- da far vedere solo se il range of value è un range custom di tipo numerico -->
			
				<spring:bind path="rangeOfValues.numberType">
					<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
						<appfuse:label styleClass="control-label" key="rangeOfValues.numberType" />
						<div class="controls">
							<form:select id="numberType" path="numberType" onchange="" disabled="${used}">
								<form:option value="" label="None" />
								<form:options items="${defaultRangeSets}" />
							</form:select>
							<form:errors path="numberType" cssClass="help-inline" />
						</div>
						
					</div>
				</spring:bind>
				
				<div class="control-group">
					<appfuse:label styleClass="control-label" key="rangeOfValues.isRange" />
					<div class="controls">
						<form:checkbox path="range" name="isRange" disabled="${used}" />
					</div>
				</div>
			
			
				 
			</div>
			
			<div id="rangeListDiv">
				
					<spring:bind path="rangeOfValues.rangeValues">
						<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							<appfuse:label styleClass="control-label" key="rangeOfValues.customValues" />
							<div class="controls">
								<form:input id="customValues" path="rangeValues" data-role="tagsinput" disabled="${used}" />
								<a onclick="showInstructions()">Instructions</a>
								<form:errors path="rangeValues" cssClass="help-inline" />
							</div>
						</div>
					</spring:bind>
					
				</div>
			
		</div>
		<div class="error-messages">
		<c:if test="${not empty duplicate_value}">
			<c:out value="${duplicate_value}"></c:out>
		</c:if>
		</div>

		<div class="form-actions">
			<c:if test="${not used}">
				<button type="submit" class="btn btn-primary" name="save">
					<i class="icon-ok icon-white"></i>
					<fmt:message key="button.save" />
				</button>
				<c:if test="${not used}">
					<%-- always checked- TODO remove if tags --%>
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
	$(document).ready(
			function() {
				$("input[type='text']:visible:enabled:first",
						document.forms['rangeOfValuesForm']).focus();
				selectTypes();
				enableNumericRange();
			});

	function selectTypes()
	{
		if($('#defaultRange').val() == 'true')
		{
			document.getElementById('numeric1').checked = true;
			document.getElementById('numberType').value = '';
			document.getElementById('range1').checked = false;
			$('#customValues').tagsinput('removeAll');
			
			$('#customRangeOfValuesDiv').hide();
			$('#defaultRangeDiv').show();
			
		}															
		else
		{
			document.getElementById('defaultRangeOfValues').value = '';
		//	document.getElementById('defaultRangeOfValues').readOnly = true;
			$('#defaultRangeDiv').hide();
			enableNumericRange();
			$('#customRangeOfValuesDiv').show();
		}
	}
	
	function enableNumericRange()
	{
		if($('#numeric1').is(':checked'))
		{		
			$('#numericRangeDiv').show();
			
		}
		else
		{
			document.getElementById('numberType').value = '';
			document.getElementById('range1').checked = false;
			
			$('#numericRangeDiv').hide();
		}
	}
	

	
	
	function showInstructions(){
		
		if($('#range1').is(':checked'))
		{
			jQuery('<div/>', {
			    id: 'dialogInstructions',
			    title:"instructions",
			    text: "To specify a valid range you have to use the following syntax: [start_value:end_value], where 'start_value' and 'end_value' must be consistent with the number type"
			}).dialog();
		}
		else
		{
			jQuery('<div/>', {
			    id: 'dialogInstructions',
			    title:"instructions",
			    text: "Insert elements one by one separating them by pressing Enter or inserting a comma (,) "
			}).dialog();
		}
	}
	
	

	
</script>