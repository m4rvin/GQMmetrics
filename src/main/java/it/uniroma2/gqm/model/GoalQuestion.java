package it.uniroma2.gqm.model;

import java.io.Serializable;

import javax.persistence.AssociationOverride;
import javax.persistence.AssociationOverrides;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.appfuse.model.BaseObject;


@Entity
@Table(name = "goal_question")
@AssociationOverrides({ @AssociationOverride(name = "pk.question", joinColumns = @JoinColumn(name = "question_id")),
						@AssociationOverride(name = "pk.goal", joinColumns = @JoinColumn(name = "goal_id")) }) 
@NamedQueries({
    @NamedQuery(
            name = "findGoalQuestion",
            query = "select gq from GoalQuestion gq  where gq.pk.goal.id= :goal_id and  gq.pk.question.id = :question_id"
    )
})
public class GoalQuestion extends BaseObject { 
	private static final long serialVersionUID = -3542171103409189810L;
	private GoalQuestionPK pk;
	private GoalQuestionStatus status;
	private String refinement;
	
	@EmbeddedId
	public GoalQuestionPK getPk() {
		return pk;
	}

	public void setPk(GoalQuestionPK pk) {
		this.pk = pk;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((pk == null) ? 0 : pk.hashCode());
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
		GoalQuestion other = (GoalQuestion) obj;
		if (pk == null) {
			if (other.pk != null)
				return false;
		} else if (!pk.equals(other.pk))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "GoalQuestion [goal=" + getGoal() + ", question="  + getQuestion()  +"]";
	}
	
	@Transient
	public Goal getGoal(){
		return this.pk.getGoal();
	}
	
	@Transient
	public Question getQuestion(){
		return this.pk.getQuestion();
	}

	@Enumerated(EnumType.STRING)
	@Column(name = "status", length = 50)
	public GoalQuestionStatus getStatus() {
		return status;
	}

	public void setStatus(GoalQuestionStatus status) {
		this.status = status;
	}
	
	@Column(name = "refinement", length = 255)
	public String getRefinement() {
		return refinement;
	}

	public void setRefinement(String refinement) {
		this.refinement = refinement;
	}
}

