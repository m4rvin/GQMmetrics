package it.uniroma2.gqm.model;


import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name="measurement_scale")
@NamedQueries({
	@NamedQuery(
			  name = "findMeasurementScaleByProject",
			  query = "select m from MeasurementScale where m.project.id = :project_id"
			  ) 
})
public class MeasurementScale extends BaseObject {


	private static final long serialVersionUID = -4900033895686795409L;
	
	@Id
	
	private long id;
	private String name;
	private RangeOfValues rangeOfValues;
	private Set<DefaultOperation> operations;
	private MeasurementScaleTypeEnum type;

	
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
	public int hashCode()
	{
		 final int prime = 31;
		 int result = 1;
		 result = prime * result + (int) (id ^ (id >>> 32));
		 result = prime * result + ((name == null) ? 0 : name.hashCode());
		 result = prime * result + ((operations == null) ? 0 : operations.hashCode());
		 result = prime * result + ((project == null) ? 0 : project.hashCode());
		 result = prime * result + ((rangeOfValues == null) ? 0 : rangeOfValues.hashCode());
		 result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		 MeasurementScale other = (MeasurementScale) obj;
		 if (id != other.id)
			  return false;
		 if (name == null)
		 {
			  if (other.name != null)
					return false;
		 } else if (!name.equals(other.name))
			  return false;
		 if (operations == null)
		 {
			  if (other.operations != null)
					return false;
		 } else if (!operations.equals(other.operations))
			  return false;
		 if (project == null)
		 {
			  if (other.project != null)
					return false;
		 } else if (!project.equals(other.project))
			  return false;
		 if (rangeOfValues == null)
		 {
			  if (other.rangeOfValues != null)
					return false;
		 } else if (!rangeOfValues.equals(other.rangeOfValues))
			  return false;
		 if (type != other.type)
			  return false;
		 return true;
	}
	
}
