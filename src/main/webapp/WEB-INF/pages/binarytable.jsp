<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="binaryTable.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
    <style type="text/css">
	    #main {
	    	border-style: solid;
		    /*position: relative;*/
		    display: table-cell;		    
		    width: 100px;
		    text-align: center;
		}
		
		#child {
			border-style: solid;
		    /*position: relative;*/
		    display: table-cell;		    
		    width: 100px;
		    text-align: center;
		}
		#suggestion {
			
		}
    </style>
    
</head>
<div class="span10">

	<c:choose>
		<c:when test="${not empty mainGoal}">
		
		    <h2><fmt:message key="binaryTable.heading"/></h2>
    
			<div id="main">
		   		<a href="binarytable?id=${mainGoal.goal.id}">OG${mainGoal.goal.id}</a>
		   		<div>${mainGoal.value}</div>
		  	</div>
			<c:forEach var="childGoal" items="${childGoal}" step="1" begin="0" end="${childGoal.size()}">
				<div id="child">
					<c:choose>
						<c:when test="${childGoal.goal.childType == -1}">
							<a>OG${childGoal.goal.id}</a>
			    			<div>${childGoal.value}</div>
						</c:when>
			    		<c:otherwise>
			    			<a href="binarytable?id=${childGoal.goal.id}">OG${childGoal.goal.id}</a>
			    			<div>${childGoal.value}</div>
			    		</c:otherwise>
		    		</c:choose>
		  		</div>
			</c:forEach>
			
			<div id="">
				<h4><fmt:message key="binaryTable.suggestions"/></h4>
				<c:forEach var="suggestions" varStatus="item" items="${suggestions}" step="1" begin="0" end="${suggestions.size()}" >
					<div id="suggestion"><c:out value="${item.index+1}"/> ) ${suggestions}</div>
				</c:forEach>
			</div>
		
		</c:when>
   		<c:otherwise>
   			<h2><fmt:message key="binaryTable.noPermission"/></h2>
   		</c:otherwise>
  	</c:choose>
  	
  	<script type="text/javascript">
  	
  	 $(document).ready(function() {
         
  		 var result = "${mainGoal.value}"
  		 
  		 switch(result)
  		 {
	  		case "1":
	  			 $('#main').css("background-color", "Lime");
	  			 break;
	  		case "0":
	 			 $('#main').css("background-color", "lightcoral");
	 			 break;
	  		default:
	 			 $('#main').css("background-color", "orange");
  			break;
 			 
  		 }
     });
  	
  	</script>
  	
</div>