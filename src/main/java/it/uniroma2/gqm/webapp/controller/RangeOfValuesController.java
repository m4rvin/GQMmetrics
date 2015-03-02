package it.uniroma2.gqm.webapp.controller;

import java.util.List;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.RangeOfValuesManager;

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
	
	
	private ProjectManager projectManager = null;
	private RangeOfValuesManager rangeOfValuesManager = null;

	@Autowired
	public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
	    this.projectManager = projectManager;
	}
	
	 @Autowired
	 public void setRangeOfValuesManager(@Qualifier("rangeOfValuesManager") RangeOfValuesManager rangeOfValuesManager) {
		this.rangeOfValuesManager = rangeOfValuesManager;
	 }

	
	@RequestMapping(method = RequestMethod.GET)
	public ModelAndView handleRequest(HttpSession session) throws Exception
	{
        Project currentProject = projectManager.getCurrentProject(session);
        List<RangeOfValues> rovs = rangeOfValuesManager.findByProject(currentProject);
     //   rovs = rangeOfValuesManager.getAll();
		return new ModelAndView().addObject("rangeOfValuesList", rovs);
	}

}
