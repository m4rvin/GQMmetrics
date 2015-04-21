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
import org.appfuse.service.GenericManager;
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
@SessionAttributes({"satisfyingCondition", "currentProject", "currentUser", "currentMetric"})
public class SatisfyingConditionFormController extends BaseFormController
{

	 @Autowired
	 SatisfyingConditionManager satisyfingConditionManager;
	 @Autowired
	 ComplexMetricManager metricManager;
	 @Autowired
	 ProjectManager projectManager;
	 @Autowired
	 GenericManager<SatisfyingConditionTarget, Long> satisfyingConditionTargetManager;
	 
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
		  model.addAttribute("satisfyingOperations", new ArrayList<String>()); //need to add this attribute otherwise it is populated with all enum fields
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
				
				for(SatisfyingConditionTarget target : satisfyingCondition.getTargets()) //representation is a transient field, it is needed to update it when retriving a condtion from db
					 target.retriveRepresentation();
				
				AbstractMetric metric = satisfyingCondition.getTargets().iterator().next().getMetric(); //There is always the same metric in all the target objects
				populateModel(model, metric, true);
				session.setAttribute("currentMetric", satisfyingCondition.getTargets().iterator().next().getMetric().getId());
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
		  
		  Locale locale = request.getLocale();
		  if (request.getParameter("cancel") != null)
				return getCancelView();
		  
		  String metric_id = request.getParameter("metric");
		  AbstractMetric metric;
		  
		  if (errors.hasErrors() && request.getParameter("delete") == null)
		  {
				 System.out.println(errors);
				 if(metric_id != "") //need to populate model only if a metric has been set
				 {
					  metric = this.metricManager.get(new Long(metric_id));
					  
					  if(satisfyingCondition.getId() == null)
							  populateModel(model, metric, false);
					  else
							  populateModel(model, metric, true);
				 }
				 return ViewName.satysfingConditionForm;
		  }
		  
		  if (request.getParameter("delete") != null)
		  {
				this.satisyfingConditionManager.remove(satisfyingCondition.getId());
				saveMessage(request, getText("satisfyingCondition.deleted", locale));
				
		  } else
		  {
			  //at this step metric_id is a valid id
			  metric = this.metricManager.get(new Long(metric_id));
		  
   		  if(satisfyingCondition.getSatisfyingConditionOwner() == null) //set the current user. Needed for enabling the edit of a satisfying condition
   				satisfyingCondition.setSatisfyingConditionOwner(getUserManager().getUserByUsername(request.getRemoteUser()));
   		  
   		 //either new object or updated one when targets are changed
   		  if(satisfyingCondition.getTargets() != null)
   		  {
   				for(SatisfyingConditionTarget target : satisfyingCondition.getTargets())
	   		   {
	   				 target = this.satisyfingConditionManager.updateTargetByRepresentation(target); 
	   				 target.setProject(satisfyingCondition.getProject());
	   				 target.setSatisfyingCondition(satisfyingCondition);   				
	   		   }
   	   		  
   		  }
   		  // targets == null means that we are in an update case where targets are not changed, so do nothing
   		  satisfyingCondition = this.satisyfingConditionManager.save(satisfyingCondition); //save the entity and the children
   		  saveMessage(request, getText("satisfyingCondition.created", locale));
		  }
		  status.setComplete();
		  return getSuccessView();
	 }
	 
	 @ResponseBody
	 @RequestMapping(method = RequestMethod.GET, value = ViewName.satysfingConditionFormAjax)
	 public String getAvailableTargetsAndOperations(HttpServletRequest request, HttpSession session)
	 {
		  Long session_metric_id = (Long) session.getAttribute("currentMetric");
		  
		  AbstractMetric metric = this.metricManager.get(new Long(request.getParameter("metricId")));
		  
		  boolean editing = new Boolean(request.getParameter("editing"));
		  
		  Map<String, JSONArray> responseMap = new HashMap<String, JSONArray>();
		  
		  JSONArray targets;
		  
		  if(editing)// in editing I want all the metric just for the old condition's one, otherwise apply normal query which filters on existing target
		  {
				if(session_metric_id == metric.getId())
					 targets = this.satisyfingConditionManager.findTargetByMetricWhenEditingJSONized(metric);
				else
					 targets = this.satisyfingConditionManager.findTargetByMetricJSONized(metric);
		  }
		  else
				targets = this.satisyfingConditionManager.findTargetByMetricJSONized(metric);
		  responseMap.put("targets", targets);
		  
		  //get the consistent satisfying operations according to metric's output type
		  JSONArray satisfyingOperations = new JSONArray();
		  
		  SatisfyingConditionOperationEnum[] allOperations = SatisfyingConditionOperationEnum.values();
		  for(SatisfyingConditionOperationEnum operation : allOperations)
		  {
				if(metric.getOutputValueType() == MetricOutputValueTypeEnum.BOOLEAN)
				{
					 if(operation.isOnlyBoolean())
						  satisfyingOperations.put(operation.toString());
				}
				else
					 satisfyingOperations.put(operation.toString());
		  }
		  
		  responseMap.put("satisfyingOperations", satisfyingOperations);
		  
		  JSONObject response = new JSONObject(responseMap);
		  
		  System.out.println("Query result = " + response.toString());
		  
		  return response.toString();
		  
	 }
	 
	 /**
	  * Method for converting the strings representing the target objects
	  * @param binder
	  */
	 @InitBinder(value = "satisfyingCondition")
	 public void initBinder(WebDataBinder binder)
	 {
		  binder.registerCustomEditor(Set.class, "targets", new CustomCollectionEditor(Set.class)
		  {
				@Override
				protected Object convertElement(Object element) 
				{
					 String e = (String) element;
					 SatisfyingConditionTarget target = new SatisfyingConditionTarget(); //instead of initializing all the objects fields (which is expensive) initialize only the representation field
					 target.setRepresentation(e);
					 return target;
				}
		  });
	 }
	 
	 /**
	  * Function for populate the view in error or editing cases
	  * @param model
	  * @param metric
	  */
	 private void populateModel(Model model, AbstractMetric metric, boolean editing)
	 {
		  List<String> targets;
		  if(editing)
				targets = this.satisyfingConditionManager.findTargetByMetricWhenEditing(metric);
		  else
				targets = this.satisyfingConditionManager.findTargetByMetric(metric);
		  
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
		  model.addAttribute("selectedMetric", metric.getId());
	 }
}
