package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Aggregator;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MetricTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricPK;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;

@Controller
@SessionAttributes({ "currentProject", "simpleMetric", "currentUser", "scales", "availableMetrics", "measurementScales", "availableQuestions" })
public class SimpleMetricFormController extends BaseFormController
{
	 @Autowired
	 private ComplexMetricManager metricManager;
	 private MeasurementScaleManager measurementScaleManager = null;
	 private MetricValidator customValidator;
	 
	 @Autowired
	 private QuestionManager questionManager;
	 private UserManager userManager = null;
	 private ProjectManager projectManager = null;

	 @Autowired
	 public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager)
	 {
		  this.projectManager = projectManager;
	 }

	 @Autowired
	 public void setQuestionManager(@Qualifier("questionManager") QuestionManager questionManager)
	 {
		  this.questionManager = questionManager;
	 }

	 @Autowired
	 public void setUserManager(UserManager userManager)
	 {
		  this.userManager = userManager;
	 }

	 @Autowired
	 public void setMeasurementScaleManager(@Qualifier("measurementScaleManager") MeasurementScaleManager measurementScaleManager)
	 {
		  this.measurementScaleManager = measurementScaleManager;
	 }
	 
	 @Autowired
	 public void setCustomValidator(@Qualifier("metricValidator") MetricValidator validator)
	 {
		  this.customValidator = validator;
	 }


	 public SimpleMetricFormController()
	 {
		  setCancelView("redirect:" + ViewName.metrics);
		  setSuccessView("redirect:" + ViewName.metrics);
	 }

	 @ModelAttribute("simpleMetric")
	 @RequestMapping(value = ViewName.simpleMetricForm, method = RequestMethod.GET)
	 protected SimpleMetric showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {
		  String id = request.getParameter("id");
		  SimpleMetric metric = null;

		  Project currentProject = projectManager.getCurrentProject(session);

		  User currentUser = userManager.getUserByUsername(request.getRemoteUser());

		  if (!StringUtils.isBlank(id))
		  {
				metric = metricManager.findSimpleMetricById(new Long(id));
				model.addAttribute("used", !metric.isEresable());
				populateModel(model, metric);
		  } else
		  {
				metric = new SimpleMetric();
				metric.setProject(currentProject);
		  }

		  List<Question> availableQuestions = makeAvailableQuestions(metric, projectManager.get(currentProject.getId()), currentUser);
		  HashMap<Long, Set<Goal>> map = new HashMap<Long, Set<Goal>>();

		  // per ogni question, recupero il goal mg a cui sono è associata e
		  // aggiungo nella hashmap l'og associato (se mg non "orfano")
		  for (Question q : availableQuestions)
		  {
				Set<Goal> relatedOGs = new HashSet<Goal>();
				for (GoalQuestion gq : q.getGoals())
				{ // gli mg a cui la question è stata associata
					 Goal associatedOG = gq.getGoal().getAssociatedOG();

					 if (associatedOG != null)
					 {
						  relatedOGs.add(associatedOG);
					 }
				}
				if (relatedOGs.size() > 0)
					 map.put(q.getId(), relatedOGs);
		  }

		  model.addAttribute("currentProject", currentProject);
		  model.addAttribute("currentUser", currentUser);
		  model.addAttribute("measurementScales", this.measurementScaleManager.findByProject(currentProject));

		  ArrayList<String> availablesTypes = new ArrayList<String>();
		  availablesTypes.add(MetricTypeEnum.OBJECTIVE.toString());
		  availablesTypes.add(MetricTypeEnum.SUBJECTIVE.toString());
		  model.addAttribute("availablesTypes", availablesTypes);
		  // model.addAttribute("availableMetrics",metricManager.findAllByProject(currentProject));

		  // System.out.println("availableMetrics ------>" +
		  // metricManager.findAllByProject(currentProject));
		  // model.addAttribute("availableGoals",makeAvailableGoals(ret,currentUser));
		  model.addAttribute("availableQuestions", availableQuestions);
		  model.addAttribute("map", map);
		  return metric;
	 }
	 /**
	  * Return a JSONArray with the aggregators available with the selected measurement scale
	  * @param request 
	  * @return JSONArray response to String
	  */
	 @ResponseBody
	 @RequestMapping(value = ViewName.simpleMetricFormAjax, method = RequestMethod.GET)
	 public String getConsistentAggregators(HttpServletRequest request)
	 {
		  MeasurementScale measurementScale = this.measurementScaleManager.get(new Long(request.getParameter("measurementScaleId")));
		  RangeOfValues rov = measurementScale.getRangeOfValues();
		  
		  JSONArray ret = new JSONArray();
		  
		  String[] numericAggregators = {Aggregator.MAX_VALUE.toString(), Aggregator.MIN_VALUE.toString(), Aggregator.SUM.toString(), Aggregator.AVG.toString(), Aggregator.VARIANCE.toString()};
		  String[] nonNumericAggregators = {Aggregator.MAX_VALUE.toString(), Aggregator.MIN_VALUE.toString()};
		  
		  if(rov.isNumeric())
		  {
				for(String aggregator : numericAggregators)
					 ret.put(aggregator);
		  }
		  else
		  {
				for(String aggregator : nonNumericAggregators)
					 ret.put(aggregator);
		  }
		  
		  System.out.println(ret);
		  return ret.toString();
	 }

	 @RequestMapping(value = ViewName.simpleMetricForm, method = RequestMethod.POST)
	 public String onSubmit(@ModelAttribute("simpleMetric") @Valid SimpleMetric metric, BindingResult errors, HttpServletRequest request, HttpServletResponse response, SessionStatus status, Model model) throws Exception
	 {
		  if (request.getParameter("cancel") != null)
		  {
			  status.setComplete();
			  return getCancelView();
		  }

		  if (validator != null)
		  { // validator is null during testing
				validator.validate(metric, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 // don't validate when deleting
					 populateModel(model, metric);
					 return ViewName.simpleMetricForm;
				}
		  }

		  log.debug("entering 'onSubmit' method...");

		  boolean isNew = (metric.getId() == null);
		  String success = getSuccessView();
		  Locale locale = request.getLocale();

		  if (request.getParameter("delete") != null)
		  {
				metricManager.remove(metric.getId());
				saveMessage(request, getText("metric.deleted", locale));
		  } else
		  {
				if (metric.getMetricOwner() == null)
					 metric.setMetricOwner(userManager.getUserByUsername(request.getRemoteUser()));


				System.out.println("\n\n" + metric + "\n\n");
				
				try{
					metric = (SimpleMetric) this.metricManager.save(metric);
				}
				catch(DataIntegrityViolationException e){
					  System.err.println(e.getMessage());
					  if(e.getMessage().contains("name")){
						  model.addAttribute("duplicate_value", "A simple metric with the same name already exists. Please change the name and retry.");

					  }
					  else{
						  model.addAttribute("duplicate_value", "The simple metric already exists in the database. Change some parameter and retry.");
					  }
					  return ViewName.simpleMetricForm;
				}
				
				
				String key = (isNew) ? "metric.added" : "metric.updated";
				saveMessage(request, getText(key, locale));
		  }
		  status.setComplete();
		  return success;
	 }
	 
	 
	 @ResponseBody
	 @RequestMapping(value = ViewName.simpleMetricFormMeasurementScaleInfoAjax, method = RequestMethod.GET)
	 public String getMeasurementScaleInfo(HttpServletRequest request)
	 {
		  MeasurementScale measurementScale = this.measurementScaleManager.get(new Long(request.getParameter("measurementScaleId")));
		  String info = measurementScale.toHumanReadableDescription();

		  System.out.println("retrieved measurement scale info: " + info);
		  return info;
	 }
	 
	 @InitBinder(value="simpleMetric")
	 public void initBinder(WebDataBinder binder)
	 {
		  binder.setValidator(this.customValidator);
	 }


	 @InitBinder
	 protected void initBinder2(HttpServletRequest request, ServletRequestDataBinder binder)
	 {
		  binder.registerCustomEditor(MeasurementScale.class, "measurementScale", new MeasurementScaleEditorSupport());
	 }

	 /**
	  * The question available are all the question associated with the goal for
	  * which the user is MMDM
	  * 
	  * @param metric
	  *            current metric
	  * @param currentUser
	  *            current user
	  * @return list of available questions
	  */
	 private List<Question> makeAvailableQuestions(SimpleMetric metric, Project project, User currentUser)
	 {
		  List<Question> ret = new ArrayList<Question>();
		  // the question available are all the question associated with the goal
		  // for which the user is MMDM
		  for (Goal g : project.getGoals())
		  {
				System.out.println("goals: " + project.getGoals());
				System.out.println("g.getMMDMMembers(): " + g.getMMDMMembers());
				if (g.getMMDMMembers().contains(currentUser))
				{
					 System.out.println("g.getQuestions(): " + g.getQuestions());
					 for (GoalQuestion gq : g.getQuestions())
					 {
						  ret.add(gq.getQuestion());
					 }
				}
		  }

		  return ret;
	 }

	 @InitBinder
	 protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	 {
		  binder.registerCustomEditor(Set.class, "questions", new CustomCollectionEditor(Set.class)
		  {

				protected Object convertElement(Object element)
				{
					 System.out.println("Element: " + element);
					 if (element != null)
					 {
						  String ids[] = ((String) element).split("\\|");
						  Question question = questionManager.get(new Long(ids[0]));
						  SimpleMetric metric = metricManager.findSimpleMetricById(new Long(ids[1]));
						  QuestionMetric questionMetric = metricManager.getQuestionMetric(metric, question);
						  if (questionMetric == null)
						  {
								questionMetric = new QuestionMetric();
								questionMetric.setPk(new QuestionMetricPK(question, metric));
								questionMetric.setStatus(QuestionMetricStatus.PROPOSED);
						  }
						  return questionMetric;
					 }
					 return null;
				}
		  });
	 }

	 private class MeasurementScaleEditorSupport extends PropertyEditorSupport
	 {

		  @Override
		  public void setAsText(String text)
		  {
				if (text != null && !text.equals(""))
				{
					 MeasurementScale measurementScale = null;
					 try
					 {
						  measurementScale = measurementScaleManager.get(new Long(text));
						  setValue(measurementScale);
					 } catch (Exception e)
					 {
						  System.out.println(e);
						  setValue(null);
					 }
				} else
				{
					 System.out.println(" FIXME error in MeasurementScaleEditorSupport conversion");
					 setValue(null);
				}
				// FIXME error
		  }
	 }
	 
	 private void populateModel(Model model, SimpleMetric metric)
	 {
		  String[] numericAggregators = {Aggregator.MAX_VALUE.toString(), Aggregator.MIN_VALUE.toString(), Aggregator.SUM.toString(), Aggregator.AVG.toString(), Aggregator.VARIANCE.toString()};
		  String[] nonNumericAggregators = {Aggregator.MAX_VALUE.toString(), Aggregator.MIN_VALUE.toString()};
		  
		  List<String> availableAggregators = new ArrayList<String>();
		  MeasurementScale ms = metric.getMeasurementScale();
		  if(ms != null)
		  {
				if(ms.getRangeOfValues().isNumeric())
						availableAggregators.addAll(Arrays.asList(numericAggregators));
				  else
						availableAggregators.addAll(Arrays.asList(nonNumericAggregators));
		  }
		  
		  model.addAttribute("availableAggregators", availableAggregators);
	 }

}
