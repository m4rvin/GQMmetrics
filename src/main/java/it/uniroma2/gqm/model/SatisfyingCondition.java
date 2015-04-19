package it.uniroma2.gqm.model;

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
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;

@Entity
@Table(name = "satisfying_condition")
@NamedQueries({ 
	 @NamedQuery(name = "findSatisfyingConditionByProject", query = "select sc from SatisfyingCondition sc where sc.project.id= :project_id "),
	 @NamedQuery(name = "findSatisfyingConditionTargetByMetric", query = "select g, q, m from AbstractMetric m join m.questions qm join qm.pk.question q join q.goals gq join gq.pk.goal g"
	 		  + " where m.id = :metric_id and qm.status like 'APPROVED' and"
	 		  + " not exists (select sct from SatisfyingConditionTarget sct where sct.goal.id = g.id and sct.question.id = q.id and sct.metric.id = m.id)"), 
	 @NamedQuery(name = "findSatisfyingConditionTargetByRepresentation", query = "select g, q, m from AbstractMetric m join m.questions qm join qm.pk.question q join q.goals gq join gq.pk.goal g"
	 		  + " where m.id = :metric_id and g.id = :goal_id and q.id = :question_id")
	 })
public class SatisfyingCondition extends BaseObject
{

	 private static final long serialVersionUID = 35874115002726557L;

	 private Long id;
	 private String hypotesis;
	 private SatisfyingConditionOperationEnum satisfyingConditionOperation;
	 private Double satisfyingConditionValue;
	 private Set<SatisfyingConditionTarget> targets;
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

	 @NotBlank(message = "hypotesis cannot be empty")
	 public String getHypotesis()
	 {
		  return hypotesis;
	 }

	 public void setHypotesis(String hypotesis)
	 {
		  this.hypotesis = hypotesis;
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

	 @OneToMany(mappedBy = "satisfyingCondition", cascade = CascadeType.ALL) //satisfying condition target is an "entità debole" ---> translation for on delete cascade
	 @NotEmpty(message = "satisfying condition targets cannot be empty")
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
	 
	 @Override
	 public String toString()
	 {
		  return "SatisfyingCondition [id=" + id + ", hypotesis=" + hypotesis + ", satisfyingConditionOperation=" + satisfyingConditionOperation + ", satisfyingConditionValue=" + satisfyingConditionValue + ", targets=" + targets + ", project=" + project + "]";
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((hypotesis == null) ? 0 : hypotesis.hashCode());
		  result = prime * result + ((id == null) ? 0 : id.hashCode());
		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  result = prime * result + ((satisfyingConditionOperation == null) ? 0 : satisfyingConditionOperation.hashCode());
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
		  if (hypotesis == null)
		  {
				if (other.hypotesis != null)
					 return false;
		  } else if (!hypotesis.equals(other.hypotesis))
				return false;
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
