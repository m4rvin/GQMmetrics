package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;

import org.appfuse.service.GenericManager;
import org.json.JSONObject;

public interface GridManager extends GenericManager<Goal, Long> {

    /**
     * Verifica che grandChild sia nipote (o figlio) di grandParent 
     * @param grandChild Il possibile nipote
     * @param grandParent Il possibile nonno
     * @return true nel caso in cui grandChild sia nipote di grandParent
     */
	public boolean isGrandChild(Object grandChild, Object grandParent);
	/**
	 * Recupera l'OG root del progetto
	 * @param project Il progetto di cui recuperare l'OG root
	 * @return Un oggetto Goal rappresentante l'OG root, nullo in caso non esistano goal OG nel progetto
	 */
	public Goal getOGRoot(Project project);
	public String explorer(Object obj, String s);
	public JSONObject saveTreeToJSON(String s);
}
