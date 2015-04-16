package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.SatisfyingConditionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping(value = ViewName.satysfingConditionForm)
public class SatisfyingConditionFormController
{

	 @Autowired
	 SatisfyingConditionManager satisyfingConditionManager;
	 @Autowired
	 ComplexMetricManager metricManager;
	 @Autowired
	 ProjectManager projectManager;
	 
	 @ModelAttribute
	 private void addModelAttributes(Model model, HttpSession session)
	 {
		  Project project = this.projectManager.getCurrentProject(session);
		  List<AbstractMetric> availableMetrics = this.metricManager.findAllByProject(project);
		  model.addAttribute("availableMetrics", availableMetrics);
	 }
	 
	 @RequestMapping(method = RequestMethod.GET)
	 protected String showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {
		  String id = request.getParameter("id");
		  SatisfyingCondition satisfyingCondition = null;
		  Project currentProject = this.projectManager.getCurrentProject(session);

		  if (!StringUtils.isBlank(id))
		  {
				satisfyingCondition = this.satisyfingConditionManager.get(new Long(id));
		  }
		  else
		  {
				satisfyingCondition = new SatisfyingCondition();
				satisfyingCondition.setProject(currentProject);
		  }
		  
		  model.addAttribute("satisfyingCondition", satisfyingCondition);
		  return ViewName.satysfingConditionForm;
	 }
	 
}
