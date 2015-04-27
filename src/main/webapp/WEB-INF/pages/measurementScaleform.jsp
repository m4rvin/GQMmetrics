<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="measurementScaleDetail.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
</head>


<div class="span2">
	<h2>
		<fmt:message key='measurementScaleDetail.heading' />
	</h2>
	<p>
		<c:choose>
			<c:when test="${used}">You cannot modify the selected Measurement Scale, it is already attached to a Metric</c:when>
			<c:otherwise>
				<fmt:message key="measurementScaleDetail.message" />
			</c:otherwise>
		</c:choose>
	</p>
	<%-- <p><fmt:message key="metric.goals.message"/></p>		
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
	<form:form commandName="measurementScale" method="post"
		action="measurementScaleform" id="measurementScaleForm"
		cssClass="well form-horizontal">
		<form:hidden path="id" />

		<spring:bind path="measurementScale.project">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.project" />
				<div class="controls">
					<form:select path="project.id" disabled="${used}">
						<form:option value="${measurementScale.project.id}"
							label="${measurementScale.project.name}" />
					</form:select>
					<form:errors path="project" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.name">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.name" />
				<div class="controls">
					<form:input path="name" id="name" maxlength="100"
						readonly="${used}" />
					<form:errors path="name" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.description">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.description" />
				<div class="controls">
					<form:input path="description" id="name" maxlength="1000"
						readonly="${used}" />
					<form:errors path="description" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.type">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.type" />
				<div class="controls">
					<form:select id="measurementScaleType" path="type"
						onchange="getSupportedValues()" disabled="${used}">
						<form:option value="" label="None" />
						<form:options items="${availableMeasurementScaleTypes}" />
					</form:select>
					<form:errors path="type" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.measurementUnit">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.measurementUnit" />
				<div class="controls">
					<form:select path="measurementUnit"
						disabled="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}">
						<form:option value="" label="None" />
						<c:choose>
							<c:when test="${empty measurementScale.measurementUnit.id and not empty measurementScale.measurementUnit.name}">
								<c:forEach items="${units}" var="unit">
								<c:choose>
									<c:when test="${unit.id eq -12}">
										<form:option value="${unit.id}" label="${unit.name}" selected="selected" />
									</c:when>
									<c:otherwise>
										<form:option value="${unit.id}" label="${unit.name}"  />
									</c:otherwise>
								</c:choose>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<form:options items="${units}" itemValue="id" itemLabel="name"/>
							</c:otherwise>
						</c:choose>
					</form:select>
					<form:errors path="measurementUnit" cssClass="help-inline" />
				</div>
			</div>

			<div id="customUnit" style="display:${(empty measurementScale.measurementUnit.id and not empty measurementScale.measurementUnit.name) ? 'block' : 'none'}"
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<div class="controls">
					<form:input path="measurementUnit" id="customUnitInput" placeholder="Custom unit name" readonly="${(simpleMetric.metricOwner ne currentUser && not empty simpleMetric.id) || ( used)}" />
				</div>
				<form:errors path="measurementUnit" cssClass="help-inline" />
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.operations">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.operations" />
				<div class="controls">
					<form:select path="operations" onchange="" disabled="${used}">
						<%-- 	<form:option value="" label="None" /> --%>
						<c:forEach items="${supportedOperations}" var="i">
							<c:set var="found" value="false"></c:set>
							<c:forEach items="${measurementScale.operations}" var="j">
								<c:choose>
									<c:when test="${i.id == j.id}">
										<c:set var="found" value="true"></c:set>
									</c:when>
								</c:choose>
							</c:forEach>
							<c:choose>
								<c:when test="${found}">
									<form:option value="${i.id}" label="${i.operation}"
										selected="selected" />
								</c:when>
								<c:otherwise>
									<form:option value="${i.id}" label="${i.operation}" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
					<form:errors path="operations" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>


		<spring:bind path="measurementScale.rangeOfValues">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.rangeOfValues" />
				<div class="controls">
					<form:select path="rangeOfValues" disabled="${used}">
						<form:option value="" label="None" />
						<form:options items="${supportedRangeOfValues}" itemLabel="name"
							itemValue="id" />
						<!-- 	<c:forEach items="${supportedRangeOfValues}" var="rov">
							<c:choose>
								<c:when test="${measurementScale.rangeOfValues.id == rov.id}">
									<form:option value="${rov.id}" label="${rov.name}"
										selected="selected" />
								</c:when>
								<c:otherwise>
									<form:option value="${rov.id}" label="${rov.name}" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
						 -->
					</form:select>
					<form:errors path="rangeOfValues" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>


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
			</c:if>
			<c:if test="${measurementScale.id ne '0' and not used}">
				<button type="submit" class="btn" name="delete">
					<i class="icon-trash"></i>
					<fmt:message key="button.delete" />
				</button>
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
						document.forms['metricForm']).focus();
			});

	function getSupportedValues() {

		currentLength = document.getElementById('operations').length;

		if (currentLength > 0) //devo rimuovere le opzioni correnti prima di listare quelle nuove
		{
			while (currentLength > 0) {
				$('#operations option:eq(0)').remove();
				currentLength = currentLength - 1;
			}
		}

		var type = $("#measurementScaleType").val();
		if (type != "") //valore non nullo
		{
			$.ajax({
				type : "GET",
				url : "measurementScaleformAjax",
				data : {
					type : type
				},
				contentType : "application/json",
				success : function(response) {
					var JSONOperations = JSON.parse(response);

					$.each(JSONOperations, function() {
						$('#operations').append(
								$("<option></option>").attr("value", this[0])
										.text(this[1]));
					});
				},
				error : function(error) {
					console.log(error);
				}
			});
		}
	}

	$('#measurementUnit').on("change", function() {

		var value = $(this).val();
		console.log(value);
		if (value == "-12") //selected custom unit, need to show a input field
		{
			$('#customUnitInput').val('');
			$('#customUnit').show();
		}
		else
			{
				$('#customUnit').hide();
			}
			
	});
</script>