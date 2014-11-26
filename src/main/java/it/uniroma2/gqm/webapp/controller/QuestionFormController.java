package it.uniroma2.gqm.webapp.controller;


import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
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
 


import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.GoalQuestionPK;
import it.uniroma2.gqm.model.GoalQuestionStatus;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.ProjectManager;
import it.uniroma2.gqm.service.QuestionManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
 
@Controller
@RequestMapping("/questionform*")
@SessionAttributes({"currentProject","question","currentUser"})
public class QuestionFormController extends BaseFormController {
	@Autowired
	private QuestionManager questionManager;

	@Autowired
	private GoalManager goalManager;
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
    
    public QuestionFormController() {
        setCancelView("redirect:questions");
        setSuccessView("redirect:questions");
    }
 
    @ModelAttribute
    @RequestMapping(method = RequestMethod.GET)
    protected Question showForm(HttpServletRequest request, HttpSession session, Model model) throws Exception {
        String id = request.getParameter("id");
        Question ret = null;
        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        if (!StringUtils.isBlank(id)) {
            ret = questionManager.get(new Long(id));
        } else {
        	ret = new Question();
        	ret.setProject(projectManager.get(currentProject.getId()));
        }
        model.addAttribute("currentProject",currentProject);
        model.addAttribute("currentUser",currentUser);
        model.addAttribute("availableGoals",makeAvailableGoals(ret,currentUser));
        return ret;
    }
 
    @RequestMapping(method = RequestMethod.POST)
    public String onSubmit(Question question, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response)
    throws Exception {
        if (request.getParameter("cancel") != null) {
            return getCancelView();
        }
        
        if (validator != null) { // validator is null during testing
            validator.validate(question, errors);
            if (errors.hasErrors() && request.getParameter("delete") == null) { // don't validate when deleting
                return "questionform";
            }
        }
 
        log.debug("entering 'onSubmit' method...");
 
        boolean isNew = (question.getId() == null);
        String success = getSuccessView();
        Locale locale = request.getLocale();
 
        if (request.getParameter("delete") != null) {
            questionManager.remove(question.getId());
            saveMessage(request, getText("question.deleted", locale));
        } else {
        	if(question.getQuestionOwner()==null)
        		question.setQuestionOwner(userManager.getUserByUsername(request.getRemoteUser()));

            questionManager.save(question);
            String key = (isNew) ? "question.added" : "question.updated";
            saveMessage(request, getText(key, locale));
 
        }
 
        return success;
    }
    
    private List<Goal> makeAvailableGoals(Question question, User currentUser){
    	List<Goal> ret = new ArrayList<Goal>(); 
    	for(Goal g:question.getProject().getGoals()){
    		if (g.getQSMembers().contains(currentUser) && g.isMG()){
    			ret.add(g);
    		}
    	}    	
    	return ret;
    }
    
    @InitBinder
    protected void initBinder(HttpServletRequest request, ServletRequestDataBinder binder) {
        binder.registerCustomEditor(Set.class, "goals", new CustomCollectionEditor(Set.class) {
        	
            protected Object convertElement(Object element) {
                if (element != null && StringUtils.isNotBlank((String)element)) {
                	String ids[] = ((String)element).split("\\|");
                    Goal goal = goalManager.get(new Long(ids[0]));
                    Question question = questionManager.get(new Long(ids[1]));
                    GoalQuestion goalQuestion = questionManager.getGoalQuestion(goal, question);                  
                    if(goalQuestion==null){
                    	goalQuestion = new GoalQuestion();
                    	goalQuestion.setPk(new GoalQuestionPK(goal,question));
                    	goalQuestion.setStatus(GoalQuestionStatus.PROPOSED);
                    }
                    return goalQuestion;
                }
                return null;
            }
        });
    }    

}