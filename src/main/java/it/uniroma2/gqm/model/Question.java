package it.uniroma2.gqm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.json.JSONObject;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "findQuestionByProject",
            query = "select q from Question q  where q.project.id= :project_id "
    )
})
public class Question extends BaseObject {

	private static final long serialVersionUID = -5918140930681988529L;
	private Long id;	
	private Project project;
	private String name;
	private String text;
	private Set<GoalQuestion> goals = new HashSet<GoalQuestion>();
	private User questionOwner;
	private Set<QuestionMetric> metrics = new HashSet<QuestionMetric>();
	
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Id @Column(name = "question_id",nullable=false,unique=true)
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name = "name", length = 255, nullable = false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name = "text", length = 255, nullable = false)
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Question other = (Question) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Question [id=" + id + ", name=" + name + ", text=" + text + "]";
	}
	
	public JSONObject toJSON()
	{
		return new JSONObject().put("id", this.id).put("name", this.name);
	}
    

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.question")
	public Set<GoalQuestion> getGoals() {
		return goals;
	}
	public void setGoals(Set<GoalQuestion> goals) {
		this.goals = goals;
	}

	@ManyToOne
	@JoinColumn(name = "qso_id", nullable = false)	
	public User getQuestionOwner() {
		return questionOwner;
	}
	public void setQuestionOwner(User questionOwner) {
		this.questionOwner = questionOwner;
	}
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.question")
	public Set<QuestionMetric>  getMetrics() {
		return metrics;
	}

	public void setMetrics(Set<QuestionMetric> metrics) {
		this.metrics = metrics;
	}

}
