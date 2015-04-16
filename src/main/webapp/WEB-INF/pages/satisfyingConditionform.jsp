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
			<c:when test="${used}">You cannot modify the selected Measurement Scale, it is already attached to a Metric</c:when>
			<c:otherwise>
				<fmt:message key="satisfyingConditionDetail.message" />
			</c:otherwise>
		</c:choose>
	</p>

</div>
<div class="span7">
	<form:errors path="*" cssClass="alert alert-error fade in"
		element="div" />
	<form:form commandName="satisfyingCondition" method="post" action="satisfyingConditionform" id="satisfyingConditionForm" cssClass="well form-horizontal">
		<form:hidden path="id" />
		
		<spring:bind path="satisfyingCondition.project">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
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
				<select id="metricSelectBox" onchange="retriveTargetsAndOperations">
				<c:forEach items="${availableMetrics}" var="metric">
					<option label="${metric.name}" value="${metric.id}">
				</c:forEach>
				</select>
			</div>
		</div>

		

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

</script>