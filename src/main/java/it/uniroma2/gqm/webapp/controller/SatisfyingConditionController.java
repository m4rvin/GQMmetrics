package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.SatisfyingConditionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping(value = ViewName.satysfingConditions)
public class SatisfyingConditionController
{
	 @Autowired
	 SatisfyingConditionManager satisfyingConditionManager;
	 @Autowired
	 ProjectManager projectManager;
	 
	 
	 @RequestMapping(method = RequestMethod.GET)
	 public ModelAndView handleRequest(HttpSession session) throws Exception
	 {
		  Project currentProject = this.projectManager.getCurrentProject(session);
		  List<SatisfyingCondition> result = this.satisfyingConditionManager.findByProject(currentProject);
		  return new ModelAndView().addObject("satisfyingConditionList", result);
	 }
}
