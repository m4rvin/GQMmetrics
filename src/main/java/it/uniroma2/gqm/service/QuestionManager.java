package it.uniroma2.gqm.service;


import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.GoalQuestion;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;

import java.util.List;

import org.appfuse.model.User;
import org.appfuse.service.GenericManager;

public interface QuestionManager extends GenericManager<Question, Long> {
	public List<Question> findByProject(Project project);
	public GoalQuestion getGoalQuestion(Goal goal,Question question);
	public List<String> getAvailableStatus(GoalQuestion goalQuestion, User user);
	public GoalQuestion saveGoalQuestion(GoalQuestion question);
}
