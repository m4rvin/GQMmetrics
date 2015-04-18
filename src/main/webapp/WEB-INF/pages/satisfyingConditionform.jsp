<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="satisfyingConditionDetail.title" /></title>
<meta name="menu" content="IntepretationPhaseMenu" />
</head>


<div class="span2">
	<h2>
		<fmt:message key='satisfyingConditionDetail.heading' />
	</h2>
	<p>
		<c:choose>
			<c:when test="${empty not satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}">You cannot modify the selected Satisfying condition, you are not the owner</c:when>
			<c:otherwise>
				<fmt:message key="satisfyingConditionDetail.message" />
			</c:otherwise>
		</c:choose>
	</p>
</div>

<div class="span7">
	<form:errors path="*" cssClass="alert alert-error fade in" 	element="div" />
	<form:form commandName="satisfyingCondition" method="post" action="satisfyingConditionform" id="satisfyingConditionForm" cssClass="well form-horizontal">
		<form:hidden path="id" />
		
		<spring:bind path="satisfyingCondition.project">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.project" />
				<div class="controls">
					<form:select path="project.id" disabled="${used}">
						<form:option value="${satisfyingCondition.project.id}" 	label="${satisfyingCondition.project.name}" />
					</form:select>
					<form:errors path="project" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<div class="control-group">
			<appfuse:label styleClass="control-label" key="satisfyingCondition.metric" />
			<div class="controls">
				<select id="metricSelectBox" name="metric" onchange="retriveTargetsAndOperations()">
				<option value="" label="None" />
				<c:forEach items="${availableMetrics}" var="metric">
					<c:choose>
						<c:when test="${metric.id eq selectedMetric}">
							<option label="${metric.name}" value="${metric.id}" selected="selected" />
						</c:when>
						<c:otherwise>
							<option label="${metric.name}" value="${metric.id}" />
						</c:otherwise>
					</c:choose>
				</c:forEach>
				</select>
			</div>
		</div>

		<spring:bind path="satisfyingCondition.targets">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.targets" />
				<div class="controls">
					<form:select path="targets" disabled="${used}">
						<c:forEach items="${targets}" var="target">
							<form:option value="${target.representation}"></form:option>
						
						</c:forEach>
						<form:options items="${targets}"/>
					</form:select>
					<form:errors path="targets" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
<!--  
		<spring:bind path="satisfyingCondition.targets">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.targets" />
				<div class="controls">
					<form:select path="targets">
						<c:forEach items="${availableTargets}" var="target">
							<c:set var="found" value="false" />
							<c:forEach items="${satisfyingCondition.targets}" var="chosedTarget">
								<c:if test="${target == chosedTarget.representation}">
									<c:set var="found" value="true" />
								</c:if>
								<script type="text/javascript">
							console.log("target: ${target}");
							console.log("chosedTarget: ${chosedTarget.representation}");
							console.log("foound: ${found}");
							</script>
							</c:forEach>
							<c:choose>
								<c:when test="${found}">
									<form:option label="${chosedTarget.representation}" value="${chosedTarget.representation}" />
									<script type="text/javascript">
									console.log("found");
									</script>
								</c:when>
								<c:otherwise>
									<form:option label="${chosedTarget.representation}" value="${chosedTarget.representation}" />
								</c:otherwise>
							</c:choose>			
						</c:forEach>
					</form:select>
					<form:errors path="targets" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		-->
		
		<spring:bind path="satisfyingCondition.satisfyingConditionOperation">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.satisfyingConditionOperation" />
				<div class="controls">
					<form:select path="satisfyingConditionOperation" disabled="${used}">
						<form:option value="" label="None" />
						<form:options items="${satisfyingOperations}"/>
					</form:select>
					<form:errors path="satisfyingConditionOperation" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		
		<spring:bind path="satisfyingCondition.satisfyingConditionValue">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.satisfyingConditionValue" />
				<div class="controls">
					<form:input path="satisfyingConditionValue" disabled="${used}" />
					<form:errors path="satisfyingConditionValue" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		
		<spring:bind path="satisfyingCondition.hypotesis">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.hypotesis" />
				<div class="controls">
					<form:input path="hypotesis" disabled="${used}" />
					<form:errors path="hypotesis" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		
		

		

		<div class="form-actions">
			<c:if test="${not used}">
				<button type="submit" class="btn btn-primary" name="save">
					<i class="icon-ok icon-white"></i>
					<fmt:message key="button.save" />
				</button>
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
						document.forms['satisfyingConditionform']).focus();
			});
	
	function retriveTargetsAndOperations()
	{
		// remove old targets when select a new metric
		$.each($('#targets option'), function() {
			this.remove();
		});
		
		$.each($('#satisfyingConditionOperation option'), function() {
			if(this.value !== "")
				this.remove();
		});
		
		var metric_id = $('#metricSelectBox').val();
		console.log(metric_id);
		if(metric_id !== "")
		{
			$.ajax({
				type : "GET",
				url : "satisfyingConditionformAjax",
				data : {
					metricId : metric_id,
				},
				contentType : "application/json",
				success : function(response) {
					var JSONResponse = JSON.parse(response);
					
					var targetsArray = JSONResponse['targets'];
					var operationsArray = JSONResponse['satisfyingOperations']
					
					$.each(targetsArray, function() {
						$('#targets').append(
								$("<option></option>").attr("value", this)
										.text(this));
					});
					$.each(operationsArray, function() {
						$('#satisfyingConditionOperation').append(
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

</script>