package it.uniroma2.gqm.webapp.controller;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.service.ComplexMetricManager;
import it.uniroma2.gqm.service.ProjectManager;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/projectselection*")
public class ProjectSelectionController {
    @Autowired
    private ComplexMetricManager metricManager;

    private ProjectManager projectManager = null;
    
    @Autowired
    public void setProjectManager(@Qualifier("projectManager") ProjectManager projectManager) {
        this.projectManager = projectManager;
    }
    
	@RequestMapping(method = RequestMethod.POST)
	public String handleRequest(Project currentProject, HttpSession session) throws Exception {
		if(currentProject.getId() != null)
			currentProject = projectManager.get(currentProject.getId());
		session.setAttribute("currentProject", currentProject);
		return "redirect:mainMenu";
	}
}
