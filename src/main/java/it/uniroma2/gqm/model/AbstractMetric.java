package it.uniroma2.gqm.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;


@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn(name="complexMetricType")
@Table(name="AbstractMetric")
public abstract class AbstractMetric extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = -4627758431767217159L;
	
	private Long id;

	
	@Id
	@Column(name="metric_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	
	
	public void setId(Long id) {
		this.id = id;
	}
	
}
