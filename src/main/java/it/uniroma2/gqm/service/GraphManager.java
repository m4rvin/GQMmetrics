package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.BinaryTable;
import it.uniroma2.gqm.model.Goal;

import org.appfuse.service.GenericManager;

public interface GraphManager extends GenericManager<BinaryTable, Long> {
	
	public String getGraph(Goal g);

}
