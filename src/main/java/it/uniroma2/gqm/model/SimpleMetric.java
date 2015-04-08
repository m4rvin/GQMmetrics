package it.uniroma2.gqm.model;

import it.uniroma2.gqm.webapp.controller.MetricValidator;
import it.uniroma2.gqm.webapp.controller.RangeOfValueValidator;

import javax.persistence.Column;
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
@NamedQueries({ 
	@NamedQuery(name = "findSimpleMetricByProject", query = "select m from SimpleMetric m where m.project.id= :project_id"),
	@NamedQuery(name = "findMeasurableSimpleMetricByProject", query = "select m from SimpleMetric m inner join m.questions qm " + " inner join qm.pk.question q  " + " inner join q.goals qg " + " inner join qg.pk.goal g " + " where g.project.id= :project_id AND qm.status LIKE 'APPROVED'")
})
public class SimpleMetric extends AbstractMetric
{

	 private static final long serialVersionUID = 7990600814484921752L;
	 

	 // TODO private enum complexMetricType???
	 
	 
	 @Column
	 private String aggregator;

	 public String getAggregator()
	 {
	 	 return aggregator;
	 }

	 public void setAggregator(String aggregator)
	 {
	 	 this.aggregator = aggregator;
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = super.hashCode();
		  result = prime * result + ((aggregator == null) ? 0 : aggregator.hashCode());
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
		  SimpleMetric other = (SimpleMetric) obj;
		  if (aggregator == null)
		  {
				if (other.aggregator != null)
					 return false;
		  } else if (!aggregator.equals(other.aggregator))
				return false;
		  return true;
	 }

	 @Override
	 public String toString()
	 {
		  return "SimpleMetric : [ id=" + this.id + ", name=" + this.name + " ]";
	 }

}
