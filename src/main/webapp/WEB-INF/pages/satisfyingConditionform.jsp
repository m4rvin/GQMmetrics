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
			<c:when test="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}">You cannot modify the selected Satisfying condition, you are not the owner</c:when>
			<c:otherwise>
				<fmt:message key="satisfyingConditionDetail.message" />
			</c:otherwise>
		</c:choose>
	</p>
	<c:if test="${not empty satisfyingCondition.satisfyingConditionOwner}">
	<p><fmt:message key="satisfyingCondition.owner.message"/><b>&nbsp;&nbsp;&nbsp;${satisfyingCondition.satisfyingConditionOwner.fullName}</b></p>
	</c:if>
</div>

<div class="span7">
	<form:errors path="*" cssClass="alert alert-error fade in" 	element="div" />
	<form:form commandName="satisfyingCondition" method="post" action="satisfyingConditionform" id="satisfyingConditionForm" cssClass="well form-horizontal">
		<form:hidden path="id" />
		
		<spring:bind path="satisfyingCondition.project">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.project" />
				<div class="controls">
					<form:select path="project.id" disabled="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}">
						<form:option value="${satisfyingCondition.project.id}" 	label="${satisfyingCondition.project.name}" />
					</form:select>
					<form:errors path="project" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>

		<div class="control-group">
			<appfuse:label styleClass="control-label" key="satisfyingCondition.metric" />
			<div class="controls">
				<select id="metricSelectBox" name="metric" onchange="retriveTargetsAndOperations()" >
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
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.targets" />
				<div class="controls">
					<form:select path="targets" disabled="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}">
						<c:forEach items="${availableTargets}" var="target">
							<c:set var="found" value="false" />
							<c:forEach items="${satisfyingCondition.targets}" var="chosedTarget">
							<script type="text/javascript">
							</script>
								<c:if test="${target == chosedTarget.representation}">
									<c:set var="found" value="true" />
									<script type="text/javascript">
									</script>
								</c:if>
							</c:forEach>
							<c:choose>
								<c:when test="${found}">
									<option id="prova" label="${target}" value="${target}" selected="selected"/>
								</c:when>
								<c:otherwise>
									<option id="prova" label="${target}" value="${target}" />
								</c:otherwise>
							</c:choose>			
						</c:forEach>
					</form:select>
					<form:errors path="targets" cssClass="help-inline" />
					<a onclick="showTargetInfoInstructions()">Instructions</a>
				</div>
			</div>
		</spring:bind>
		
		
		<spring:bind path="satisfyingCondition.satisfyingConditionOperation">
			<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="satisfyingCondition.satisfyingConditionOperation" />
				<div class="controls">
					<form:select path="satisfyingConditionOperation" disabled="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}">
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
					<form:input path="satisfyingConditionValue" disabled="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner ne currentUser}" />
					<form:errors path="satisfyingConditionValue" cssClass="help-inline" />
				</div>
			</div>
		</spring:bind>
		
		<div class="form-actions">
			<c:if test="${satisfyingCondition.satisfyingConditionOwner eq currentUser ||empty satisfyingCondition.id }">
				<button type="submit" class="btn btn-primary" name="save">
					<i class="icon-ok icon-white"></i>
					<fmt:message key="button.save" />
				</button>
			</c:if>
			<c:if test="${not empty satisfyingCondition.id and satisfyingCondition.satisfyingConditionOwner eq currentUser}">
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
		var editing_cond = ($('#id').val() !== "") ? true : false;
		if(metric_id !== "")
		{
			$.ajax({
				type : "GET",
				url : "satisfyingConditionformAjax",
				data : {
					metricId : metric_id,
					editing : editing_cond
				},
				contentType : "application/json",
				success : function(response) {
					var JSONResponse = JSON.parse(response);
					
					var targetsArray = JSONResponse['targets'];
					var operationsArray = JSONResponse['satisfyingOperations']
					if(targetsArray === undefined) //display retrived targets
					{
						jQuery('<div/>', {
						    id: 'emptyTargetsDialog',
						    text: "The current metric has not available targets, please select another metric."
						}).dialog();
					}
					else
					{
						$.each(targetsArray, function() {
							$('#targets').append(
									$("<option></option>").attr("value", this)
											.text(this));
						});
						
						//attach target-info listener to new options
						$('#targets option').mouseup(showTargetInfo);
					}
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
	
	function showTargetInfo(e){

		switch(e.which)
	    {
	        
	        case 2:
	            //middle Click
	           	console.log("mouse middle click");
	            var target = $(this).text();
	            console.log("getting info for target: " + target);
	             
           		$.ajax({
       				type : "GET",
       				url : "satisfyingConditionformGetTargetInfoAjax",
       				data : {
       					target : target,
       				},
       				contentType : "application/json",
       				success : function(response) {
       					
       					console.log("response="+response);

       					if(response== "")
       						return;
       					
       					jQuery('<div/>', {
       					    id: 'targetInfoBox',
       					    title:"Satisfying Condition target info",
       					    text: response
       					}).dialog();
       				},
       				error : function(error) {
       					console.log(error);
       				}
       			});	 
	        break;
	    }
	}

	function showTargetInfoInstructions(){
		jQuery('<div/>', {
		    id: 'dialogInstructions',
		    title:"Target box instructions",
		    text: "Click with the mouse wheel (or press right and left button of your touchpad in case you have a notebook) to retrieve information about each item."
		}).dialog();
	}
</script>