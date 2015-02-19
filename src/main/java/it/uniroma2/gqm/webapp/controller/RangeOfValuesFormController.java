package it.uniroma2.gqm.webapp.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.Metric;
import it.uniroma2.gqm.model.MetricTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.service.ProjectManager;

import org.apache.commons.lang.StringUtils;
import org.appfuse.model.User;
import org.appfuse.service.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/rangeOfValuesform*")
public class RangeOfValuesFormController extends BaseFormController{

	private UserManager userManager = null;
	private ProjectManager projectManager = null;
	
	
    public RangeOfValuesFormController() {
        setCancelView("redirect:rangeOfValues");
        setSuccessView("redirect:rangeOfValues");
    }
    
   @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
   
   @Autowired
   public void setUserManager(UserManager userManager) {
       this.userManager = userManager;
   }
	   
	@ModelAttribute
	@RequestMapping(method = RequestMethod.GET)
	protected RangeOfValues showForm(HttpServletRequest request,HttpSession session, Model model) throws Exception
	{

		//String id = request.getParameter("id");
		RangeOfValues rov = null;

        Project currentProject = projectManager.getCurrentProject(session);
        User currentUser = userManager.getUserByUsername(request.getRemoteUser());
        
        
    	rov = new RangeOfValues();
    	rov.setProject(currentProject);
    
        
        
        model.addAttribute("currentProject",currentProject);
        model.addAttribute("currentUser",currentUser);
        //model.addAttribute("units",unitManager.getAll());
        //model.addAttribute("scales",scaleManager.getAll());
        
        /*
        HashMap<Long, Set<Goal>> map = new HashMap<Long, Set<Goal>>();
		ArrayList<String> availablesTypes = new ArrayList<String>();
        availablesTypes.add(MetricTypeEnum.OBJECTIVE.toString());
        availablesTypes.add(MetricTypeEnum.SUBJECTIVE.toString());
        model.addAttribute("availablesTypes",availablesTypes);
        model.addAttribute("availableMetrics",metricManager.findByProject(currentProject));
        
        System.out.println("availableMetrics ------>" + metricManager.findByProject(currentProject));
        //model.addAttribute("availableGoals",makeAvailableGoals(rov,currentUser));
        model.addAttribute("availableQuestions", availableQuestions);
        model.addAttribute("map", map);
        */
        return rov;
		
	}

}
