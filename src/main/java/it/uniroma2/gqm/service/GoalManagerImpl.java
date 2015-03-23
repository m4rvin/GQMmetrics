package it.uniroma2.gqm.service;


import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma2.gqm.dao.*;
import it.uniroma2.gqm.model.*;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
 
@Service("goalManager")
public class GoalManagerImpl extends GenericManagerImpl<Goal, Long> implements GoalManager {
    GoalDao goalDao;
    SimpleMetricDao metricDao;
    
    @Autowired
    public GoalManagerImpl(GoalDao goalDao,SimpleMetricDao metricDao) {
        super(goalDao);
        this.goalDao = goalDao;
        this.metricDao = metricDao;
    }
 
    public List<Goal> findByDescription(String description) {
        return goalDao.findByDescription(description);
    }

    public List<String> getAvailableStatus(Goal goal, User user){
		List<String> ret = new ArrayList<String>();
		switch (goal.getStatus()) {
			case DRAFT:
				ret.add(GoalStatus.DRAFT.toString());				
				if(goal.getGoalOwner().equals(user))			// only GO can propose a goal...
					ret.add(GoalStatus.PROPOSED.toString());
				break;
			case PROPOSED:
				ret.add(GoalStatus.PROPOSED.toString());
				if(goal.getProject().getProjectManagers().contains(user)){	// only a PM can Accept or Reject a goal
					// Accepted status is available only after vote...
					//ret.add(GoalStatus.ACCEPTED.toString());
					ret.add(GoalStatus.REJECTED.toString());
				}
				break;
			case ACCEPTED:
				ret.add(GoalStatus.ACCEPTED.toString());
				if(goal.getGoalEnactor().equals(user)){		// only GE can Approve or Require a refinement
					ret.add(GoalStatus.APPROVED.toString());
					ret.add(GoalStatus.FOR_REVIEW.toString());
				}
				break;
			case REJECTED:
				ret.add(GoalStatus.REJECTED.toString());
				break;
			case APPROVED:
				ret.add(GoalStatus.APPROVED.toString());
				ret.add(GoalStatus.CLOSED.toString());
				break;
			case FOR_REVIEW:
				ret.add(GoalStatus.FOR_REVIEW.toString());
				if(goal.getGoalOwner().equals(user)){		// only GO can Not Accept a request of refinement
					ret.add(GoalStatus.ACCEPTED.toString());
					ret.add(GoalStatus.PROPOSED.toString());
				}
				break;
			case CLOSED:
				ret.add(GoalStatus.CLOSED.toString());
				break;
			default:
				ret.add(GoalStatus.PROPOSED.toString());
				break;
		}
		return ret;
	}
    
    public Goal goalSplitting(Goal goal, int numberOfSplit){    	
    	// close the current goal
    	goal.setStatus(GoalStatus.CLOSED);
    	for(int i=1;i<=numberOfSplit;i++){
    		Goal temp = new Goal();
    		temp.setGoalOwner(goal.getGoalEnactor());
    		temp.setProject(goal.getProject());
    		temp.setStatus(GoalStatus.DRAFT);
    		
    		temp.setDescription(goal.getDescription().concat(" [").concat(String.valueOf(i)).concat("]"));
    		System.out.println("----------> " + temp.getDescription());
    		temp.setType(goal.getType());
    		temp.setOrgParent(goal); // set current goal parent...
    		
    		goalDao.save(temp);
    	}
    	goalDao.save(goal);
    	return goal;
    }
    
	@Override
	public List<Goal> findByProject(Project project) {
    	if(project !=null)
    		return goalDao.findByProject(project.getId());
    	else
    		return null;
	}

	@Override
	public List<Goal> getMeasuredGoal(Project currentProject) {		
		Map<String, Object> maps = new Hashtable<String, Object>();
		maps.put("project_id", currentProject.getId());
		List<Goal> goals= goalDao.findByNamedQuery("findMeasuredGoal", maps);
		return goals;
	}
	
	@Override
	public List<SimpleMetric> getMeasuredMetricByGoal(Goal goal) {
		Map<String, Object> maps = new Hashtable<String, Object>();
		maps.put("goal_id", goal.getId());
		List<SimpleMetric> metrics= metricDao.findByNamedQuery("findMeasuredMetric", maps);
		return metrics;		
	}
	
	@Override
	public List<Goal> getOrganizationalGoal(Project currentProject){
		return goalDao.getOrganizationalGoal(currentProject.getId());
	}
	
	@Override
	public boolean rootExists(Project currentProject){
		
		if (this.getOrganizationalGoal(currentProject).size() > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	/*public boolean contains(Goal g) {
		return goalDao.contains(g);
	}*/
}