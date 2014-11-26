package it.uniroma2.gqm.service;

import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.dao.ProjectDao;
import it.uniroma2.gqm.model.Project;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("projectManager")
public class ProjectManagerImpl extends GenericManagerImpl<Project, Long> implements ProjectManager {

	private ProjectDao projectDao = null;
	
	@Autowired
	public ProjectManagerImpl(ProjectDao projectDao) {
		super(projectDao);
		this.projectDao = projectDao;
	}
	
	@Override
	public Project getCurrentProject(HttpSession session) {
        Project currentProject = (Project) session.getAttribute("currentProject");
        
        if(currentProject == null) {
        	currentProject = get(new Long(-1));
        	session.setAttribute("currentProject", currentProject);
        }
        
        return currentProject;
	}

}
