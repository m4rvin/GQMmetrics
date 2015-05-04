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

import org.json.JSONObject;

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
		  if (!this.composedBy.contains(metric))
		  {
				this.composedBy.add(metric);
				metric.addComposerFor(this);
		  }
	 }

	 public void removeComposedBy(AbstractMetric metric)
	 {
		  //removed to overcome ConcurrentModificationException: this.composedBy.remove(metric);
		  metric.removeComposerFor(this);
	 }
	 
	 @Override
	 public String toString()
	 {
		  return "CombinedMetric : [ id=" + this.id + ", name=" + this.name + " ]";
	 }
	 
	 @Override
	 public JSONObject toJSON()
	 {
		 JSONObject metricInfo = new JSONObject();
		 metricInfo.put("type", "combined metric");
		 metricInfo.put("name", this.name);
		 metricInfo.put("formula", this.formula);
		 
		 return metricInfo;
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = super.hashCode();
		  result = prime * result + ((composedBy == null) ? 0 : composedBy.hashCode());
		  return result;
	 }

	 @Override
	 public boolean equals(Object obj)
	 {
		  if (this == obj)
				return true;
		  if (!super.equals(obj))
				return false;
		  if (getClass() != obj.getClass())
				return false;
		  CombinedMetric other = (CombinedMetric) obj;
/*		  if (composedBy == null)
		  {
				if (other.composedBy != null)
					 return false;
		  } else if (!composedBy.equals(other.composedBy))
				return false; */
		  return true;
	 }

}
