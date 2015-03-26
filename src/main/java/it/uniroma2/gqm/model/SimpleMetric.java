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
	 public String toString()
	 {
		  return "SimpleMetric : [ id=" + this.id + ", name=" + this.name + " ]";
	 }

}
