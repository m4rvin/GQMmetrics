package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;


@Controller
@RequestMapping("/questionmetricform*")
@SessionAttributes({"availableUsers","question","metric","currentUser"})

public class QuestionMetricFormController extends BaseFormController {

    @Autowired
    private ComplexMetricManager metricManager;
    
    @Autowired
    private QuestionManager questionManager;
    
    private UserManager userManager = null;

    private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public QuestionMetricFormController() {
        setCancelView("redirect:metrics");
        setSuccessView("redirect:metrics");
    }
    
    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected QuestionMetric showForm(HttpServletRequest request,HttpSession session, Model model)
    throws Exception {
    	
        String questionId = request.getParameter("questionId");
        String metricId = request.getParameter("metricId");
        QuestionMetric ret = null;
        
        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
       
        if (!StringUtils.isBlank(questionId) && !StringUtils.isBlank(metricId)) {
        	AbstractMetric metric = metricManager.get(new Long(metricId));
        	Question question = questionManager.get(new Long(questionId));
        	ret = metricManager.getQuestionMetric(metric, question);
        }else {
        	ret = new QuestionMetric();
        	ret.setStatus(QuestionMetricStatus.PROPOSED);
        }    
        
        // === GQM+S+MS changes
        //session.setAttribute("questionMetricStatus", ret.getStatus());
        // === END GQM+S+MS changes

        List<String> availableStatus =metricManager.getAvailableStatus(ret, currentUser); 
        
        model.addAttribute("availableStatus",availableStatus);
        model.addAttribute("questionGoal",ret);
		model.addAttribute("currentUser",currentUser);
        return ret;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(QuestionMetric questionMetric, BindingResult errors, HttpServletRequest request,
                           HttpSession session, HttpServletResponse response) throws Exception {
        if (request.getParameter("cancel") != null) {
            // === GQM+S+MS changes
            //session.removeAttribute("questionMetricStatus");
            // === END GQM+S+MS changes

            return getCancelView();
        }
 
        if (validator != null) { // validator is null during testing
            validator.validate(questionMetric, errors); 
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "goalquestionform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = false;
         Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {        	
            //goalManager.remove(goal.getId());
            saveMessage(request, getText("goal.deleted", locale));
        } else {
        	
        	questionMetric.getPk().setQuestion(questionManager.get(questionMetric.getPk().getQuestion().getId()));
        	AbstractMetric metric = metricManager.get(questionMetric.getPk().getMetric().getId());
        	questionMetric.getPk().setMetric(metric);
        	metricManager.saveQuestionMetric(questionMetric);

            String key = (isNew) ? "questionMetric.added" : "questionMetric.updated";
            saveMessage(request, getText(key, locale));

            // === GQM+S+MS changes
            /*if(metric.getClass().equals(CombinedMetric.class)){
	            QuestionMetricStatus qmStatus_saved = (QuestionMetricStatus) session.getAttribute("questionMetricStatus");
	            QuestionMetricStatus qmStatus_updated = questionMetric.getStatus();
	            if(!qmStatus_saved.equals(qmStatus_updated) && qmStatus_updated.equals(QuestionMetricStatus.APPROVED)){
	            	boolean dummytestvalue = FormulaHandler.evaluateFormula((CombinedMetric) metric, metricManager);
	            	System.out.println("A new combined metric has been proposed. It has been evaluated and its formula evaluation returned: " + dummytestvalue);
	            }
            }*/
        }
        
        //session.removeAttribute("questionMetricStatus");
        // === END GQM+S+MS changes

        return getSuccessView();
    }



}
