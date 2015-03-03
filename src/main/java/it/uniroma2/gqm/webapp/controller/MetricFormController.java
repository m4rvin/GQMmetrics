package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.MetricTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricPK;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.model.Scale;
import it.uniroma2.gqm.model.Unit;
import it.uniroma2.gqm.service.MeasurementScaleManager;
import it.uniroma2.gqm.service.MetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.propertyeditors.CustomCollectionEditor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/metricform*")
@SessionAttributes({"currentProject","metric","currentUser","units","scales","availableMetrics"})
public class MetricFormController  extends BaseFormController {
	@Autowired
	private MetricManager metricManager;
    private GenericManager<Unit, Long> unitManager = null;
    private MeasurementScaleManager measurementScaleManager = null;

	@Autowired
	private QuestionManager questionManager;
	private UserManager userManager = null;
	private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    @Autowired
    public void setQuestionManager(@Qualifier("questionManager") QuestionManager questionManager) {
        this.questionManager = questionManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    @Autowired
    public void setUnitManager(@Qualifier("unitManager") GenericManager<Unit, Long> unitManager) {
        this.unitManager = unitManager;
    }
        
    @Autowired
    public void setMeasurementScaleManager(@Qualifier("measurementScaleManager") MeasurementScaleManager measurementScaleManager) {
        this.measurementScaleManager = measurementScaleManager;
    }

    public MetricFormController() {
        setCancelView("redirect:metrics");
        setSuccessView("redirect:metrics");
    }

    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected Metric showForm(HttpServletRequest request,HttpSession session, Model model) throws Exception {
        String id = request.getParameter("id");
        Metric ret = null;

        Project currentProject = projectManager.getCurrentProject(session);
        
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        if (!StringUtils.isBlank(id)) {
            ret = metricManager.get(new Long(id));
        } else {
        	ret = new Metric();
        	ret.setProject(currentProject);
        }
        
        List<Question> availableQuestions = makeAvailableQuestions(ret,projectManager.get(currentProject.getId()),currentUser);
        HashMap<Long, Set<Goal>> map = new HashMap<Long, Set<Goal>>();
        
        //per ogni question, recupero il goal mg a cui sono è associata e aggiungo nella hashmap l'og associato (se mg non "orfano")
        for (Question q : availableQuestions) {
        	Set<Goal> relatedOGs = new HashSet<Goal>();
        	for (GoalQuestion gq : q.getGoals()) { //gli mg a cui la question è stata associata
        		Goal associatedOG = gq.getGoal().getAssociatedOG();
        		
        		if(associatedOG != null) {
        			relatedOGs.add(associatedOG);
        		}
			}
        	if(relatedOGs.size() > 0)
        		map.put(q.getId(), relatedOGs);
		}
        
        model.addAttribute("currentProject",currentProject);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("units",unitManager.getAll());
        model.addAttribute("measurementScales", this.measurementScaleManager.findByProject(currentProject));
        
        ArrayList<String> availablesTypes = new ArrayList<String>();
        availablesTypes.add(MetricTypeEnum.OBJECTIVE.toString());
        availablesTypes.add(MetricTypeEnum.SUBJECTIVE.toString());
        model.addAttribute("availablesTypes",availablesTypes);
        model.addAttribute("availableMetrics",metricManager.findByProject(currentProject));
        
        System.out.println("availableMetrics ------>" + metricManager.findByProject(currentProject));
        //model.addAttribute("availableGoals",makeAvailableGoals(ret,currentUser));
        model.addAttribute("availableQuestions", availableQuestions);
        model.addAttribute("map", map);
        return ret;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Metric metric, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
        
                
        if (validator != null) { // validator is null during testing
            validator.validate(metric, errors);
            if (errors.hasErrors() && request.getParameter("delete") == null) {
            	// don't validate when deleting
                return "metricform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (metric.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {
           metricManager.remove(metric.getId());
            saveMessage(request, getText("metric.deleted", locale));
        } else {
        	if(metric.getMetricOwner()==null)
        		metric.setMetricOwner(userManager.getUserByUsername(request.getRemoteUser()));
            
        	
        	if(metric.getScale() != null && metric.getScale().getId() != null){
            	metric.setScale(scaleManager.get(metric.getScale().getId()));
            	
            } else {
            	metric.setScale(null);
            }
        	if(metric.getUnit() != null && metric.getUnit().getId() != null){
        		metric.setUnit(unitManager.get(metric.getUnit().getId()));
        	} else {
        		metric.setUnit(null);
            }
        	metric.setMetricA(metric.getMetricA()!=null && metric.getMetricA().getId() != null ? 
        				metricManager.get(metric.getMetricA().getId()) 
        				: null);
        	metric.setMetricB(metric.getMetricB()!=null && metric.getMetricB().getId() != null ? 
    				metricManager.get(metric.getMetricB().getId()) 
    				: null);
        	metricManager.save(metric);
            String key = (isNew) ? "metric.added" : "metric.updated";
            saveMessage(request, getText(key, locale));
        }
        return success;
    }

    @InitBinder
    protected void initBinder1(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(List.class, "unit", new CustomCollectionEditor(List.class) {
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    Unit u = unitManager.get(id);
                    return u;
                }
                return null;
            }
        });
    }    
   
    @InitBinder
    protected void initBinder2(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Set.class, "scale", new CustomCollectionEditor(Set.class) {
            protected Object convertElement(Object element) {
                if (element != null) {
                    Long id = new Long((String)element);
                    Scale s = scaleManager.get(id);
                    return s;
                }
                return null;
            }
        });
    }

    /**
     * The question available are all the question associated with the goal for which the user is MMDM
     * @param metric current metric
     * @param currentUser current user 
     * @return list of available questions
     */
    private List<Question> makeAvailableQuestions(Metric metric, Project project,User currentUser){
    	List<Question> ret = new ArrayList<Question>();
    	//the question available are all the question associated with the goal for which the user is MMDM
    	for(Goal g:project.getGoals()){
    		System.out.println("goals: " + project.getGoals());
    		System.out.println("g.getMMDMMembers(): " + g.getMMDMMembers());
    		if(g.getMMDMMembers().contains(currentUser)){
    			System.out.println("g.getQuestions(): " + g.getQuestions());
    			for(GoalQuestion gq:g.getQuestions()){
    				ret.add(gq.getQuestion());
    			}
    		}
    	}

    	return ret;
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Set.class, "questions", new CustomCollectionEditor(Set.class) {
        	
            protected Object convertElement(Object element) {
            	System.out.println("Element: " + element);
                if (element != null) {
                	String ids[] = ((String)element).split("\\|");
                	Question question = questionManager.get(new Long(ids[0]));
                    Metric metric = metricManager.get(new Long(ids[1]));
                    QuestionMetric questionMetric = metricManager.getQuestionMetric(metric, question);                  
                    if(questionMetric==null){
                    	questionMetric = new QuestionMetric();
                    	questionMetric.setPk(new QuestionMetricPK(question,metric));
                    	questionMetric.setStatus(QuestionMetricStatus.PROPOSED);
                    }
                    return questionMetric;
                }
                return null;
            }
        });
    }      
}
