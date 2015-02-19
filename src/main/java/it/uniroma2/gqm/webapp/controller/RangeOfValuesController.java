package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.ProjectManager;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;



@Controller
@RequestMapping("/rangeOfValues*")
public class RangeOfValuesController {
	
	
	private ProjectManager projectManager;

	@Autowired
	public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
	    this.projectManager = projectManager;
	}

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpSession session) throws Exception
	{
        Project currentProject = projectManager.getCurrentProject(session);

		return new ModelAndView();
	}

}
