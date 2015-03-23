package it.uniroma2.gqm.webapp.controller;

import java.util.List;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.SimpleMetricManager;
import it.uniroma2.gqm.service.ProjectManager;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/goalsatisfaction*")
@SessionAttributes({"currentProject","goal","currentUser"})
public class GoalSatisfactionController {
	private GoalManager goalManager;
	private SimpleMetricManager metricManager;
	
	@Autowired
	public void setMetricManager(@Qualifier("metricManager") SimpleMetricManager metricManager) {
		this.metricManager = metricManager;
	}
	
	@Autowired
	public void setGoalManager(@Qualifier("goalManager") GoalManager goalManager) {
		this.goalManager = goalManager;
	}

	private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpSession session,
			@RequestParam(required = false, value = "g") String query) throws Exception {
		ModelAndView model =new ModelAndView().addObject(goalManager.getMeasuredGoal(projectManager.getCurrentProject(session)));
		if(!StringUtils.isEmpty(query)){
			Goal selectedGoal = goalManager.get(new Long(query));
			model.addObject("currentGoal", selectedGoal);
			
			List<SimpleMetric> metrics = goalManager.getMeasuredMetricByGoal(selectedGoal);
			model.addObject("metrics", metrics);
		}
		model.addObject("availableMetrics", metricManager.getAll());		
		System.out.println("---------------> g " + query);
		return model;
	}
}
