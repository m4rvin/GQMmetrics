<%@ include file="/common/taglibs.jsp"%>

<head>
    <title><fmt:message key="mainMenu.title"/></title>
    <meta name="menu" content="MainMenu"/>
</head>
<body class="home">

<h2><fmt:message key="mainMenu.heading"/></h2>
<p><fmt:message key="mainMenu.message1"/></p>
<img alt="GQM+Strategies" src="images/GQM+Strategies_logo.jpg"/>

    <p><fmt:message key="mainMenu.message2"/></p>
    <form:form commandName="currentProject" method="post" action="projectselection" id="projectselection">
	        <div class="controls">
	        	<form:select path="id" onchange="this.form.submit();"  >
						<form:options  items="${projects}" itemLabel="name" itemValue="id" />
				</form:select>  
	        
            	<form:errors path="id" cssClass="help-inline"/>
        	</div>               
               
	</form:form>
	
</body>
