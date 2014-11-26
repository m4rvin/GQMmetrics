package it.uniroma2.gqm.service;
import javax.servlet.http.HttpSession;

import it.uniroma2.gqm.model.Project;

import org.appfuse.service.GenericManager;


public interface ProjectManager extends GenericManager<Project, Long> {
	/**
	 * Restituisce il progetto corrente nella sessione. Se non è associato alcun progetto alla sessione, viene recuperato quello
	 * di default con id uguale a -1
	 * @param session
	 * @return
	 */
	public Project getCurrentProject(HttpSession session);
}
