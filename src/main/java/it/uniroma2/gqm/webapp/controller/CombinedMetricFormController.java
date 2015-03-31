package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.MetricTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricPK;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.model.Unit;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;
import it.uniroma2.gqm.webapp.jsp.ViewName;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
import org.appfuse.service.UserManager;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
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
@SessionAttributes({ "currentProject", "combinedMetric", "currentUser", "units", "scales", "measurementScales" })
public class CombinedMetricFormController extends BaseFormController
{

	 @Autowired
	 private ComplexMetricManager metricManager;
	 private GenericManager<Unit, Long> unitManager = null;
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
	 public void setUnitManager(@Qualifier("unitManager") GenericManager<Unit, Long> unitManager)
	 {
		  this.unitManager = unitManager;
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

	 public CombinedMetricFormController()
	 {
		  setCancelView("redirect:" + ViewName.metrics);
		  setSuccessView("redirect:" + ViewName.metrics);
	 }

	 @ModelAttribute("combinedMetric")
	 @RequestMapping(value = ViewName.combinedMetricForm, method = RequestMethod.GET)
	 protected CombinedMetric showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception
	 {
		  String id = request.getParameter("id");
		  CombinedMetric metric = null;

		  Project currentProject = projectManager.getCurrentProject(session);

		  User currentUser = userManager.getUserByUsername(request.getRemoteUser());

		  if (!StringUtils.isBlank(id))
		  {
			  	model.addAttribute("metricInEditingId", id);
				metric = metricManager.findCombinedMetricById(new Long(id));
				model.addAttribute("used", !metric.isEresable());
				MeasurementScale measurementScale = metric.getMeasurementScale();
				if (measurementScale != null && measurementScale.getType() != null)
					 populateModel(model, measurementScale,  metric.getId());
		  } else
		  {
				metric = new CombinedMetric();
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
		  model.addAttribute("units", unitManager.getAll());
		  model.addAttribute("measurementScales", this.measurementScaleManager.findByProject(currentProject));

		  ArrayList<String> availablesTypes = new ArrayList<String>();
		  availablesTypes.add(MetricTypeEnum.OBJECTIVE.toString());
		  availablesTypes.add(MetricTypeEnum.SUBJECTIVE.toString());
		  model.addAttribute("availablesTypes", availablesTypes);

		  // model.addAttribute("availableMetrics",metricManager.findCombinedMetricByProject(currentProject));
		  // System.out.println("availableMetrics ------>" +
		  // metricManager.findCombinedMetricByProject(currentProject));
		  // model.addAttribute("availableGoals",makeAvailableGoals(ret,currentUser));
		  model.addAttribute("availableQuestions", availableQuestions);
		  model.addAttribute("map", map);
		  return metric;
	 }

	 /**
	  * Retrieve the metrics that are compatible with the combined metric being
	  * created.
	  * 
	  * @param request
	  * @return
	  */
	 @ResponseBody
	 @RequestMapping(value = ViewName.combinedMetricFormAjax, method = RequestMethod.GET)
	 public String getConsistentMetrics(HttpServletRequest request)
	 {
		  MeasurementScale measurementScale = this.measurementScaleManager.get(new Long(request.getParameter("measurementScaleId")));
		  Long metricId = (request.getParameter("metricToExcludeId")!= "" ? new Long(request.getParameter("metricToExcludeId")) : null);
		  List<AbstractMetric> metricsSet;
		  if(metricId == null)
			  metricsSet = this.metricManager.findMetricByMeasurementScaleType(measurementScale.getType());
		  else
			  metricsSet = this.metricManager.findMetricByMeasurementScaleTypeExludingOneById(measurementScale.getType(), metricId);
		  
		  metricsSet = this.filterByRangeOfValues(measurementScale, metricsSet);
		  
		  JSONArray ret = new JSONArray();
		  for(AbstractMetric m : metricsSet)
				ret.put(m.getName());
		  
		  System.out.println("Query result of combinedMetricFormAjax: " + ret.toString());
		  return ret.toString();
	 }

	 @RequestMapping(value = ViewName.combinedMetricForm, method = RequestMethod.POST)
	 public String onSubmit(@ModelAttribute("combinedMetric") @Valid CombinedMetric metric, BindingResult errors, HttpServletRequest request, HttpServletResponse response, SessionStatus status, Model model) throws Exception
	 {
		  if (request.getParameter("cancel") != null)
		  {
				return getCancelView();
		  }

		  if (validator != null)
		  { // validator is null during testing
				validator.validate(metric, errors);
				if (errors.hasErrors() && request.getParameter("delete") == null)
				{
					 // don't validate when deleting
					 System.out.println(errors);
					 System.out.println(metric);
					 MeasurementScale measurementScale = metric.getMeasurementScale();
					 if (measurementScale != null && measurementScale.getType() != null)
						  populateModel(model, measurementScale, metric.getId());
					 return ViewName.combinedMetricForm;
				}
		  }

		  log.debug("entering 'onSubmit' method...");

		  boolean isNew = (metric.getId() == null);
		  String success = getSuccessView();
		  Locale locale = request.getLocale();

		  if (request.getParameter("delete") != null)
		  {
				for (AbstractMetric m : metric.getComposedBy())
					 m.removeComposerFor(metric);
				metricManager.remove(metric.getId());
				saveMessage(request, getText("metric.deleted", locale));
		  } else
		  {
				
				if(metric.getComposedBy().size() > 0) //== not isNew --->  during editing I want to delete the old references to the composer metrics
				{
					 for(AbstractMetric m : metric.getComposedBy())
						  metric.removeComposedBy(m);
				}
				
				
				if (metric.getMetricOwner() == null)
					 metric.setMetricOwner(userManager.getUserByUsername(request.getRemoteUser()));


				/*
				 * TEST per combinedMetric relationship Set<AbstractMetric> mSet =
				 * new
				 * HashSet<AbstractMetric>(this.metricManager.findSimpleMetricByProject
				 * (metric.getProject()));
				 * mSet.addAll(this.metricManager.findCombinedMetricByProject
				 * (metric.getProject()));
				 * 
				 * for(AbstractMetric m : mSet) { metric.addComposedBy(m); }
				 */
				populateModel(model, metric.getMeasurementScale(), metric.getId());
				
				Set<String> composedByMetricNames = MetricValidator.getUsedMetrics(metric.getFormula());

				List<AbstractMetric> availableMetrics = (ArrayList<AbstractMetric>) model.asMap().get("availableMetricComposers");

				for (String metricName : composedByMetricNames)
				{
					 boolean found = false;
					 metricName = metricName.replaceAll("_", "");
					 for (AbstractMetric m : availableMetrics)
					 {
						  if (m.getName().equals(metricName))
						  {
								metric.addComposedBy(m);
								found = true;
								break;
						  }
					 }
					 if (!found)
					 {
						  errors.rejectValue("formula", "formula", "Usage of not valid metric");
						//  populateModel(model, metric.getMeasurementScale().getType());
						  return ViewName.combinedMetricForm;
					 }
				}

				System.out.println("\n\n" + metric + "\n\n");

				metricManager.save(metric);

				String key = (isNew) ? "metric.added" : "metric.updated";
				saveMessage(request, getText(key, locale));
		  }
		  status.setComplete();
		  return success;
	 }

	 @InitBinder(value = "combinedMetric")
	 public void initBinder(WebDataBinder binder)
	 {
		  binder.setValidator(this.customValidator);
	 }

	 @InitBinder
	 protected void initUnitBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	 {
		  binder.registerCustomEditor(List.class, "unit", new CustomCollectionEditor(List.class)
		  {
				protected Object convertElement(Object element)
				{
					 if (element != null)
					 {
						  Long id = new Long((String) element);
						  Unit u = unitManager.get(id);
						  return u;
					 }
					 return null;
				}
		  });
	 }

	 @InitBinder
	 protected void initMeasurementScaleBinder(HttpServletRequest request, ServletRequestDataBinder binder)
	 {
		  binder.registerCustomEditor(MeasurementScale.class, "measurementScale", new MeasurementScaleEditorSupport());
	 }

	 @InitBinder
	 protected void initQuestionBinder(HttpServletRequest request, ServletRequestDataBinder binder)
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
						  CombinedMetric metric = metricManager.findCombinedMetricById(new Long(ids[1]));
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
	 private List<Question> makeAvailableQuestions(CombinedMetric metric, Project project, User currentUser)
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

	 private void populateModel(Model model, MeasurementScale measurementScale, Long metricId)
	 {
		  List<AbstractMetric> availableMetricComposers;
		  if(metricId != null)
			  availableMetricComposers = this.metricManager.findMetricByMeasurementScaleTypeExludingOneById(measurementScale.getType(), metricId);
		  else
			  availableMetricComposers = this.metricManager.findMetricByMeasurementScaleType(measurementScale.getType());
		  availableMetricComposers = this.filterByRangeOfValues(measurementScale, availableMetricComposers);
		  model.addAttribute("availableMetricComposers", availableMetricComposers);
	 }
	 
	 private List<AbstractMetric> filterByRangeOfValues(MeasurementScale currentMeasurementScale, List<AbstractMetric> rawList)
	 {
		  Iterator<AbstractMetric> it = rawList.iterator();
		  while(it.hasNext())
		  {
				AbstractMetric m = it.next();
				if(!m.getMeasurementScale().getRangeOfValues().isIncluded(currentMeasurementScale.getRangeOfValues()))
					 it.remove();
		  }
		  return rawList;
	 }
}
