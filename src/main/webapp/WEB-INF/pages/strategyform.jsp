<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="strategyDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
</head>
 
<div class="span2">
    <h2><fmt:message key='strategyDetail.heading'/></h2>
    <c:choose>
    	<c:when test="${strategy.strategyOwner eq currentUser}">You are <b>Owner</b> for this Strategy.</c:when>
    </c:choose>
</div>

<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="strategy" method="post" action="strategyform" id="strategyForm"
               cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="strategy.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="strategy.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange=""  >
					<form:option value="${strategy.project.id}" label="${strategy.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>
    <spring:bind path="strategy.name">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="strategy.name"/>
        <div class="controls">
            <form:input path="name" id="description" maxlength="255"  />
            <form:errors path="name" cssClass="help-inline"/>
        </div>
    </div>

    <spring:bind path="strategy.assumption">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="strategy.assumption"/>
        <div class="controls">
        	<form:textarea path="assumption" id="assumption" cols="200" rows="6" />
            <form:errors path="assumption" cssClass="help-inline"/>
        </div>
    </div>            
    
    <%-- ################################################ --%>
	
	<spring:bind path="strategy.parentType">
	<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	<appfuse:label styleClass="control-label" key="strategy.parentType"/>
	</spring:bind>
		<div class="controls"> 
			<form:select path="parentType"
					onchange="if(this.form.parentType.value == 0) {
	        					document.getElementById('parentOrg').style.display='block';
	        					document.getElementById('parentStra').style.display='none';
	        					document.getElementById('parentStra').value = '-1';	
	        				  } 
	        				  else if(this.form.parentType.value == 1) {
	        					document.getElementById('parentOrg').style.display='none';
	        					document.getElementById('parentStra').style.display='block';
	        					document.getElementById('parentOrg').value = '-1';
	        				  }
	        				  else {
	        				  	document.getElementById('parentOrg').style.display='none';
	        				  	document.getElementById('parentStra').style.display='none';
	        				  	document.getElementById('parentOrg').value = '-1';
	        				  	document.getElementById('parentStra').value = '-1';	        				  	
	        				  }"
					disabled=""
					cssStyle="width:400px" >
				<form:option value="-1" selected="${empty strategy.parentType ? 'selected' : ''}">None</form:option>
				<form:option value="0">Organizational Goal</form:option>
				<form:option value="1">Strategy</form:option>
			</form:select>
			<form:errors path="parentType" cssClass="help-inline"/>
		</div>
	</div>
	
	<c:choose>
	   	<c:when test="${strategy.parentType eq 0}">
	    	<div id="parentOrg" >
		</c:when>
		<c:otherwise>
			<div id="parentOrg" hidden="true">
		</c:otherwise>
    </c:choose>
    			<%--
    			<c:set var="contains" value="false"></c:set>
    			<c:forEach var="item1" items="${goalParent}">
    				<c:forEach var="item2" items="${item1.orgChild}">    				
	    				<c:choose>
	    					<c:when test="${item2 eq goal}">
	    						<c:set var="contains" value="true"></c:set>
	    					</c:when>
	    				</c:choose>
    				</c:forEach>
    			</c:forEach>
    			--%>
    
    			<spring:bind path="strategy.sorgParent">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="strategy.org.parent"/>
				</spring:bind>
					<div class="controls"> 
						<form:select  path="sorgParent" onchange="" disabled=""	cssStyle="width:400px" >
									<form:option value="-1">None</form:option>
									<form:options items="${goalParent}" itemValue="id" itemLabel="description"/>
						</form:select>
						<form:errors path="sorgParent" cssClass="help-inline"/>
					</div>
				</div>
			</div>
			
	<c:choose>
	   	<c:when test="${strategy.parentType eq 1}">
	    	<div id="parentStra" >
		</c:when>
		<c:otherwise>
			<div id="parentStra" hidden="true">
		</c:otherwise>
    </c:choose>
    			<spring:bind path="strategy.strategyParent">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="strategy.strategies.parent"/>
				</spring:bind>
					<div class="controls"> 
						<form:select path="strategyParent" onchange="" disabled="" cssStyle="width:400px" >
									<form:option value="-1">None</form:option>
									<form:options items="${strategyParent}" itemValue="id" itemLabel="name"/>
						</form:select>
						<form:errors path="strategyParent" cssClass="help-inline"/>
					</div>
				</div>
			</div>
	
	<div class="control-group">
	<appfuse:label styleClass="control-label" key="strategy.childType"/>
		<div class="controls"> 
			<form:select path="childType" 
					onchange="if(this.form.childType.value == 0) { 
	        					document.getElementById('childOrg').style.display='block';
	        					document.getElementById('childStra').style.display='none';
	        					document.getElementById('childStra').value = '-1';	
	        				  } 
	        				  else if(this.form.childType.value == 1) { 
	        					document.getElementById('childOrg').style.display='none';
	        					document.getElementById('childStra').style.display='block';
	        					document.getElementById('childOrg').value = '-1';
	        				  }
	        				  else if(this.form.childType.value == -1) { 
	        					document.getElementById('childOrg').style.display='none';
	        					document.getElementById('childStra').style.display='none';
	        					document.getElementById('childOrg').value = '-1';
	        					document.getElementById('childStra').value = '-1';
	        				  }"					
					cssStyle="width:400px" disabled="true">
				<form:option value="-1">None</form:option>
				<form:option value="0">Organizational Goal</form:option>
				<form:option value="1">Strategy</form:option>
			</form:select>
			<form:errors path="childType" cssClass="help-inline"/>
		</div>
	</div>
	
	<c:choose>
	   	<c:when test="${strategy.childType eq 0}">
	    	<div id="childOrg" >
		</c:when>
		<c:otherwise>
			<div id="childOrg" hidden="true">
		</c:otherwise>
    </c:choose>
    			<spring:bind path="strategy.sorgChild">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="strategy.org.child"/>
				</spring:bind>
					<div class="controls"> 
						<form:select path="sorgChild" multiple="true" onchange="" disabled="true" cssStyle="width:400px" >
									<form:option value="-1">None</form:option>
									<form:options items="${goalChildren}" itemValue="id" itemLabel="description"/>
						</form:select>
						<form:errors path="sorgChild" cssClass="help-inline"/>
					</div>
				</div>
			</div>
			
	<c:choose>
	   	<c:when test="${strategy.childType eq 1}">
	    	<div id="childStra" >
		</c:when>
		<c:otherwise>
			<div id="childStra" hidden="true">
		</c:otherwise>
    </c:choose>
    			<spring:bind path="strategy.strategyChild">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="strategy.strategies.child"/>
				</spring:bind>
					<div class="controls"> 
						<form:select path="strategyChild" multiple="true" onchange="" disabled="true" cssStyle="width:400px" >
									<form:option value="-1">None</form:option>
									<form:options items="${strategyChildren}" itemValue="id" itemLabel="name"/>
						</form:select>
						<form:errors path="strategyChild" cssClass="help-inline"/>
					</div>
				</div>
			</div>
			
    <%--#################################################################### --%>
    <div class="form-actions">
        <button type="submit" class="btn btn-primary" name="save">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </button>
        <%--<c:if test="${not empty strategy.id  && strategy.goals.size() eq 0  && strategy.strategyOwner eq currentUser }">--%>
        <c:if test="${not empty strategy.id && strategy.strategyOwner eq currentUser }">
          <button type="submit" class="btn" name="delete">
              <i class="icon-trash"></i> <fmt:message key="button.delete"/>
          </button>
        </c:if>
        <button type="submit" class="btn" name="cancel">
            <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
        </button>
    </div>
    </form:form>
</div>
<script type="text/javascript">

	/*Prima di sottomettere il form, riabilito i select disabilitati, per poter passare i relativi valori al controller*/
	$('#strategyForm').submit(function() {
		$('#childType').removeAttr('disabled');
	    $('#sorgChild').removeAttr('disabled');
	    $('#strategyChild').removeAttr('disabled');	    
	});

    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['strategyForm']).focus();
    });
</script>