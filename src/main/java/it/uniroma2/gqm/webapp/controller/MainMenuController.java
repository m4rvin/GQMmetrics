package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.ProjectManager;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;



@Controller
@RequestMapping("/mainMenu*")
public class MainMenuController {

    private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
    @ModelAttribute
	@RequestMapping(method = RequestMethod.GET)
	public Model handleRequest(Model model,HttpSession session) throws Exception {
        // set the default if necessary...
        Project currentProject = projectManager.getCurrentProject(session);
        
		List<Project> ret = projectManager.getAll();
		model.addAttribute("projects", ret);
		model.addAttribute("currentProject", currentProject);
		return model;
	}
}