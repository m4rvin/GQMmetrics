<%@ include file="/common/taglibs.jsp"%>
<head>
<title><fmt:message key="binaryTable.title" /></title>
<meta name="menu" content="DefinitionPhaseMenu" />
<style type="text/css">
#main {
	border-style: solid;
	/*position: relative;*/
	display: table-cell;
	width: 100px;
	text-align: center;
}

.child {
	border-style: solid;
	/*position: relative;*/
	display: table-cell;
	width: 100px;
	text-align: center;
}

#suggestion {
	
}
</style>

<script type="text/javascript">
	function colorDiv(divId, varStatus) {
		switch (varStatus) {
		case "1":
			$('#' + divId).css("background-color", "Lime");
			break;
		case "0":
			$('#' + divId).css("background-color", "lightcoral");
			break;
		default:
			$('#' + divId).css("background-color", "orange");
			break;

		}
	}
</script>

</head>
<div class="span10">

	<c:choose>
		<c:when test="${not empty mainGoal}">

			<h2>
				<fmt:message key="binaryTable.heading" />
			</h2>

			<div id="main">
				<a href="binarytable?id=${mainGoal.goal.id}">${mainGoal.goal.description}</a>
				<div>${mainGoal.value}</div>
			</div>
			<c:forEach var="childGoal" items="${childGoal}"
				varStatus="loopStatus" step="1" begin="0" end="${childGoal.size()}">
				<div id="child${loopStatus.index}" class="child">
					<c:choose>
						<c:when test="${childGoal.goal.childType == -1}">
							<a>${childGoal.goal.description}</a>
							<div>${childGoal.value}</div>
						</c:when>
						<c:otherwise>
							<a href="binarytable?id=${childGoal.goal.id}">${childGoal.goal.description}</a>
							<div>${childGoal.value}</div>
						</c:otherwise>
					</c:choose>
					<script type="text/javascript">
						var childValue = "${childGoal.value}";
						var currId = "child" + "${loopStatus.index}";
						colorDiv(currId, childValue);
					</script>
				</div>
			</c:forEach>

			<div id="">
				<h4>
					<fmt:message key="binaryTable.suggestions" />
				</h4>
				<c:forEach var="suggestions" varStatus="item" items="${suggestions}"
					step="1" begin="0" end="${suggestions.size()}">
					<div id="suggestion">
						<c:out value="${item.index+1}" />
						) ${suggestions}
					</div>
				</c:forEach>
			</div>

		</c:when>
		<c:otherwise>
			<h2>
				<fmt:message key="binaryTable.noPermission" />
			</h2>
		</c:otherwise>
	</c:choose>
	<script type="text/javascript">
		$(document).ready(function() {

			var result = "${mainGoal.value}"
			colorDiv("main", result);
		});
	</script>
</div>