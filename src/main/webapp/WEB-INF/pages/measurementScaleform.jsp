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
		<fmt:message key="measurementScaleDetail.message" />
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
					<form:select path="project.id" disabled="false">
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
					<form:input path="name" id="name" maxlength="255" readonly="false" />
					<form:errors path="name" cssClass="help-inline" />
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
						onchange="getSupportedValues()" disabled="false">
						<form:option value="" label="None" />
						<form:options items="${availableMeasurementScaleTypes}" />
					</form:select>
					<form:errors path="type" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.rangeOfValues">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.rangeOfValues" />
				<div class="controls">
					<form:select path="rangeOfValues" onchange="" disabled="false">
						<form:option value="" label="None" />
						<c:forEach items="${supportedRangeOfValues}" var="rov">
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
					</form:select>
					<form:errors path="rangeOfValues" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<spring:bind path="measurementScale.operations">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label"
					key="measurementScale.operations" />
				<div class="controls">
					<form:select path="operations" onchange="" disabled="false">
						<%-- 					<form:option value="" label="None" /> --%>
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
									<form:option value="${i.id}"
										label="${i.operation}" selected="selected" />
								</c:when>
								<c:otherwise>
									<form:option value="${i.id}"
										label="${i.operation}" />
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</form:select>
					<form:errors path="operations" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>


		<div class="form-actions">
			<c:if test="${metric.metricOwner eq currentUser || empty metric.id}">
				<button type="submit" class="btn btn-primary" name="save">
					<i class="icon-ok icon-white"></i>
					<fmt:message key="button.save" />
				</button>
				<c:if test="${metric.metricOwner eq currentUser}">
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
						document.forms['metricForm']).focus();
			});

	function getSupportedValues() {
		var currentLength = document.getElementById('rangeOfValues').length;
		if (currentLength > 1) //devo rimuovere le opzioni correnti prima di listare quelle nuove
		{
			while (currentLength > 1) {
				$('#rangeOfValues option:eq(1)').remove();
				currentLength = currentLength - 1;
			}
		}
		currentLength = document.getElementById('operations').length;

		if (currentLength > 1) //devo rimuovere le opzioni correnti prima di listare quelle nuove
		{
			while (currentLength > 1) {
				$('#operations option:eq(1)').remove();
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
					var JSONResponse = JSON.parse(response);

					var JSONRangeOfValues = JSONResponse["rangeOfValues"];
					$.each(JSONRangeOfValues, function() {
						$('#rangeOfValues').append(
								$("<option></option>").attr("value", this[0])
										.text(this[1]));
					});

					var JSONOperations = JSONResponse["operation"];
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
</script>