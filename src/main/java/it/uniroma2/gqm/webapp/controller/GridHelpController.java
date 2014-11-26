package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.service.GoalManager;
import it.uniroma2.gqm.service.GridManager;
import it.uniroma2.gqm.service.ProjectManager;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/gridhelp*")
@SessionAttributes({"currentProject","goal","currentUser"})
public class GridHelpController extends BaseFormController{
	
	@Autowired
    private GoalManager goalManager;
    
    private GridManager gridManager;
    
    private ProjectManager projectManager;
    
    @Autowired
    public void setGridManager(@Qualifier("gridManager") GridManager gridManager) {
    	this.gridManager = gridManager;
    }

    @Autowired
    public void setGoalManager(@Qualifier("goalManager") GoalManager goalManager) {
        this.goalManager = goalManager;
    }
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    @RequestMapping(method = RequestMethod.POST)
    public ModelAndView onSubmit(@Valid Goal goal, BindingResult errors, HttpServletRequest request,
                           HttpServletResponse response, HttpSession session) throws Exception {

    	//Goal ret = goalManager.get(Long.parseLong("1")); //TODO inserire ROOT
    	Goal ret = gridManager.getOGRoot(projectManager.getCurrentProject(session));
        String tree = gridManager.explorer(ret, "null");
		
        JSONObject jsonTree = new JSONObject(tree);
        
        return new ModelAndView().addObject("tree", (JSONObject)jsonTree);
    }

}
