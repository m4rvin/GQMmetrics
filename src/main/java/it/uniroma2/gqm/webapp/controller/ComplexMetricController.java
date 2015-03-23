package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
@RequestMapping("/metrics*")
public class ComplexMetricController {
    @Autowired
    private ComplexMetricManager metricManager;

    private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpSession session) throws Exception {
        Project currentProject = projectManager.getCurrentProject(session);
		List<AbstractMetric> ret = metricManager.findByProject(currentProject);
		return new ModelAndView().addObject("metricList", ret);
	}
}