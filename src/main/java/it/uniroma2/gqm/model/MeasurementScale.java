package it.uniroma2.gqm.model;


import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import org.appfuse.model.BaseObject;

@Entity
@Table(name="measurement_scale", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "measurementScale_type", "rangeofvalues_id" }))
@NamedQueries({
	@NamedQuery(
			  name = "findMeasurementScaleByProject",
			  query = "select m from MeasurementScale m where m.project.id = :project_id"
			  ),
	@NamedQuery(
  			  name = "findMeasurementScaleByRangeOfValues",
  			  query = "select m from MeasurementScale m where m.rangeOfValues.id = :rangeofvalues_id"
	 )
})
public class MeasurementScale extends BaseObject {


	private static final long serialVersionUID = -4900033895686795409L;
	
	@Id
	@Column(name = "measurementScale_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	@Column(unique = true)
	private String name;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "rangeofvalues_id")
	private RangeOfValues rangeOfValues;
	
	@Column(name = "supported_operations")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "MeasurementScale_DefaultOperation",
				joinColumns = {@JoinColumn(name = "measurementScale_id")},
				inverseJoinColumns = {@JoinColumn(name = "defaultoperation_id")}
			  	)
	private Set<DefaultOperation> operations;
	
	@Column(name = "measurementScale_type")
	private MeasurementScaleTypeEnum type;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	private Project project;
	

	@Override
	public String toString() {
		return "MeasurementScale [id=" + id + ", name=" + name
				+ ", rangeOfValues=" + rangeOfValues + ", operations="
				+ operations + ", type=" + type + ", project=" + project + "]";
	}

	public MeasurementScaleTypeEnum getType() {
		return type;
	}

	public void setType(MeasurementScaleTypeEnum type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public RangeOfValues getRangeOfValues() {
		return rangeOfValues;
	}

	public void setRangeOfValues(RangeOfValues rangeOfValues) {
		this.rangeOfValues = rangeOfValues;
	}

	public Set<DefaultOperation> getOperations() {
		return operations;
	}

	public void setOperations(Set<DefaultOperation> operations) {
		this.operations = operations;
	}
	
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((operations == null) ? 0 : operations.hashCode());
		result = prime * result + ((project == null) ? 0 : project.hashCode());
		result = prime * result
				+ ((rangeOfValues == null) ? 0 : rangeOfValues.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		MeasurementScale other = (MeasurementScale) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (operations == null) {
			if (other.operations != null)
				return false;
		} else if (!operations.equals(other.operations))
			return false;
		if (project == null) {
			if (other.project != null)
				return false;
		} else if (!project.equals(other.project))
			return false;
		if (rangeOfValues == null) {
			if (other.rangeOfValues != null)
				return false;
		} else if (!rangeOfValues.equals(other.rangeOfValues))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	
}
