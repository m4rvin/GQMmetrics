<%@ include file="/common/taglibs.jsp"%>
<head>
    <title><fmt:message key="goalDetail.title"/></title>
    <meta name="menu" content="DefinitionPhaseMenu"/>
    <script src="http://d3js.org/d3.v3.min.js"></script>
    <style>
    
		.node {
			cursor: pointer;
		}
	
		.node circle {
		  fill: #fff;
		  stroke: steelblue;
		  stroke-width: 3px;
		}
	
		.node text {
		  font: 12px sans-serif;
		}
	
		.link {
		  fill: none;
		  stroke: #ccc;
		  stroke-width: 2px;
		}
		
    </style>
</head>

<body>

<div id="json_graph"></div>
<div id="identifier" hidden="true">${identifier}</div>

<script>

var url = "graphhelp";
$.post(url, {id: $("#identifier").text()},
		function(responseText) {
			var treeData = [];
			var html = "";
			var json_graph = "";
			
			html = $.parseHTML(responseText);
			json_graph = $(html).find("#json_graph").text().trim();
			treeData.push(JSON.parse(json_graph));
	
			// ************** Generate the tree diagram	 *****************
			var margin = {top: 20, right: 120, bottom: 20, left: 120},
				width = 960 - margin.right - margin.left,
				height = 500 - margin.top - margin.bottom;
				
			var i = 0,
				duration = 750,
				root;
			
			var tree = d3.layout.tree()
				.size([height, width]);
			
			var diagonal = d3.svg.diagonal()
				.projection(function(d) { return [d.y, d.x]; });
			
			var svg = d3.select("#json_graph").append("svg")
				.attr("width", width + margin.right + margin.left)
				.attr("height", height + margin.top + margin.bottom)
			  .append("g")
				.attr("transform", "translate(" + margin.left + "," + margin.top + ")");
			
			root = treeData[0];
			root.x0 = height / 2;
			root.y0 = 0;
			  
			update(root);
			
			d3.select(self.frameElement).style("height", "500px");
		
			function update(source) {
			
			  // Compute the new tree layout.
			  var nodes = tree.nodes(root).reverse(),
				  links = tree.links(nodes);
			
			  // Normalize for fixed-depth.
			  nodes.forEach(function(d) { d.y = d.depth * 180; });
			
			  // Update the nodes
			  var node = svg.selectAll("g.node")
				  .data(nodes, function(d) { return d.id || (d.id = ++i); });
			
			  // Enter any new nodes at the parent's previous position.
			  var nodeEnter = node.enter().append("g")
				  .attr("class", "node")
				  .attr("transform", function(d) { return "translate(" + source.y0 + "," + source.x0 + ")"; })
				  .on("click", click);
			
			  nodeEnter.append("circle")
				  .attr("r", 13)
				  .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });
			
			  nodeEnter.append("svg:a").attr("xlink:href", function(d){
					if(d.type == 0) 
						return "goalform?id="+d.identifier; 
					else if(d.type==1) 
						return "questionform?id="+d.identifier; 
					else 
						return "metricform?id="+d.identifier;
					}).append("text")
				  .attr("x", function(d) { return d.children || d._children ? 0 : 0; })
				  .attr("dy", ".35em").attr("text-anchor", "middle")
				  //.attr("text-anchor", function(d) { return d.children || d._children ? "end" : "start"; })
				  .text(function(d) { 
					  if(d.type == 0) 
						  return "MG"+d.identifier; 
					  else if(d.type==1) 
						  return "Q"+d.identifier; 
					  else 
						  return "M"+d.identifier; })
				  .style("fill-opacity", 1e-6);
			
			  // Transition nodes to their new position.
			  var nodeUpdate = node.transition()
				  .duration(duration)
				  .attr("transform", function(d) { return "translate(" + d.y + "," + d.x + ")"; });
			
			  nodeUpdate.select("circle")
				  .attr("r", 13)
				  .style("fill", function(d) { return d._children ? "lightsteelblue" : "#fff"; });
			
			  nodeUpdate.select("text")
				  .style("fill-opacity", 1);
			
			  // Transition exiting nodes to the parent's new position.
			  var nodeExit = node.exit().transition()
				  .duration(duration)
				  .attr("transform", function(d) { return "translate(" + source.y + "," + source.x + ")"; })
				  .remove();
			
			  nodeExit.select("circle")
				  .attr("r", 13);
			
			  nodeExit.select("text")
				  .style("fill-opacity", 1e-6);
			
			  // Update the links
			  var link = svg.selectAll("path.link")
				  .data(links, function(d) { return d.target.id; });
			
			  // Enter any new links at the parent's previous position.
			  link.enter().insert("path", "g")
				  .attr("class", "link")
				  .attr("d", function(d) {
					var o = {x: source.x0, y: source.y0};
					return diagonal({source: o, target: o});
				  });
			
			  // Transition links to their new position.
			  link.transition()
				  .duration(duration)
				  .attr("d", diagonal);
			
			  // Transition exiting nodes to the parent's new position.
			  link.exit().transition()
				  .duration(duration)
				  .attr("d", function(d) {
					var o = {x: source.x, y: source.y};
					return diagonal({source: o, target: o});
				  })
				  .remove();
			
			  // Stash the old positions for transition.
			  nodes.forEach(function(d) {
				d.x0 = d.x;
				d.y0 = d.y;
			  });
			}
			
			// Toggle children on click.
			function click(d) {
			  if (d.children) {
				d._children = d.children;
				d.children = null;
			  } else {
				d.children = d._children;
				d._children = null;
			  }
			  update(d);
			}
		},
"html");

</script>    

</body>

