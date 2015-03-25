package it.uniroma2.gqm.model;

import it.uniroma2.gqm.webapp.controller.MetricValidator;
import it.uniroma2.gqm.webapp.controller.RangeOfValueValidator;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

@Entity
@DiscriminatorValue("simple")
@Table(name = "SimpleMetric")
@NamedQueries({ @NamedQuery(name = "findSimpleMetricByProject", query = "select m from SimpleMetric m where m.project.id= :project_id"), })
public class SimpleMetric extends AbstractMetric
{

	 private static final long serialVersionUID = 7990600814484921752L;
	 

	 // TODO private enum complexMetricType???

	 @Override
	 public int hashCode()
	 {
		  // FIXME metti id da super
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((actualValue == null) ? 0 : actualValue.hashCode());
		  result = prime * result + ((code == null) ? 0 : code.hashCode());
		  result = prime * result + ((collectingType == null) ? 0 : collectingType.hashCode());
		  result = prime * result + ((hypothesis == null) ? 0 : hypothesis.hashCode());
		  result = prime * result + ((measurementScale == null) ? 0 : measurementScale.hashCode());
		  result = prime * result + ((measurements == null) ? 0 : measurements.hashCode());

		  result = prime * result + ((metricOwner == null) ? 0 : metricOwner.hashCode());
		  result = prime * result + ((name == null) ? 0 : name.hashCode());

		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  /*
		   * result = prime * result + ((questions == null) ? 0 :
		   * questions.hashCode());
		   */
		  result = prime * result + ((satisfyingConditionOperation == null) ? 0 : satisfyingConditionOperation.hashCode());
		  result = prime * result + ((satisfyingConditionValue == null) ? 0 : satisfyingConditionValue.hashCode());
		  result = prime * result + ((type == null) ? 0 : type.hashCode());
		  result = prime * result + ((unit == null) ? 0 : unit.hashCode());
		  return result;
	 }

	 @Override
	 public boolean equals(Object obj)
	 {
		  // FIXME metti id da super
		  if (this == obj)
				return true;
		  if (obj == null)
				return false;
		  if (getClass() != obj.getClass())
				return false;
		  SimpleMetric other = (SimpleMetric) obj;
		  if (actualValue == null)
		  {
				if (other.actualValue != null)
					 return false;
		  } else if (!actualValue.equals(other.actualValue))
				return false;
		  if (code == null)
		  {
				if (other.code != null)
					 return false;
		  } else if (!code.equals(other.code))
				return false;
		  if (collectingType != other.collectingType)
				return false;
		  if (hypothesis == null)
		  {
				if (other.hypothesis != null)
					 return false;
		  } else if (!hypothesis.equals(other.hypothesis))
				return false;
		  if (measurementScale == null)
		  {
				if (other.measurementScale != null)
					 return false;
		  } else if (!measurementScale.equals(other.measurementScale))
				return false;
		  if (measurements == null)
		  {
				if (other.measurements != null)
					 return false;
		  } else if (!measurements.equals(other.measurements))
				return false;

		  if (metricOwner == null)
		  {
				if (other.metricOwner != null)
					 return false;
		  } else if (!metricOwner.equals(other.metricOwner))
				return false;
		  if (name == null)
		  {
				if (other.name != null)
					 return false;
		  } else if (!name.equals(other.name))
				return false;

		  if (project == null)
		  {
				if (other.project != null)
					 return false;
		  } else if (!project.equals(other.project))
				return false;
		  if (questions == null)
		  {
				if (other.questions != null)
					 return false;
		  } else if (!questions.equals(other.questions))
				return false;
		  if (satisfyingConditionOperation != other.satisfyingConditionOperation)
				return false;
		  if (satisfyingConditionValue == null)
		  {
				if (other.satisfyingConditionValue != null)
					 return false;
		  } else if (!satisfyingConditionValue.equals(other.satisfyingConditionValue))
				return false;
		  if (type != other.type)
				return false;
		  if (unit == null)
		  {
				if (other.unit != null)
					 return false;
		  } else if (!unit.equals(other.unit))
				return false;
		  return true;
	 }

	 @Override
	 public String toString()
	 {
		  return "SimpleMetric : [ id=" + this.id + ", name=" + this.name + " ]";
	 }

}
