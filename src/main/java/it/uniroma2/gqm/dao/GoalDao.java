package it.uniroma2.gqm.dao;

import org.appfuse.dao.GenericDao;

import it.uniroma2.gqm.model.*;

import java.util.List;
 
public interface GoalDao extends GenericDao<Goal, Long> {
    public List<Goal> findByDescription(String description);
    public List<Goal> findByProject(Long projectId);
    public List<Goal> getOrganizationalGoal(Long projectId);
    //public boolean contains(Goal g);
}