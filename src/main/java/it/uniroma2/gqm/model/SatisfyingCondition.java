package it.uniroma2.gqm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.hibernate.validator.constraints.Range;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;

@Entity
@Table(name = "satisfying_condition")
@NamedQueries({ 
	 @NamedQuery(name = "findSatisfyingConditionByProject", query = "select sc from SatisfyingCondition sc where sc.project.id= :project_id "),
	 @NamedQuery(name = "findSatisfyingConditionTargetByMetric", query = "select g, q, m from AbstractMetric m join m.questions qm join qm.pk.question q join q.goals gq join gq.pk.goal g where m.id = :metric_id and qm.status like 'APPROVED' and m.outputValueType not like 'UNDEFINED' and not exists (select sct from SatisfyingConditionTarget sct where sct.goal.id = g.id and sct.question.id = q.id and sct.metric.id = m.id)"), 
	 @NamedQuery(name = "findSatisfyingConditionTargetByMetricEditing", query = "select g, q, m from AbstractMetric m join m.questions qm join qm.pk.question q join q.goals gq join gq.pk.goal g"
	 		  + " where m.id = :metric_id and qm.status like 'APPROVED' and m.outputValueType not like 'UNDEFINED'"),
	 @NamedQuery(name = "findSatisfyingConditionTargetByRepresentation", query = "select g, q, m from AbstractMetric m join m.questions qm join qm.pk.question q join q.goals gq join gq.pk.goal g"
	 		  + " where m.id = :metric_id and g.id = :goal_id and q.id = :question_id"),
	 @NamedQuery(name = "findSatisfyingConditionByProjectGoalMetric", query = "select sc from SatisfyingCondition sc JOIN sc.targets sct where sct.satisfyingCondition.id = sc.id AND sct.project.id = :project_id AND sct.goal.id = :goal_id AND sct.metric.id = :metric_id")
	 })
public class SatisfyingCondition extends BaseObject
{

	 private static final long serialVersionUID = 35874115002726557L;

	 private Long id;
	 private SatisfyingConditionOperationEnum satisfyingConditionOperation;
	 private Double satisfyingConditionValue;
	 private Set<SatisfyingConditionTarget> targets = new HashSet<SatisfyingConditionTarget>();
	 private Project project;
	 private User satisfyingConditionOwner;

	 @Id
	 @Column(name = "satisfying_condition_id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 public Long getId()
	 {
		  return id;
	 }

	 public void setId(Long id)
	 {
		  this.id = id;
	 }

	 @Column(name = "condition_operation")
	 @Enumerated(EnumType.STRING)
	 @NotNull(message = "satisfying condition operation cannot be empty")
	 public SatisfyingConditionOperationEnum getSatisfyingConditionOperation()
	 {
		  return satisfyingConditionOperation;
	 }

	 public void setSatisfyingConditionOperation(SatisfyingConditionOperationEnum satisfyingConditionOperation)
	 {
		  this.satisfyingConditionOperation = satisfyingConditionOperation;
	 }

	 @Column(name = "satisfying_value")
	 @Range(message = "satisfying condition value must be a valid number") //range annotation must be used when validating a number
	 @NotNull(message = "satisfying condition value cannot be empty")
	 public Double getSatisfyingConditionValue()
	 {
		  return satisfyingConditionValue;
	 }

	 public void setSatisfyingConditionValue(Double satisfyingConditionValue)
	 {
		  this.satisfyingConditionValue = satisfyingConditionValue;
	 }

	 @OneToMany(mappedBy = "satisfyingCondition", cascade = CascadeType.ALL, orphanRemoval = true) //satisfying condition target is an "entità debole" ---> translation for on delete cascade
	 @NotEmpty(message = "satisfying condition targets cannot be empty")
	 @NotNull(message = "satisfying condition targets cannot be null")
	 public Set<SatisfyingConditionTarget> getTargets()
	 {
		  return targets;
	 }

	 public void setTargets(Set<SatisfyingConditionTarget> targets)
	 {
		  this.targets = targets;
	 }

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "project_id", nullable = false)
	 public Project getProject()
	 {
		  return project;
	 }

	 public void setProject(Project project)
	 {
		  this.project = project;
	 }

	 @ManyToOne
	 @JoinColumn(name = "owner_id")
	 public User getSatisfyingConditionOwner()
	 {
		  return satisfyingConditionOwner;
	 }

	 public void setSatisfyingConditionOwner(User satisfyingConditionOwner)
	 {
		  this.satisfyingConditionOwner = satisfyingConditionOwner;
	 }
	 
	 /**
	  * Function which evaluate the measured value
	  * @param measuredValue
	  * @return true if <b>measuredValue</b> satisfies the condition ,false otherwise
	  * @throws IllegalArgumentException if <b>measuredValue</b> is not a valid number, null or Double.MIN_VALUE
	  */
	 public boolean getSatisfaction(Double  measuredValue)
	 {
		  if(measuredValue == null || measuredValue == Double.MIN_VALUE)
				throw new IllegalArgumentException("The metric has not a valid associated measurement, then it is not evaluable");
		  
		  Double threshold = this.satisfyingConditionValue;
		  
		  switch(this.satisfyingConditionOperation)
		  {
   		  case EQUAL:
   				return measuredValue.equals(threshold);
   		  case GREATER:
   				return measuredValue > threshold;
   		  case GREATER_OR_EQUAL:
   				return measuredValue >= threshold;
   		  case LESS:
   				return measuredValue < threshold;
   		  case LESS_OR_EQUAL:
   				return measuredValue <= threshold;
   		  case NOT_EQUAL:
   				return !measuredValue.equals(threshold);
   		  default:
   				return false;
		  }
	 }
	 
	 @Override
	 public String toString()
	 {
		  return "SatisfyingCondition [id=" + id + ", satisfyingConditionOperation=" + satisfyingConditionOperation + ", satisfyingConditionValue=" + satisfyingConditionValue + ", targets=" + targets + ", project=" + project + ", satisfyingConditionOwner=" + satisfyingConditionOwner + "]";
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((id == null) ? 0 : id.hashCode());
		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  result = prime * result + ((satisfyingConditionOperation == null) ? 0 : satisfyingConditionOperation.hashCode());
		  result = prime * result + ((satisfyingConditionOwner == null) ? 0 : satisfyingConditionOwner.hashCode());
		  result = prime * result + ((satisfyingConditionValue == null) ? 0 : satisfyingConditionValue.hashCode());
		  result = prime * result + ((targets == null) ? 0 : targets.hashCode());
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
		  SatisfyingCondition other = (SatisfyingCondition) obj;
		  if (id == null)
		  {
				if (other.id != null)
					 return false;
		  } else if (!id.equals(other.id))
				return false;
		  if (project == null)
		  {
				if (other.project != null)
					 return false;
		  } else if (!project.equals(other.project))
				return false;
		  if (satisfyingConditionOperation != other.satisfyingConditionOperation)
				return false;
		  if (satisfyingConditionOwner == null)
		  {
				if (other.satisfyingConditionOwner != null)
					 return false;
		  } else if (!satisfyingConditionOwner.equals(other.satisfyingConditionOwner))
				return false;
		  if (satisfyingConditionValue == null)
		  {
				if (other.satisfyingConditionValue != null)
					 return false;
		  } else if (!satisfyingConditionValue.equals(other.satisfyingConditionValue))
				return false;
		  if (targets == null)
		  {
				if (other.targets != null)
					 return false;
		  } else if (!targets.equals(other.targets))
				return false;
		  return true;
	 }
}
