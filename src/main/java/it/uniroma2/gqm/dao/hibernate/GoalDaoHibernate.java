package it.uniroma2.gqm.dao.hibernate;


import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.appfuse.dao.UserDao;
import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.appfuse.model.User;

import it.uniroma2.gqm.dao.*;
import it.uniroma2.gqm.model.*;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
 
@Repository("goalDao")
public class GoalDaoHibernate extends GenericDaoHibernate<Goal, Long> implements GoalDao {
	
	@Autowired
	private UserDao userDao;
	
	
    public GoalDaoHibernate() {
        super(Goal.class);
    }
 
    public List<Goal> findByDescription(String description) {
        return getSession().createCriteria(Goal.class).add(Restrictions.eq("description", description)).list();
    }
    
	@Override
	public void remove(Goal object) {
		super.remove(object);
	}
	
	@Override
	public Goal save(Goal object)  {
		Goal updatedGoal = object;
		if(updatedGoal.getId() != null){	
			if(updatedGoal.getStatus() == GoalStatus.PROPOSED){		 		
				Session old= getSessionFactory().openSession();		//TODO Perchè prende un'altra sessione?	
				Goal gTemp = (Goal) old.get(Goal.class, updatedGoal.getId());
				System.out.println("*** OLD STATUS = " +  gTemp.getStatus());	
				if(gTemp.getStatus() == GoalStatus.FOR_REVIEW){
					System.out.println("*** clean old votes....");	
					// clean previous vote....
					updatedGoal.getVotes().clear();
				}
				old.close();
			}
		}
		// if the quorum was reached, change status to Accepted
		if(updatedGoal.getStatus() == GoalStatus.PROPOSED && updatedGoal.getNumberOfVote() == updatedGoal.getQuorum())
			updatedGoal.setStatus(GoalStatus.ACCEPTED);
		
		try{
			System.out.println("numero di voti: " + updatedGoal.getVotes().size());
			if(updatedGoal.getVotes() != null && updatedGoal.getVotes().size() > 0){
				List<Long> ids = new ArrayList<Long>();
				for(User u:updatedGoal.getVotes())
					ids.add(u.getId());
				
				
				updatedGoal.getVotes().clear();
				updatedGoal = (Goal)getSession().merge(updatedGoal);
				getSession().flush();
				for(Long uId:ids){
					updatedGoal.getVotes().add(userDao.get(uId));
					updatedGoal = (Goal)getSession().merge(updatedGoal);
					getSession().flush();
				}
			}else {
				updatedGoal = (Goal)getSession().merge(updatedGoal);
		        getSession().flush();
			}
		} catch (Exception e){	
			e.printStackTrace();
		}

		return updatedGoal;
	}
	
	@Override
	public List<Goal> findByProject(Long id) {
		     	Query q =  getSession().getNamedQuery("findGoalByProject").setLong("project_id", id);
    	return q.list();
	}

	@Override
	public List<Goal> getOrganizationalGoal(Long projectId) {
		Map<String, Object> maps = new Hashtable<String, Object>();
		maps.put("project_id", projectId);
		List<Goal> goals= findByNamedQuery("findOrganizationalGoal", maps);
		return goals;
	}
    
	/*public boolean contains(Goal g) {
		return getSession().contains(g);
	}*/
}