package it.uniroma2.gqm.dao.hibernate;



import java.util.List;



import org.appfuse.dao.UserDao;
import org.appfuse.dao.hibernate.GenericDaoHibernate;

import it.uniroma2.gqm.dao.*;
import it.uniroma2.gqm.model.*;

import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
 
@Repository("questionDao")
public class QuestionDaoHibernate extends GenericDaoHibernate<Question, Long> implements QuestionDao {
	
	
    public QuestionDaoHibernate() {
        super(Question.class);
    }
 
    public List<Question> findByProject(Long id) {
    	Query q =  getSession().getNamedQuery("findQuestionByProject").setLong("project_id", id);
    	return q.list();
    }

	@Override
	public Question save(Question object) {
		if(object.getId() != null){			
			Question qTemp = get(object.getId());
			for(GoalQuestion gq: object.getGoals()){
				if(!qTemp.getGoals().contains(gq)){
					getSession().saveOrUpdate(gq);
				}
			}
			for(GoalQuestion gqTemp: qTemp.getGoals()){
				if(!object.getGoals().contains(gqTemp)){
					getSession().delete(gqTemp);
				}
			}
		}else {
			getSession().saveOrUpdate(object);
		}
		
		try{
			getSession().merge(object);
		} catch(Exception e){
			getSession().saveOrUpdate(object);
		}
        getSession().flush();
		return object;
	}

	@Override
	public void remove(Question object) {
		if(object.getGoals().size() >0)
			throw new UnsupportedOperationException("The question [" + object.getId() + "] should not be associated with any goals to be eliminated!");		
		super.remove(object);
	}

	@Override
	public void remove(Long id) {
		Question q = get(id);
		super.remove(q);
	}
}