package it.uniroma2.gqm.webapp.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.QuestionMetric;
import it.uniroma2.gqm.model.QuestionMetricStatus;
import it.uniroma2.gqm.service.MetricManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.GenericManager;
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
    private MetricManager metricManager;
    
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
        	Metric metric = metricManager.get(new Long(metricId));
        	Question question = questionManager.get(new Long(questionId));
        	ret = metricManager.getQuestionMetric(metric, question);
        }else {
        	ret = new QuestionMetric();
        	ret.setStatus(QuestionMetricStatus.PROPOSED);
        }              
        
        List<String> availableStatus =metricManager.getAvailableStatus(ret, currentUser); 
        
        model.addAttribute("availableStatus",availableStatus);
        model.addAttribute("questionGoal",ret);
		model.addAttribute("currentUser",currentUser);
        return ret;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(QuestionMetric questionMetric, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        if (request.getParameter("cancel") != null) {
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
        	questionMetric.getPk().setMetric(metricManager.get(questionMetric.getPk().getMetric().getId()));
        	metricManager.saveQuestionMetric(questionMetric);

            String key = (isNew) ? "questionMetric.added" : "questionMetric.updated";
            saveMessage(request, getText(key, locale));

        }
 
        return getSuccessView();
    }



}
