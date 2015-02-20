<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="rangeOfValuesDetail.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
</head>


<div class="span2">
	<h2>
		<fmt:message key='rangeOfValuesDetail.heading' />
	</h2>
	<p>
		<fmt:message key="rangeOfValuesDetail.message" />
	</p>
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
	<form:form commandName="rangeOfValues" method="post"
		action="rangeOfValuesform" id="rangeOfValuesForm"
		cssClass="well form-horizontal">
		<form:hidden path="id" />

		<spring:bind path="rangeOfValues.project">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label" key="rangeOfValues.project" />
		<div class="controls">
			<form:select path="project.id" onchange="" disabled="${true}">
				<form:option value="${rangeOfValues.project.id}"
					label="${rangeOfValues.project.name}" />
			</form:select>
			<form:errors path="project" cssClass="help-inline" />
		</div>

		<spring:bind path="rangeOfValues.name">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label" key="rangeOfValues.name" />
		<div class="controls">
			<form:input path="name" id="name" maxlength="255" readonly="false" />
			<form:errors path="name" cssClass="help-inline" />
		</div>

		<spring:bind path="rangeOfValues.measurementScaleType">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label"
			key="rangeOfValues.measurementScaleType" />
		<div class="controls">
			<form:select path="measurementScaleType" onchange="" disabled="false">
				<form:options items="${availableMeasurementScaleTypes}" />
			</form:select>
			<form:errors path="measurementScaleType" cssClass="help-inline" />
		</div>

		<br>
		<br>
		<appfuse:label styleClass="control-label"
			key="rangeOfValues.defaultRanges" />
		<div class="controls">
			<form:radiobutton id="rangeChoice" path="defaultRange"
				name="rangeChoice" value="0" />
			<form:select id="defaultRangeOfValues" path="rangeValues"
				style="margin-left:10px;" onchange="" disabled="false">
				<form:options items="${defaultRangeSets}" />
			</form:select>
			<form:errors path="rangeValues" cssClass="help-inline" />
		</div>

		<br>
		<appfuse:label styleClass="control-label"
			key="rangeOfValues.customRanges" />
		<div class="controls">
			<form:radiobutton id="rangeChoice" path="defaultRange"
				name="rangeChoice" value="1" />
		</div>

		<div id="customRangeOfValuesDiv">
			<!--  tutto quello che riguarda il custom range of values va qua dentro per poter essere nascoso ad occorrenza -->

			<spring:bind path="rangeOfValues.numeric">
				<div
					class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
			</spring:bind>
			<appfuse:label styleClass="control-label"
				key="rangeOfValues.isNumeric" />
			<div class="controls">
				<form:checkbox path="numeric" name="isNumeric" />
			</div>

			<div id="numericRangeDiv">
				<!-- da far vedere solo se il range of value è un range custom di tipo numerico -->

				<spring:bind path="rangeOfValues.numberType">
					<div
						class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
				</spring:bind>
				<appfuse:label styleClass="control-label"
					key="rangeOfValues.numberType" />
				<div class="controls">
					<form:select id="numberType" path="numberType" onchange=""
						disabled="false">
						<form:options items="${defaultRangeSets}" />
					</form:select>
				</div>

				<spring:bind path="rangeOfValues.range">
					<div
						class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
				</spring:bind>
				<appfuse:label styleClass="control-label"
					key="rangeOfValues.isRange" />
				<div class="controls">
					<form:checkbox path="range" name="isRange" />
				</div>
				<div id="rangeDiv">
					<spring:bind path="rangeOfValues.rangeValues">
						<div
							class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
					</spring:bind>
					<appfuse:label styleClass="control-label"
						key="rangeOfValues.rangeValues" />
					<div class="controls">
						From
						<form:input path="rangeValues" id="name" maxlength="25"
							style="width: 50px;" readonly="false" />
						To
						<form:input path="rangeValues" id="name" maxlength="25"
							style="width: 50px;" readonly="false" />
						<form:errors path="rangeValues" cssClass="help-inline" />
					</div>
				</div>
				<!-- end rangeDiv -->
			</div>
			<div id="rangeListDiv"> <!-- div dove è presente l'input field per inserire i valori manualmente -->
				<spring:bind path="rangeOfValues.rangeValues">
					<div
						class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
				</spring:bind>
				<appfuse:label styleClass="control-label"
					key="rangeOfValues.customValues" />
				<div class="controls">
					<form:input path="rangeValues" data-role="tagsinput" id="valuesE" />
					<form:errors path="rangeValues" cssClass="help-inline" />
				</div>
			</div> <!-- end rangeListDiv -->
		</div>

		<div class="form-actions">
			<c:if test="${true}">
				<button type="submit" class="btn btn-primary" name="save">
					<i class="icon-ok icon-white"></i>
					<fmt:message key="button.save" />
				</button>
				<c:if test="${true}">
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
				hide_elements();

			});

	function hide_elements() {
		$("#customRangeOfValuesDiv").hide();
		$("#numericRangeDiv").hide();
		$("#rangeDiv").hide();
	}

	$("input[name=defaultRange]").change(function() {
		var radioValue = $("input[name=defaultRange]:checked").val();
		if (radioValue == 0) {
			$("#defaultRangeOfValues").attr("disabled", false);
			$("#customRangeOfValuesDiv").hide();
		} else {
			$("#defaultRangeOfValues").attr("disabled", true);
			$("#customRangeOfValuesDiv").show();

		}
	});
	
	$("input[name=numeric]").change(function() {

		if ($(this).is(":checked")) {
			$("#numericRangeDiv").show();
		} else {
			$("#numericRangeDiv").hide();
			if($("input[name=range]").prop('checked'))
				$("input[name=range]").click();
			$("#rangeListDiv").show();
		}
	});
	
	$("input[name=range]").change(function() {

		if ($(this).is(":checked")) {
			$("#rangeDiv").show();
			$("#rangeListDiv").hide();
		} else {
			$("#rangeDiv").hide();
			$("#rangeListDiv").show();
		}
	});
	
	
</script>