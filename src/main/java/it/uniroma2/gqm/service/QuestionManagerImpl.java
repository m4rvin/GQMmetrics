package it.uniroma2.gqm.service;


import it.uniroma2.gqm.dao.GoalQuestionDao;
import it.uniroma2.gqm.dao.QuestionDao;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.GoalStatus;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("questionManager")
public class QuestionManagerImpl extends GenericManagerImpl<Question, Long> implements QuestionManager {
    private QuestionDao questionDao;
    private GoalQuestionDao goalQuestionDao;
	
    @Autowired
    public QuestionManagerImpl(QuestionDao questionDao, 
    		GoalQuestionDao goalQuestionDao) {
        super(questionDao);
        this.questionDao = questionDao;
        this.goalQuestionDao = goalQuestionDao;
    }
 
    public List<Question> findByProject(Project project) {
    	if(project !=null)
    		return questionDao.findByProject(project.getId());
    	else
    		return null;
    }
    public GoalQuestion getGoalQuestion(Goal goal,Question question){
    	if(goal!=null && question!=null)
    		return goalQuestionDao.getGoalQuestion(goal.getId(), question.getId());
    	else
    		return null;
    }
    
    public List<String> getAvailableStatus(GoalQuestion goalQuestion, User user){
		List<String> ret = new ArrayList<String>();
		switch (goalQuestion.getStatus()) {
			case PROPOSED:
				ret.add(GoalStatus.PROPOSED.toString());				
				if(goalQuestion.getGoal().getMMDMMembers().contains(user)){			// only MMDM can ... a Question...
					ret.add(GoalStatus.FOR_REVIEW.toString());
					ret.add(GoalStatus.APPROVED.toString());
					ret.add(GoalStatus.REJECTED.toString());
				}
				break;
			case FOR_REVIEW:
				ret.add(GoalStatus.FOR_REVIEW.toString());				
				if(goalQuestion.getQuestion().getQuestionOwner().equals(user)){			// only MMDM can ... a Question...
					ret.add(GoalStatus.PROPOSED.toString());
				}
				break;
			case APPROVED:
				ret.add(GoalStatus.APPROVED.toString());				
				break;
			case REJECTED:
				ret.add(GoalStatus.APPROVED.toString());				
				break;				
				
		}
		return ret;
    }
    
    public Question save(Question question){
    	return questionDao.save(question);
    }
    
    public GoalQuestion saveGoalQuestion(GoalQuestion goalQuestion){
    	return goalQuestionDao.save(goalQuestion);
    }
}