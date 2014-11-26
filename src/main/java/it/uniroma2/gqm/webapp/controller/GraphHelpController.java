package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.GraphManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/graphhelp*")
@SessionAttributes({"currentProject","goal","currentUser"})
public class GraphHelpController {
	
	@Autowired
    private GoalManager goalManager;
    
    private GraphManager graphManager;
    
    @Autowired
    public void setGraphManager(@Qualifier("graphManager") GraphManager graphManager) {
    	this.graphManager = graphManager;
    }

    @Autowired
    public void setGoalManager(@Qualifier("goalManager") GoalManager goalManager) {
        this.goalManager = goalManager;
    }
    
    @ModelAttribute
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Valid Goal goal, BindingResult errors, HttpServletRequest request,
            				HttpServletResponse response) throws Exception {
    	
    	String id = request.getParameter("id");
    	System.out.println("IDENTIFIER: "+id);
    	Goal ret = goalManager.get(Long.parseLong(id));
        String graph = graphManager.getGraph(ret);
		
        JSONObject jsonGraph = new JSONObject(graph);
        
        return new ModelAndView().addObject("graph", (JSONObject)jsonGraph);
    }
}
