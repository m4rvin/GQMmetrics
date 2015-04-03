package it.uniroma2.gqm.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;
import org.hibernate.annotations.Type;
import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;

@Entity
@Indexed
@NamedQueries({
    @NamedQuery(
            name = "findMeasuremntsByMetric",
            query = "select m from Measurement m  where m.metric.id= :metric_id order by m.collectingDate, m.collectingTime "
    ),
    @NamedQuery(
            name = "findMeasurementByProject",
            query = "select distinct m from Measurement m inner join m.metric mt " +
            		"inner join mt.questions qm " + 
            		"inner join qm.pk.question q " +
            		"inner join q.goals gq " +
            		"inner join gq.pk.goal g " +
            		"where g.project.id= :project_id"
    )
})
public class Measurement extends BaseObject implements Serializable {
	private static final long serialVersionUID = 5045073708418494229L;
	private Long id;
	private AbstractMetric metric;
	private Date collectingDate;
	private String collectingTime;
	private Date timestamp;
	private String value;
	private User measurementOwner;
	
	@Id
	@Column(name = "measurement_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	@DocumentId
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metric_id", nullable = false)
	public AbstractMetric getMetric() {
		return metric;
	}

	public void setMetric(AbstractMetric metric) {
		this.metric = metric;
	}

	@Column(name = "collecting_d")
	@Type(type = "date")
	public Date getCollectingDate() {
		return collectingDate;
	}

	public void setCollectingDate(Date collectingDate) {
		this.collectingDate = collectingDate;
	}
	
	
	@Column(name = "value")
	@Field
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Measurement other = (Measurement) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Measurement [id=" + id + ", metric=" + metric + ", date="
				+ collectingDate + ", value=" + value + "]";
	}

	@Column(name = "ts")
	@Type(type = "timestamp")
	public Date getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(Date timestamp) {
		this.timestamp = timestamp;
	}

	@ManyToOne
	@JoinColumn(name = "measuremento_id", nullable = true)		
	public User getMeasurementOwner() {
		return measurementOwner;
	}

	public void setMeasurementOwner(User measurementOwner) {
		this.measurementOwner = measurementOwner;
	}
	
	@Transient
	public Date getCollectingDateTime(){
		SimpleDateFormat formatter = new SimpleDateFormat("mm/dd/yyyy HH:MM:SS");
	    try {
	    	Date ret =formatter.parse(this.getCollectingDate().toString() + " " + this.getCollectingTime());
	    	System.out.println("Return date/time: " + ret);
			return ret;
		} catch (ParseException e) {
			e.printStackTrace();
		}
	    return null;
	}
	
	@Column(name = "collecting_t")
	public String getCollectingTime() {
		return collectingTime;
	}

	public void setCollectingTime(String collectingTime) {
		this.collectingTime = collectingTime;
	}
	
	@Transient
	@Field
	public String getMetricCode(){
		if(this.metric != null)
			return this.metric.getCode();
		else
			return null;
	}

}
