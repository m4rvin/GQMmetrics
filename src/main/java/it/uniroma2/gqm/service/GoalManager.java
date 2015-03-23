package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

public interface GoalManager extends GenericManager<Goal, Long> { 
	public List<Goal> findByDescription(String description);
	public List<Goal> findByProject(Project project);
	
	public List<String> getAvailableStatus(Goal goal, User user);
	public Goal goalSplitting(Goal goal, int numberOfSplit);
	
	/**
	 * This method return goals associated with one or more measured value
	 * @param currentProject current project
	 * @return list of goal
	 */
	public List<Goal> getMeasuredGoal(Project currentProject);
	public List<AbstractMetric> getMeasuredMetricByGoal(Goal goal);
	public List<Goal> getOrganizationalGoal(Project currentProject);
	public boolean rootExists(Project currentProject);
	
	//public boolean contains(Goal g);	
}
