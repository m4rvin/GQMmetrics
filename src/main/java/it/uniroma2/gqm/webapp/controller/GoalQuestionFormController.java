package it.uniroma2.gqm.webapp.controller;

import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.service.GoalManager;
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
@RequestMapping("/goalquestionform*")
@SessionAttributes({"availableUsers","goal","question","currentUser"})
public class GoalQuestionFormController extends BaseFormController {
    @Autowired
    private GoalManager goalManager;
    @Autowired
    private QuestionManager questionManager;
    
    private UserManager userManager = null;

    private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    @Autowired
    public void setGoalManager(@Qualifier("goalManager") GoalManager goalManager) {
        this.goalManager = goalManager;
    }
     

    @Autowired
    public void setUserManager(UserManager userManager) {
        this.userManager = userManager;
    }
    
    public GoalQuestionFormController() {
        setCancelView("redirect:questions");
        setSuccessView("redirect:questions");
    }
    
    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected GoalQuestion showForm(HttpServletRequest request,HttpSession session, Model model)
    throws Exception {
    	
        String questionId = request.getParameter("questionId");
        String goalId = request.getParameter("goalId");
        GoalQuestion ret = null;
        

        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
       
        if (!StringUtils.isBlank(goalId) && !StringUtils.isBlank(questionId)) {
        	Goal goal = goalManager.get(new Long(goalId));
        	Question question = questionManager.get(new Long(questionId));
        	ret = questionManager.getGoalQuestion(goal, question);
        }else {
        	ret = new GoalQuestion();
        	//ret.setStatus(GoalStatus.DRAFT);
        	//ret.setGoalOwner(currentUser);
        	//ret.setProject(currentProject);
        }              
        
        List<String> availableStatus =questionManager.getAvailableStatus(ret, currentUser); 
        
        model.addAttribute("availableStatus",availableStatus);
        model.addAttribute("questionGoal",ret);
		model.addAttribute("currentUser",currentUser);
        return ret;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(GoalQuestion goalQuestion, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response) throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
 
        if (validator != null) { // validator is null during testing
            validator.validate(goalQuestion, errors); 
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
        	
        	goalQuestion.getPk().setGoal(goalManager.get(goalQuestion.getPk().getGoal().getId()));
        	goalQuestion.getPk().setQuestion(questionManager.get(goalQuestion.getPk().getQuestion().getId()));
        	questionManager.saveGoalQuestion(goalQuestion);

            String key = (isNew) ? "goalQuestion.added" : "goalQuestion.updated";
            saveMessage(request, getText(key, locale));

        }
 
        return getSuccessView();
    }

    
}
