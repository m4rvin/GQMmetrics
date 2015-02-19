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
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label"
			key="measurementScale.project" />
		<div class="controls">
			<form:select path="project.id" onchange=""
				disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
				<form:option value="${measurementScale.project.id}"
					label="${measurementScale.project.name}" />
			</form:select>
			<form:errors path="project" cssClass="help-inline" />
		</div>

		<%--   <spring:bind path="metric.code">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="metric.code"/>
        <div class="controls">
            <form:input path="code" id="code" maxlength="50" readonly="${metric.metricOwner ne currentUser && not empty metric.id}"/>
            <form:errors path="code" cssClass="help-inline"/>        
        </div>
    </div>
   --%>
		<spring:bind path="measurementScale.name">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label" key="measurementScale.name" />
		<div class="controls">
			<form:input path="name" id="name" maxlength="255"
				readonly="${metric.metricOwner ne currentUser && not empty metric.id}" />
			<form:errors path="name" cssClass="help-inline" />
		</div>

		<spring:bind path="metric.type">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label" key="measurementScale.type" />
		<div class="controls">
			<form:select path="type" multiple="false"
				disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
				<form:option value="" label="None" />
				<form:options items="${availablesTypes}" />
			</form:select>
			<form:errors path="type" cssClass="help-inline" />
		</div>

		<%--  <spring:bind path="metric.unit">
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
 --%>
		<spring:bind path="metric.scale">
			<div
				class="control-group${(not empty status.errorMessage) ? ' error' : ''}"></div>
		</spring:bind>
		<appfuse:label styleClass="control-label" key="metric.scale" />
		<div class="controls">
			<form:select path="scale.id"
				disabled="${metric.metricOwner ne currentUser && not empty metric.id}">
				<form:option value="" label="None" />
				<form:options items="${scales}" itemValue="id" itemLabel="name" />
			</form:select>
			<form:errors path="scale" cssClass="help-inline" />
		</div>

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
</script>