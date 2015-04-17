package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionOperationEnum;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.SatisfyingConditionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;

@Controller
public class SatisfyingConditionFormController extends BaseFormController
{

	 @Autowired
	 SatisfyingConditionManager satisyfingConditionManager;
	 @Autowired
	 ComplexMetricManager metricManager;
	 @Autowired
	 ProjectManager projectManager;
	 
	 public SatisfyingConditionFormController()
	 {
		  setCancelView("redirect:" + ViewName.satysfingConditions);
		  setSuccessView("redirect:" + ViewName.satysfingConditions);
	 }
	 
	 @ModelAttribute
	 private void addModelAttributes(Model model, HttpSession session)
	 {
		  Project project = this.projectManager.getCurrentProject(session);
		  List<AbstractMetric> availableMetrics = this.metricManager.findAllByProject(project);
		  model.addAttribute("availableMetrics", availableMetrics);
		  model.addAttribute("satisfyingOperations", new ArrayList<String>()); //need to add this attribute otherwise it is populated with enum fields
	 }
	 
	 @RequestMapping(method = RequestMethod.GET, value = ViewName.satysfingConditionForm)
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
	 
	 @RequestMapping(method = RequestMethod.POST, value = ViewName.satysfingConditionForm)
	 public String onSubmit(@ModelAttribute("satisfyingCondition") @Valid SatisfyingCondition satisfyingCondition, BindingResult errors, HttpServletRequest request, HttpServletResponse response, SessionStatus status, Model model) throws Exception
	 {
		  if (request.getParameter("cancel") != null)
		  {
				return getCancelView();
		  }
		  
		  if (errors.hasErrors() && request.getParameter("delete") == null)
			{
				 System.out.println(errors);
				 return ViewName.satysfingConditionForm;
			}
		  
		  Locale locale = request.getLocale();
		  
		  
		  return getSuccessView();
	 }
	 
	 @ResponseBody
	 @RequestMapping(method = RequestMethod.GET, value = ViewName.satysfingConditionFormAjax)
	 public String getAvailableTargetsAndOperations(HttpServletRequest request)
	 {
		  AbstractMetric metric = this.metricManager.get(new Long(request.getParameter("metricId")));
		  
		  Map<String, JSONArray> responseMap = new HashMap<String, JSONArray>();
		  
		  JSONArray targets = this.metricManager.getAvailableTargetsJSONized(metric);
		  responseMap.put("targets", targets);
		  
		  
		  //get the consistent satisfying operations according to metric's output type
		  JSONArray satisfyingOperations = new JSONArray();
		  if(metric.getOutputValueType() == MetricOutputValueTypeEnum.NUMERIC)
		  {
				satisfyingOperations.put(SatisfyingConditionOperationEnum.GREATER);
				satisfyingOperations.put(SatisfyingConditionOperationEnum.GREATER_OR_EQUAL);
				satisfyingOperations.put(SatisfyingConditionOperationEnum.LESS);
				satisfyingOperations.put(SatisfyingConditionOperationEnum.LESS_OR_EQUAL);
		  }
		  satisfyingOperations.put(SatisfyingConditionOperationEnum.EQUAL);
		  satisfyingOperations.put(SatisfyingConditionOperationEnum.NOT_EQUAL);
		  
		  responseMap.put("satisfyingOperations", satisfyingOperations);
		  
		  JSONObject response = new JSONObject(responseMap);
		  
		  System.out.println("Query result = " + response.toString());
		  
		  return response.toString();
		  
	 }

	 
	 
}
