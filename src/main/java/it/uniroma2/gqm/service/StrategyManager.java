package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Strategy;

import java.util.List;

import org.appfuse.service.GenericManager;

public interface StrategyManager  extends GenericManager<Strategy, Long> {
	public List<Strategy> findByProject(Project project);
	//public boolean hasChildren(Strategy s);
	//public boolean hasParent(Strategy s);
}
