
<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="goalDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
    <style type="text/css">
    #helpbox { 
		position: relative;
		padding: 5px;
		width: 200px;
    }
    </style>
</head>
 
<div class="span2">
    <h2><fmt:message key='goal.heading'/></h2>
    <p><fmt:message key="goal.message"/></p>
    <c:choose>
    	<c:when test="${goal.goalOwner eq currentUser}">You are <b>Goal Owner</b> for this Goal.</c:when>
		<c:when test="${goal.goalEnactor eq currentUser}">You are <b>GoalEnactor</b> for this Goal.</c:when>		
		<c:when test="${goal.QSMembers.contains(currentUser)}">You are <b>Question Stakeholder</b> for this Goal.</c:when>
		<c:when test="${goal.MMDMMembers.contains(currentUser)}">You are <b>Metric Model Data Manager</b> for this Goal.</c:when>
		<c:when test="${goal.project.projectManagers.contains(currentUser)}">You are <b>Project Manager</b> for this Project.</c:when>		
	</c:choose>
</div>


<div class="span7">
    <form:errors path="*" cssClass="alert alert-error fade in" element="div"/>
    <form:form commandName="goal" method="post" action="goalform" id="goalForm"
               cssClass="well form-horizontal">
    <form:hidden path="id"/>

    <spring:bind path="goal.project">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.project"/>
        <div class="controls">
        	<form:select path="project.id" onchange="" >
					<form:option value="${goal.project.id}" label="${goal.project.name}"/>
			</form:select>  
            <form:errors path="project" cssClass="help-inline"/>
        </div>
    </div>

    <spring:bind path="goal.description">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.description"/>
        <div class="controls">
            <form:input path="description" id="description" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
            <form:errors path="description" cssClass="help-inline"/>
        </div>
    </div>
	
    <spring:bind path="goal.type">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.type"/>
        <div class="controls">
        	<form:select path="type"
        		onchange="if(this.form.type.value == 0) { //ho selezionato OG, mostro divOG e annullo gli eventuali campi di MG già riempiti
        					document.getElementById('divOG').style.display='block';
        					document.getElementById('divMG').style.display='none';
        					
        					resetMGFields();
        				  } 
        				  else { //ho selezionato MG, mostro divMG, e annullo gli eventuali campi di OG già riempiti
        				  	document.getElementById('divOG').style.display='none';
        				  	document.getElementById('divMG').style.display='block';
        				  	document.getElementById('parentOrg').style.display='none';
				        	document.getElementById('parentStra').style.display='none';
        				  	document.getElementById('childOrg').style.display='none';
				        	document.getElementById('childStra').style.display='none';
        				  	
        				  	resetOGFields();
        				  }"
        		disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser && empty goal.type)}">
        		
        		<c:choose>
		    		<c:when test="${goal.type eq 0}">
		    			<form:option value="0"  label="Organizational Goal"/>  		
					</c:when>
					<c:when test="${goal.type eq 1}">
						<form:option value="1"  label="Measurement Goal"/>
					</c:when>
					<c:otherwise>
					    <form:option value="0"  label="Organizational Goal"/>
						<form:option value="1"  label="Measurement Goal"/>
					</c:otherwise>
		    	</c:choose>
        	</form:select>
        </div>
    </div>
	
	<%-- ################################################ --%>
		
    <spring:bind path="goal.scope">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.scope"/>
        <div class="controls">
            <form:input path="scope" id="scope" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
            <form:errors path="scope" cssClass="help-inline"/>
        </div>
    </div>

    <spring:bind path="goal.focus">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.focus"/>
        <div class="controls">
            <form:input path="focus" id="focus" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
            <form:errors path="focus" cssClass="help-inline"/>
        </div>
    </div>

	<c:choose>
	   	<c:when test="${goal.type ne 1}">
	    	<div id="divOG" >
		</c:when>
		<c:otherwise>
			<div id="divOG" hidden="true">
		</c:otherwise>
    </c:choose>	

				<spring:bind path="goal.parentType">
				<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
				<appfuse:label styleClass="control-label" key="goal.parentType"/>
				</spring:bind>
					<div class="controls"> 
						<form:select path="parentType" id="parentType"
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
								disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"
								cssStyle="width:400px" >
							<form:option value="-1" selected="${empty goal.parentType ? 'selected' : ''}">None</form:option>
							<form:option value="0">Organizational Goal</form:option>
							<form:option value="1">Strategy</form:option>
						</form:select>
						<form:errors path="parentType" cssClass="help-inline"/>
					</div>
				</div>
				
				<c:choose>
				   	<c:when test="${goal.parentType eq 0}">
				    	<div id="parentOrg" >
					</c:when>
					<c:otherwise>
						<div id="parentOrg" hidden="true">
					</c:otherwise>
			    </c:choose>
			    			<spring:bind path="goal.orgParent">
							<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							<appfuse:label styleClass="control-label" key="goal.org.parent"/>
							</spring:bind>
								<div class="controls" > 
									<form:select  path="orgParent" id="orgParent" onchange=""
											disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"
											cssStyle="width:400px" >
												<form:option value="-1">None</form:option>
												<form:options items="${goalParent}" itemValue="id" itemLabel="description"/>
									</form:select>
									<form:errors path="orgParent" cssClass="help-inline"/>
								</div>
							</div>
						</div>
						
				<c:choose>
				   	<c:when test="${goal.parentType eq 1}">
				    	<div id="parentStra" >
					</c:when>
					<c:otherwise>
						<div id="parentStra" hidden="true">
					</c:otherwise>
			    </c:choose>
			    			<spring:bind path="goal.ostrategyParent">
							<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							<appfuse:label styleClass="control-label" key="goal.strategies.parent"/>
							</spring:bind>
								<div class="controls"> 
									<form:select path="ostrategyParent" id="ostrategyParent" onchange=""
											disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"
											cssStyle="width:400px" >
												<form:option value="-1">None</form:option>
												<form:options items="${strategyParent}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="ostrategyParent" cssClass="help-inline"/>
								</div>
							</div>
						</div>
				
				<div class="control-group">
				<appfuse:label styleClass="control-label" key="goal.childType"/>
					<div class="controls"> 
						<form:select path="childType" id="childType"
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
				        				  else {
				        				  	document.getElementById('childOrg').style.display='none';
				        					document.getElementById('childStra').style.display='none';
				        					document.getElementById('childOrg').value = '-1';
				        					document.getElementById('childStra').value = '-1';
				        				  }"
								disabled="true"
								cssStyle="width:400px" >
							<form:option value="-1" selected="${empty goal.childType ? 'selected' : ''}">None</form:option>
							<form:option value="0">Organizational Goal</form:option>
							<form:option value="1">Strategy</form:option>
						</form:select>
						<form:errors path="childType" cssClass="help-inline"/>
					</div>
				</div>
				
				<c:choose>
				   	<c:when test="${goal.childType eq 0}">
				    	<div id="childOrg" >
					</c:when>
					<c:otherwise>
						<div id="childOrg" hidden="true">
					</c:otherwise>
			    </c:choose>
			    			<spring:bind path="goal.orgChild">
							<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							<appfuse:label styleClass="control-label" key="goal.org.child"/>
							</spring:bind>
							<div class="controls"> 
									<form:select path="orgChild" id="orgChild" multiple="true" onchange=""
											disabled="true"
											cssStyle="width:400px" >
												<form:option value="-1">None</form:option>
												<form:options items="${goalChildren}" itemValue="id" itemLabel="description"/>
									</form:select>
									<form:errors path="orgChild" cssClass="help-inline"/>
								</div>
							</div>
						</div>
						
				<c:choose>
				   	<c:when test="${goal.childType eq 1}">
				    	<div id="childStra" >
					</c:when>
					<c:otherwise>
						<div id="childStra" hidden="true">
					</c:otherwise>
			    </c:choose>
			    			<spring:bind path="goal.ostrategyChild">
							<div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
							<appfuse:label styleClass="control-label" key="goal.strategies.child"/>
							</spring:bind>
								<div class="controls"> 
									<form:select path="ostrategyChild" id="ostrategyChild" multiple="true" onchange=""
											disabled="true"
											cssStyle="width:400px" >
												<form:option value="-1">None</form:option>
												<form:options items="${strategyChildren}" itemValue="id" itemLabel="name"/>
									</form:select>
									<form:errors path="ostrategyChild" cssClass="help-inline"/>
								</div>
							</div>
						</div>
    
    	
		        <div class="control-group">
				<appfuse:label styleClass="control-label" key="goal.associated_mg"/>
					<div class="controls">
						<form:select path="associatedMGs" id="associatedMGs" onchange="" 
								disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"
								cssStyle="width:500px" multiple="multiple">	
							<option value="-1">None</option>
							<form:options items="${associableMGoals}" itemValue="id" itemLabel="description"/>
					</form:select>
						<form:errors path="associatedMGs" cssClass="help-inline"/>
					</div>
				</div>
				
				
		        
			    <spring:bind path="goal.activity">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.activity"/>
			        <div class="controls">
			            <form:input path="activity" id="activity" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="activity" cssClass="help-inline"/>
			        </div>
			    </div>
			
			    <spring:bind path="goal.object">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.object"/>
			        <div class="controls">
			            <form:input path="object" id="object" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="object" cssClass="help-inline"/>
			        </div>
			    </div>   
			
			
			    <spring:bind path="goal.magnitude">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.magnitude"/>
			        <div class="controls">
			            <form:input path="magnitude" id="magnitude" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="magnitude" cssClass="help-inline"/>
			        </div>
			    </div>  
			
			    <spring:bind path="goal.timeframe">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.timeframe"/>
			        <div class="controls">
			            <form:input path="timeframe" id="timeframe" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="timeframe" cssClass="help-inline"/>
			        </div>
			    </div> 		
			    
			    <spring:bind path="goal.constraints">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.constraints"/>
			        <div class="controls">
			            <form:input path="constraints" id="constraints" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="constraints" cssClass="help-inline"/>
			        </div>
			    </div>	   	
			</div>
    		
	<c:choose>
	   	<c:when test="${goal.type eq 1}">
	    	<div id="divMG">
		</c:when>
		<c:otherwise>
			<div id="divMG" hidden="true">
		</c:otherwise>
    </c:choose>
		        <div class="control-group">
				<appfuse:label styleClass="control-label" key="goal.associated_og"/>
					<div class="controls">
						<form:select path="associatedOG" id="associatedOG"
								onchange="showHelpBox()" 
								disabled="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"
								cssStyle="width:400px">							
							<form:option value="-1">None</form:option>
							<form:options items="${associableOGoals}" itemValue="id" itemLabel="description"/>
						</form:select>
						<form:errors path="associatedOG" cssClass="help-inline"/>
					</div>
				</div> 
							 
			    <spring:bind path="goal.subject">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.subject"/>
			        <div class="controls">
			            <form:input path="subject" id="subject" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="subject" cssClass="help-inline"/>
			        </div>
			    </div>
			
			
			    <spring:bind path="goal.context">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.context"/>
			        <div class="controls">
			            <form:input path="context" id="context" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="context" cssClass="help-inline"/>
			        </div>
			    </div>
			
			    <spring:bind path="goal.viewpoint">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.viewpoint"/>
			        <div class="controls">
			            <form:input path="viewpoint" id="viewpoint" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="viewpoint" cssClass="help-inline"/>
			        </div>
			    </div>
			
			    <spring:bind path="goal.impactOfVariation">
			    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
			    </spring:bind>
			        <appfuse:label styleClass="control-label" key="goal.impactOfVariation"/>
			        <div class="controls">
			            <form:input path="impactOfVariation" id="impactOfVariation" maxlength="255" readonly="${!((goal.status eq 'DRAFT' || goal.status eq 'FOR_REVIEW') && goal.goalOwner eq currentUser)}"/>
			            <form:errors path="impactOfVariation" cssClass="help-inline"/>
			        </div>
			    </div>		     
			</div>
		                        
    <div class="control-group">
        <appfuse:label styleClass="control-label" key="goal.go"/>
        <div class="controls">      
            <form:select path="goalOwner.id" onchange="">
					<form:option value="${goal.goalOwner.id}" label="${goal.goalOwner.fullName}"/>
			</form:select>            	
            <form:errors path="goalOwner" cssClass="help-inline"/>
        </div>
    </div>
	    <div class="control-group">
	        <appfuse:label styleClass="control-label" key="goal.ge"/>
	        <div class="controls">        	
				<form:select path="goalEnactor.id" id="goalEnactor" onchange=""  disabled="${ ((goal.status ne 'DRAFT') ||  goal.goalOwner ne currentUser) ? 'true':'false'}">
					<form:options items="${availableUsers}" itemValue="id" itemLabel="fullName"   />
				</form:select>            
	            <form:errors path="goalEnactor" cssClass="help-inline"/>
	        </div>
	    </div>   
		    
	<c:if test="${visibleGESection}">
	
	    <div class="control-group">
	        <appfuse:label styleClass="control-label" key="goal.qs"/>
	        <div class="controls">        	
			    <form:select path="QSMembers" multiple="true"  size="6" disabled="${(goal.goalEnactor ne currentUser) || (goal.status eq 'APPROVED')}">
			    	<form:options items="${availableUsers}" itemLabel="fullName" itemValue="id"/>	
			    </form:select>       
	            <form:errors path="QSMembers" cssClass="help-inline"/>
	        </div>
	    </div>   
	
		<spring:bind path="goal.MMDMMembers">
	    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	    </spring:bind>
	        <appfuse:label styleClass="control-label" key="goal.mmdm"/>
	        <div class="controls">
	        	<form:select path="MMDMMembers" onchange="" disabled="${(goal.goalEnactor ne currentUser) || (goal.status eq 'APPROVED')}">        		
	        		<form:options items="${availableUsers}" itemValue="id" itemLabel="fullName"   />								
				</form:select>
	            <form:errors path="MMDMMembers" cssClass="help-inline"/>
	        </div>
	    </div>
	            
	</c:if>	
	
    <spring:bind path="goal.status">
    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
    </spring:bind>
        <appfuse:label styleClass="control-label" key="goal.status"/>
        <div class="controls">
			<form:select path="status" multiple="false" >
		    	<form:options items="${availableStatus}"/>
		    </form:select>		
		    <c:if test="${goal.status eq 'PROPOSED' && goal.project.projectManagers.contains(currentUser)}">
		    
				<c:set var="justVoted" value="false" />
				<c:forEach var="item" items="${goal.votes}">
  					<c:if test="${item eq currentUser}">
    					<c:set var="justVoted" value="true" />
  					</c:if>
				</c:forEach>	
				<c:choose>
					<c:when test="${justVoted}">					
						<input type="checkbox" name="vote" value="true" checked="checked" disabled="disabled">  Accepted by ${goal.numberOfVote}/${goal.quorum} Project Managers</input>
					</c:when>
					<c:otherwise>
						<input type="checkbox" name="vote" value="true">  Mark as accepted. Is just accepted by ${goal.numberOfVote}/${goal.quorum} Project Managers</input>
					</c:otherwise>
				</c:choose>	    		    
		    </c:if>
		    		    
            <form:errors path="status" cssClass="help-inline"/>
        </div>
    </div>

    <c:if test="${(((goal.goalEnactor eq currentUser) || (goal.goalOwner eq currentUser)) && (goal.status eq 'FOR_REVIEW' || goal.status eq 'ACCEPTED'))}">
	    <spring:bind path="goal.refinement">
	    <div class="control-group${(not empty status.errorMessage) ? ' error' : ''}">
	    </spring:bind>
	        <appfuse:label styleClass="control-label" key="goal.refinement"/>
	        <div class="controls">
	            <form:input path="refinement" id="refinement" maxlength="255" readonly="${goal.goalOwner eq currentUser}"	/>
	            <form:errors path="refinement" cssClass="help-inline"/>
	        </div>
	    </div>
    </c:if>
    
    <div id="dialog-confirm" title="Proceed?" hidden="true">
  		<p><span class="ui-icon ui-icon-alert" style="float:left; margin:0 7px 20px 0;"></span>You are creating/modifying a Measurement Goal without associated Organizational Goal. Are you sure?</p>
	</div>
	
	<div id="helpbox" title="Help box" hidden="true">
		<c:forEach var="itemGoal" items="${associableOGoals}">
			<div id="${itemGoal.id}" hidden="true">
				<div>Object: ${itemGoal.object}</div>
				<div>Scope: ${itemGoal.scope}</div>
				<div>Focus: ${itemGoal.focus}</div>
				<div>Constrainst: ${itemGoal.constraints}</div>
			</div>
		</c:forEach>
	</div>
    
    <div class="form-actions">
        <button type="submit" onclick="setClickedButton('save')" class="btn btn-primary" name="save">
            <i class="icon-ok icon-white"></i> <fmt:message key="button.save"/>
        </button>
        <c:if test="${not empty goal.id && goal.status eq 'DRAFT' && goal.goalOwner eq currentUser}">
          <button type="submit" onclick="setClickedButton('delete')" class="btn" name="delete">
              <i class="icon-trash"></i> <fmt:message key="button.delete"/>
          </button>
        </c:if>
        <button type="submit" onclick="setClickedButton('cancel')" class="btn" name="cancel">
            <i class="icon-remove"></i> <fmt:message key="button.cancel"/>
        </button>
    </div>
    </form:form>
</div>

<%--
<c:if test="${(goal.type eq 0) && (goal.status eq 'ACCEPTED') && (goal.goalEnactor eq currentUser)}">
	<div class="span2">
	    <h2><fmt:message key="goal.splitting"/></h2>
	    <form:form commandName="goal" method="post" action="goalsplit" id="goalsplit"
	               cssClass="well form-vertival">
	    	<form:hidden path="id"/>    	
	    	
	    	<img alt="GQM+Strategies" src="images/GQM+Strategies_logo.jpg"/>
	    	
	    	<div>
	    		<br/>	    	
	    		Number of split:
	    		<select id="split" name="split" style="width:80px;">			  
				  <option value="2">2</option>
				  <option value="3">3</option>
				  <option value="4">4</option>
				  <option value="5">5</option>
				  <option value="6">6</option>
				  <option value="7">7</option>
				  <option value="8">8</option>
				  <option value="9">9</option>
				  <option value="10">10</option>			  
				</select> 
	    	</div>
		    <br/>
	        <button type="submit" class="btn btn-primary" name="split">
	            <i class="icon-ok icon-white"></i> <fmt:message key="button.split"/>
	        </button>	    	
		</form:form>    
	</div>
</c:if>--%>

<script type="text/javascript">

	/*$('#goalForm').submit(function() {		
	    $('#childType').removeAttr('disabled');
	    $('#orgChild').removeAttr('disabled');
	    $('#ostrategyChild').removeAttr('disabled');
	});*/
	
	function resetMGFields() {
	  	document.getElementById('associatedOG').value = '-1';
	  	document.getElementById('subject').value = '';
	  	document.getElementById('context').value = '';
	  	document.getElementById('viewpoint').value = '';
	  	document.getElementById('impactOfVariation').value = '';
	}
	
	function resetOGFields() {
	  	document.getElementById('associatedMGs').value = '-1';
	  	document.getElementById('activity').value = '';
	  	document.getElementById('object').value = '';
	  	document.getElementById('magnitude').value = '';
	  	document.getElementById('timeframe').value = '';
	  	document.getElementById('constraints').value = '';
	  	
	  	document.getElementById('parentType').value = '-1';
		document.getElementById('orgParent').selectedIndex = '-1';
		document.getElementById('ostrategyParent').selectedIndex = '-1';
		document.getElementById('childType').value = '-1';
		document.getElementById('orgChild').selectedIndex = '-1';
		document.getElementById('ostrategyChild').selectedIndex = '-1';
	}
	
	var prev = -1;
	
	function showHelpBox(){
		
		var e = document.getElementById("associatedOG");
		var current = e.options[e.selectedIndex].value;
		
		//If an OG is selected
		if(current != -1){
			if(prev != -1)
				$('#'+prev).attr('hidden','true');
			
			$('#'+current).removeAttr('hidden');
			$('#helpbox').dialog( {
			      resizable: true,
			      modal: false
			    });
			
		//If NONE is selected
		} else {
			$('#helpbox').dialog( "destroy" );
			$('#'+prev).attr('hidden','true');
		}
		//console.log("prev: "+prev);
		//console.log("current: "+current);
		prev = current;
	}
	
	//Prima di sottomettere il form, riabilito i select disabilitati, per poter passare i relativi valori al controller
	function enableSelect() {
	    $('#childType').removeAttr('disabled');
	    $('#orgChild').removeAttr('disabled');
	    $('#ostrategyChild').removeAttr('disabled');
	}
	
	var map = new Object();
	map["save"] = false;
	map["delete"] = false;
	map["cancel"] = false;
	
	function setClickedButton(buttonClicked) {
		map[buttonClicked] = true;
	}
	
	function resetClickedButton() {
		map["save"] = false;
		map["delete"] = false;
		map["cancel"] = false;
	}
	
	$('#goalForm').submit(function(event) {	
		event.preventDefault();
		
		var goalForm = $(this); //$('#goalForm')		
		var mg_without_og = ($("#type").val() == 1 && $('#associatedOG').val() == -1);
		
		if(mg_without_og) { //goal mg senza og associato
			if(map["save"]) { //stò salvando
				$("#dialog-confirm").dialog( {
				      resizable: false,
				      height:200,
				      modal:true,
				      buttons: {
				        "Confirm": function() {
				         	$(this).dialog("close");				         	
				         	enableSelect(); //riabilito select disabilitate
				         	goalForm.unbind('submit').submit();	          
				        },
				        Cancel: function() {
				        	$(this).dialog("close");
				        }
				      }
				    });				
			}
		}
	
		//posso continuare se ho cliccato su delete, su cancel, oppure se stò salvando un goal diverso da un mg senza og
		var go_submit = (map["delete"] || map["cancel"] || (map["save"] && !mg_without_og)); 
		
		if(go_submit) {
			enableSelect(); //riabilito select disabilitate
			goalForm.unbind('submit').submit();
		}			
		
		resetClickedButton(); //restto lo stato dei pulsanti
	});
	
    $(document).ready(function() {
        $("input[type='text']:visible:enabled:first", document.forms['goalForm']).focus();
    });
    
    jQuery(function($) {
        $('form').bind('submit', function() {
            $(this).find(':input').removeAttr('disabled');
        });

    });
</script>
