package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.MetricOutputValueTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionOperationEnum;
import it.uniroma2.gqm.model.SatisfyingConditionTarget;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.SatisfyingConditionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({"satisfyingCondition", "currentProject", "currentUser"})
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
		  User currentUser = this.getUserManager().getUserByUsername(request.getRemoteUser());

		  if (!StringUtils.isBlank(id))
		  {
				satisfyingCondition = this.satisyfingConditionManager.get(new Long(id));
				//manca la parte di recupero della metrica e inserimento sul modello
		  }
		  else
		  {
				satisfyingCondition = new SatisfyingCondition();
				satisfyingCondition.setProject(currentProject);
		  }
		  
		  model.addAttribute("currentProject", currentProject);
		  model.addAttribute("satisfyingCondition", satisfyingCondition);
		  model.addAttribute("currentUser", currentUser);
		  
		  return ViewName.satysfingConditionForm;
	 }
	 
	 @RequestMapping(method = RequestMethod.POST, value = ViewName.satysfingConditionForm)
	 public String onSubmit(@ModelAttribute("satisfyingCondition") @Valid SatisfyingCondition satisfyingCondition, BindingResult errors, HttpServletRequest request, HttpServletResponse response, SessionStatus status, Model model) throws Exception
	 {
		  String metric_id = request.getParameter("metric");
		  Locale locale = request.getLocale();
		  if (request.getParameter("cancel") != null)
				return getCancelView();

		  if (errors.hasErrors() && request.getParameter("delete") == null)
		  {
				 System.out.println(errors);
				 populateModel(model, metric_id);
				 return ViewName.satysfingConditionForm;
		  }
		  
		  if (request.getParameter("delete") != null)
		  {
				this.satisyfingConditionManager.remove(satisfyingCondition.getId());
				saveMessage(request, getText("satisfyingCondition.deleted", locale));
		  } else
		  {
		  
   		  if(satisfyingCondition.getSatisfyingConditionOwner() == null)
   				satisfyingCondition.setSatisfyingConditionOwner(getUserManager().getUserByUsername(request.getRemoteUser()));
   		  
   		  for(SatisfyingConditionTarget target : satisfyingCondition.getTargets())
   		  {
   				target = this.satisyfingConditionManager.updateTargetByRepresentation(target);
   		  }
   		  
   		  this.satisyfingConditionManager.save(satisfyingCondition);
		  }
		  
		  return getSuccessView();
	 }
	 
	 @ResponseBody
	 @RequestMapping(method = RequestMethod.GET, value = ViewName.satysfingConditionFormAjax)
	 public String getAvailableTargetsAndOperations(HttpServletRequest request)
	 {
		  AbstractMetric metric = this.metricManager.get(new Long(request.getParameter("metricId")));
		  
		  Map<String, JSONArray> responseMap = new HashMap<String, JSONArray>();
		  
		  JSONArray targets = this.satisyfingConditionManager.findTargetByMetricJSONized(metric);
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
	 
	 @InitBinder(value = "satisfyingCondition")
	 public void initBinder(WebDataBinder binder)
	 {
		  binder.registerCustomEditor(Set.class, "targets", new CustomCollectionEditor(Set.class)
		  {
				@Override
				protected Object convertElement(Object element) 
				{
					 String e = (String) element;
					 SatisfyingConditionTarget target = new SatisfyingConditionTarget();
					 target.setRepresentation(e);
					 return target;
				}
		  });
	 }
	 
	 private void populateModel(Model model, String metric_id)
	 {
		  AbstractMetric metric = this.metricManager.get(new Long(metric_id));
		  List<String> targets = this.satisyfingConditionManager.findTargetByMetric(metric);
		  
		  List<String> satisfyingOperations = new ArrayList<String>();
		  SatisfyingConditionOperationEnum[] allOperations = SatisfyingConditionOperationEnum.values();
		  for(SatisfyingConditionOperationEnum operation : allOperations)
		  {
				if(metric.getOutputValueType() == MetricOutputValueTypeEnum.BOOLEAN)
				{
					 if(operation.isOnlyBoolean())
						  satisfyingOperations.add(operation.toString());
				}
				else
					 satisfyingOperations.add(operation.toString());
		  }
		  
		  model.addAttribute("satisfyingOperations", satisfyingOperations);
		  model.addAttribute("availableTargets", targets);
		  model.addAttribute("selectedMetric", metric_id);
	 }
}
