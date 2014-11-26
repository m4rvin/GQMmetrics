package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.GoalQuestionDao;
import it.uniroma2.gqm.model.GoalQuestion;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


@Repository("goalQuestionDao")
public class GoalQuestionDaoHibernate extends GenericDaoHibernate<GoalQuestion, Long> implements GoalQuestionDao {
	
	
    public GoalQuestionDaoHibernate() {
        super(GoalQuestion.class);
    }
 

    @SuppressWarnings("unchecked")
	public GoalQuestion getGoalQuestion(Long goalId,Long questionId){
    	List<GoalQuestion> questions;
    	Query q =  getSession().getNamedQuery("findGoalQuestion").setLong("goal_id", goalId).setLong("question_id", questionId);
    	questions = q.list();
    	return (questions!=null && questions.size()>0) ? questions.get(0) : null;
    }
    

}