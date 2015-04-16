package it.uniroma2.gqm.model;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "satisfying_condition")
public class SatisfyingCondition extends BaseObject
{

	 private static final long serialVersionUID = 35874115002726557L;
	 
	 private Long id;
	 private String hypotesis;
	 private SatisfyingConditionOperationEnum satisfyingConditionOperation;
	 private Double satisfyingConditionValue;
	 private Set<SatisfyingConditionTarget> targets;
	 
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
	 public SatisfyingConditionOperationEnum getSatisfyingConditionOperation()
	 {
	 	 return satisfyingConditionOperation;
	 }

	 public void setSatisfyingConditionOperation(SatisfyingConditionOperationEnum satisfyingConditionOperation)
	 {
	 	 this.satisfyingConditionOperation = satisfyingConditionOperation;
	 }

	 @Column(name = "satisfying_value")
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

	 @Override
	 public String toString()
	 {
		  // TODO Auto-generated method stub
		  return null;
	 }

	 @Override
	 public boolean equals(Object o)
	 {
		  // TODO Auto-generated method stub
		  return false;
	 }

	 @Override
	 public int hashCode()
	 {
		  // TODO Auto-generated method stub
		  return 0;
	 }

}
