package it.uniroma2.gqm.model;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.ManyToOne;


@Embeddable
public class GoalQuestionPK implements Serializable { 
	private static final long serialVersionUID = 6222968472061692626L;	
	private Goal goal;
	private Question question;
	
	public GoalQuestionPK(){ 
		;
	}
	public GoalQuestionPK(Goal goal, Question question){
		this.goal = goal;
		this.question = question;
	}
	
	@ManyToOne
	public Goal getGoal() {
		return goal;
	}
	public void setGoal(Goal goal) {
		this.goal = goal;
	}
	
	@ManyToOne
	public Question getQuestion() {
		return question;
	}
	public void setQuestion(Question question) {
		this.question = question;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		result = prime * result + ((question == null) ? 0 : question.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GoalQuestionPK other = (GoalQuestionPK) obj;
		if (goal == null) {
			if (other.goal != null)
				return false;
		} else if (!goal.equals(other.goal))
			return false;
		if (question == null) {
			if (other.question != null)
				return false;
		} else if (!question.equals(other.question))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "GoalQuestionPK [goal=" + goal + ", question=" + question + "]";
	}
}