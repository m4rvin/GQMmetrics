package it.uniroma2.gqm.model;

import java.util.Set;

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

import org.appfuse.model.BaseObject;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotBlank;
import org.springmodules.validation.bean.conf.loader.annotation.handler.NotEmpty;
import org.springmodules.validation.bean.conf.loader.annotation.handler.RegExp;

@Entity
@Table(name = "satisfying_condition")
@NamedQueries({ 
	 @NamedQuery(name = "findSatisfyingConditionByProject", query = "select sc from SatisfyingCondition sc where sc.project.id= :project_id ") 
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

	 @NotEmpty(message = "hypotesis cannot be empty")
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
	 @NotEmpty(message = "satisfying condition operation cannot be empty")
	 public SatisfyingConditionOperationEnum getSatisfyingConditionOperation()
	 {
		  return satisfyingConditionOperation;
	 }

	 public void setSatisfyingConditionOperation(SatisfyingConditionOperationEnum satisfyingConditionOperation)
	 {
		  this.satisfyingConditionOperation = satisfyingConditionOperation;
	 }

	 @Column(name = "satisfying_value")
	 @NotEmpty(message = "satisfying condition value cannot be empty")
	 public Double getSatisfyingConditionValue()
	 {
		  return satisfyingConditionValue;
	 }

	 public void setSatisfyingConditionValue(Double satisfyingConditionValue)
	 {
		  this.satisfyingConditionValue = satisfyingConditionValue;
	 }

	 @OneToMany(mappedBy = "satisfyingCondition")
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
