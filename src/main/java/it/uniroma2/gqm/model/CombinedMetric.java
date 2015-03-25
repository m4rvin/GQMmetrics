package it.uniroma2.gqm.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@DiscriminatorValue("combined")
@Table(name = "CombinedMetric")
@NamedQueries({ @NamedQuery(name = "findCombinedMetricByProject", query = "select m from CombinedMetric m where m.project.id= :project_id"), })
public class CombinedMetric extends AbstractMetric
{

	 private static final long serialVersionUID = 5194614013197863177L;

	 protected Set<AbstractMetric> composedBy = new HashSet<AbstractMetric>();

	 @ManyToMany(fetch = FetchType.LAZY)
	 @JoinTable(name = "metriccomposedby_metriccomposerfor", joinColumns = { @JoinColumn(name = "metriccomposedby_id", referencedColumnName = "metric_id") }, inverseJoinColumns = { @JoinColumn(name = "metriccomposerfor_id", referencedColumnName = "metric_id") })
	 public Set<AbstractMetric> getComposedBy()
	 {
		  return composedBy;
	 }

	 public void setComposedBy(Set<AbstractMetric> composedBy)
	 {
		  this.composedBy = composedBy;
	 }

	 public void addComposedBy(AbstractMetric metric)
	 {
		  this.composedBy.add(metric);
		  metric.addComposerFor(this);
	 }

	 @Override
	 public String toString()
	 {

		  return "CombinedMetric : [ id=" + this.id + ", name=" + this.name + " ]";
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((composedBy == null) ? 0 : composedBy.hashCode());
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
		  return true;
	 }

}
