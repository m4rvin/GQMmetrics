package it.uniroma2.gqm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "satisfying_condition_target")
public class SatisfyingConditionTarget extends BaseObject
{
	 
	 private static final long serialVersionUID = 8695326719015317020L;

	 private Long id;
	 private Project project;
	 private AbstractMetric metric;
	 private Question question;
	 private Goal goal;
	 private SatisfyingCondition satisfyingCondition;
	 
	 @Id
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 @Column(name = "satisfying_condition_target_id")
	 public Long getId()
	 {
		  return id;
	 }
	 
	 public void setId(Long id)
	 {
		  this.id = id;
	 }
	 
	 @OneToOne
	 @JoinColumn(name = "project_id")
	 public Project getProject()
	 {
	 	 return project;
	 }

	 public void setProject(Project project)
	 {
	 	 this.project = project;
	 }

	 @OneToOne
	 @JoinColumn(name = "metric_id")
	 public AbstractMetric getMetric()
	 {
	 	 return metric;
	 }

	 public void setMetric(AbstractMetric metric)
	 {
	 	 this.metric = metric;
	 }

	 @OneToOne
	 @JoinColumn(name = "question_id")
	 public Question getQuestion()
	 {
	 	 return question;
	 }

	 public void setQuestion(Question question)
	 {
	 	 this.question = question;
	 }

	 @OneToOne
	 @JoinColumn(name = "goal_id")
	 public Goal getGoal()
	 {
	 	 return goal;
	 }

	 public void setGoal(Goal goal)
	 {
	 	 this.goal = goal;
	 }

	 @ManyToOne
	 @JoinColumn(name = "satisfying_condition_id")
	 public SatisfyingCondition getSatisfyingCondition()
	 {
	 	 return satisfyingCondition;
	 }

	 public void setSatisfyingCondition(SatisfyingCondition satisfyingCondition)
	 {
	 	 this.satisfyingCondition = satisfyingCondition;
	 }

	 @Override
	 public String toString()
	 {
		  return "SatisfyingConditionTarget [project=" + project + ", metric=" + metric + ", question=" + question + ", goal=" + goal + "]";
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((goal == null) ? 0 : goal.hashCode());
		  result = prime * result + ((metric == null) ? 0 : metric.hashCode());
		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  result = prime * result + ((question == null) ? 0 : question.hashCode());
		  return result;
	 }

	 @Override
	 public boolean equals(Object obj)
	 {
		  if (this == obj)
				return true;
		  if (obj == null)
				return false;
		  if (getClass() != obj.getClass())
				return false;
		  SatisfyingConditionTarget other = (SatisfyingConditionTarget) obj;
		  if (goal == null)
		  {
				if (other.goal != null)
					 return false;
		  } else if (!goal.equals(other.goal))
				return false;
		  if (metric == null)
		  {
				if (other.metric != null)
					 return false;
		  } else if (!metric.equals(other.metric))
				return false;
		  if (project == null)
		  {
				if (other.project != null)
					 return false;
		  } else if (!project.equals(other.project))
				return false;
		  if (question == null)
		  {
				if (other.question != null)
					 return false;
		  } else if (!question.equals(other.question))
				return false;
		  return true;
	 }
	 
}
