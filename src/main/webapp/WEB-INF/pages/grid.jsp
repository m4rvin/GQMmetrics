<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="goalDetail.title"/></title>
    <meta name="menu" content="InterpretationPhaseMenu"/>
    <script src="http://d3js.org/d3.v3.min.js"></script>
     <style>
 
		.node circle {
		fill: #fff;
		stroke: steelblue;
		stroke-width: 3px;
		}
		
		.node rect {
		fill: #fff;
		stroke: steelblue;
		stroke-width: 3px;
		}
		 
		.node text { font: 12px sans-serif; }
		 
		.link {
		fill: none;
		stroke: #ccc;
		stroke-width: 1px;
		}
		
		div.tooltip {
		  position: absolute;
		  text-align: center;
		  width: 60px;
		  height: 12px;
		  padding: 8px;
		  font: 10px sans-serif;
		  background: #ddd;
		  border: solid 1px #aaa;
		  border-radius: 8px;
		  pointer-events: none;
		}
	</style>
</head>

<body>

<div id="json_tree"></div>

<script>

var url = "gridhelp";
$.post(url, 
		function(responseText) {
			var treeData = [];
			var html = "";
			var json_tree = "";
			
			html = $.parseHTML(responseText);
			json_tree = $(html).find("#json_tree").text().trim();
			treeData.push(JSON.parse(json_tree));
			
			//console.log(json_tree);
			
			// ************** Generate the tree diagram *****************
			var margin = {top: 40, right: 120, bottom: 20, left: 120},
			width = 960 - margin.right - margin.left,
			height = 500 - margin.top - margin.bottom;
			var i = 0;
			var widthRect = 80;
			var heightRect = 50;
			
			var tree = d3.layout.tree()
			.size([height, width]);
			 
			var diagonal = d3.svg.diagonal()
			.projection(function(d) { return [d.x, d.y]; });
			 
			var svg = d3.select("#json_tree").append("svg")
			.attr("width", width + margin.right + margin.left)
			.attr("height", height + margin.top + margin.bottom)
			.append("g")
			.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
			 
			root = treeData[0];
			update(root);
			 
			function update(source) {
			 
			// Compute the new tree layout.
			var nodes = tree.nodes(root).reverse(),
			links = tree.links(nodes);
			 
			// Normalize for fixed-depth.
			nodes.forEach(function(d) { d.y = d.depth * 100;});
			 
			console.log(nodes);
			
			// Declare the nodes
			var node = svg.selectAll("g.node")
			.data(nodes, function(d) { return d.id || (d.id = ++i); });
			 
			//console.log(node);
			
			// Enter the nodes.
			var nodeEnter = node.enter().append("g")
			.attr("class", "node")
			.attr("transform", function(d) {
			return "translate(" + d.x + "," + d.y + ")"; });
			
			nodeEnter.append("svg:rect")
			.attr("x", -widthRect/2)
			.attr("y", -heightRect/2)
			.attr("width", widthRect)
			.attr("height", heightRect);
			
			//Nome clickabile
			nodeEnter.append("svg:a").attr("xlink:href", function(d){
				if(d.type == 0) 
					return "binarytable?id="+d.identifier; 
				else 
					return "strategyform?id="+d.identifier;
				}).append("text")
			.attr("y", function(d) {
				return d.children || d._children ? -7 : -7; })
			.attr("dy", ".35em")
			.attr("text-anchor", "middle")
			.text(function(d) { if(d.type == 0) return /*OG*/d.name; else return /*S*/d.name; })
			.style("fill-opacity", 1);
			
			nodeEnter.append("text")
			.attr("y", function(d) {
				return d.children || d._children ? 10 : 10; })
			.attr("dy", ".35em")
			.attr("text-anchor", "middle")
			.text(function(d) { if(d.type == 0) return "OG"; else return "Strategy";})
			.style("fill-opacity", 1);
			
			nodeEnter.on("click", function(d){ 
	
				if(d.type == 0) {				
					if(d3.select(this).selectAll("circle").size() == 0 && d.mgs != null){
							
						for (var i = 0; i < d.mgs.length; i++) {
							
							d3.select(this).append("circle")
							.attr("r", 15).attr("cx", 58).attr("cy", 30*i);
							
							d3.select(this).append("svg:a")
							.attr("xlink:href", function(d){
									return "graph?id="+d.mgs[i].identifier; 
									})
								.append("text").attr("dy", ".35em").attr("x", 58).attr("y",30*i)
							.attr("text-anchor", "middle")
							.text(function(d) { return /*MG*/d.mgs[i].description; });
							
						}
					}
				}
			})
			.on("dblclick", function(d){
				if(d.type == 0) {
					if(d3.select(this).selectAll("circle").size() != 0)
						d3.select(this).selectAll("circle").remove();
					if(d3.select(this).selectAll("text").size() != 0)
						d3.select(this).selectAll("text").remove();
					d3.select(this).append("svg:a")
					.attr("xlink:href", function(d){return "binarytable?id="+d.identifier;}).append("text")
					.attr("y", function(d) {
						return d.children || d._children ? -7 : -7; })
					.attr("dy", ".35em")
					.attr("text-anchor", "middle")
					.text(function(d) { if(d.type == 0) return /*OG*/+d.name; else return /*S*/d.name; })
					.style("fill-opacity", 1);
					
					d3.select(this).append("text")
					.attr("y", function(d) {
						return d.children || d._children ? 10 : 10; })
					.attr("dy", ".35em")
					.attr("text-anchor", "middle")
					.text(function(d) { if(d.type == 0) return "OG"; else return "Strategy";})
					.style("fill-opacity", 1);
				}
				});
			 
			// Declare the links
			var link = svg.selectAll("path.link")
			.data(links, function(d) { return d.target.id; });
			 
			// Enter the links.
			link.enter().insert("path", "g")
			.attr("class", "link")
			.attr("d", diagonal);
			 
			}
			
		},
"html");
 
</script>
    

</body>

