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
import javax.validation.constraints.Size;

import org.appfuse.model.BaseObject;
import org.json.JSONArray;
import org.json.JSONObject;

@Entity
@Table(name="measurement_scale", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "measurementScale_type", "rangeofvalues_id" }))
@NamedQueries({ @NamedQuery(name = "findMeasurementScaleByProject", query = "select m from MeasurementScale m where m.project.id = :project_id"), @NamedQuery(name = "findMeasurementScaleByRangeOfValues", query = "select m from MeasurementScale m where m.rangeOfValues.id = :rangeofvalues_id") })
public class MeasurementScale extends BaseObject
{

	 private static final long serialVersionUID = -4900033895686795409L;

	 @Id
	 @Column(name = "measurementScale_id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;

	 @Column(unique = true)
	 private String name;

	 @Column(length = 100)
	 @Size(max = 100, message = "Description is too long")
	 private String description;

	 @ManyToOne(fetch = FetchType.EAGER)
	 @JoinColumn(name = "rangeofvalues_id")
	 private RangeOfValues rangeOfValues;

	 @Column(name = "supported_operations")
	 @ManyToMany(fetch = FetchType.EAGER)
	 @JoinTable(name = "MeasurementScale_DefaultOperation", joinColumns = { @JoinColumn(name = "measurementScale_id") }, inverseJoinColumns = { @JoinColumn(name = "defaultoperation_id") })
	 private Set<DefaultOperation> operations;

	 @Column(name = "measurementScale_type")
	 private MeasurementScaleTypeEnum type;

	 @ManyToOne(fetch = FetchType.EAGER)
	 @JoinColumn(name = "project_id", nullable = false)
	 private Project project;

	 @ManyToOne(fetch = FetchType.EAGER)
	 @JoinColumn(name = "unit_id", nullable = true)
	 private Unit measurementUnit;

	 @Override
	 public String toString()
	 {
		  return "MeasurementScale [id=" + id + ", name=" + name + ", description=" + description + ", rangeOfValues=" + rangeOfValues + ", operations=" + operations + ", type=" + type + ", project=" + project + ", measurementUnit=" + measurementUnit + "]";
	 }
	 
	 public String toHumanReadableDescription(){
		 return "name: "+ this.name + ", description: " + this.description + ", measurementscaletype: " + this.type.toString() + ", supported operations: " + toHumanReadableOperationList() + ", range of values name: " + this.rangeOfValues.getName() ;
	 }
	 
	 public JSONObject toJSONObject()
	 {
		 JSONObject measurementScaleInfo = new JSONObject();
		 measurementScaleInfo.append("name", this.name);
		 measurementScaleInfo.append("description", this.description);
		 measurementScaleInfo.append("measurementscaletype", this.type.toString());
		 measurementScaleInfo.append("supportedOperations", toJSONOperationListNames());
		 measurementScaleInfo.append("rangeOfValuesName", this.rangeOfValues.getName());
		 return measurementScaleInfo;

	 }
	 
	 public String toHumanReadableOperationList(){
		 String operationDescription = "_";
		 
		 for(DefaultOperation op: this.operations)
		 {
			 operationDescription += op.getOperation() + "_";
		 }
		 return operationDescription;
	 }
	 
	 public JSONArray toJSONOperationListNames()
	 {
		 JSONArray opList = new JSONArray();
		 
		 for(DefaultOperation op: this.operations)
		 {
			 opList.put(op.getOperation());
		 }
		 return opList;
	 }

	 public MeasurementScaleTypeEnum getType()
	 {
		  return type;
	 }

	 public void setType(MeasurementScaleTypeEnum type)
	 {
		  this.type = type;
	 }

	 public long getId()
	 {
		  return id;
	 }

	 public void setId(long id)
	 {
		  this.id = id;
	 }

	 public String getName()
	 {
		  return name;
	 }

	 public void setName(String name)
	 {
		  this.name = name;
	 }

	 public RangeOfValues getRangeOfValues()
	 {
		  return rangeOfValues;
	 }

	 public void setRangeOfValues(RangeOfValues rangeOfValues)
	 {
		  this.rangeOfValues = rangeOfValues;
	 }

	 public Set<DefaultOperation> getOperations()
	 {
		  return operations;
	 }

	 public void setOperations(Set<DefaultOperation> operations)
	 {
		  this.operations = operations;
	 }

	 public Project getProject()
	 {
		  return project;
	 }

	 public void setProject(Project project)
	 {
		  this.project = project;
	 }

	 public String getDescription()
	 {
		  return description;
	 }

	 public void setDescription(String description)
	 {
		  this.description = description;
	 }

	 public Unit getMeasurementUnit()
	 {
	 	 return measurementUnit;
	 }

	 public void setMeasurementUnit(Unit measurementUnit)
	 {
	 	 this.measurementUnit = measurementUnit;
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((description == null) ? 0 : description.hashCode());
		  result = prime * result + (int) (id ^ (id >>> 32));
		  result = prime * result + ((measurementUnit == null) ? 0 : measurementUnit.hashCode());
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
		  if (description == null)
		  {
				if (other.description != null)
					 return false;
		  } else if (!description.equals(other.description))
				return false;
		  if (id != other.id)
				return false;
		  if (measurementUnit == null)
		  {
				if (other.measurementUnit != null)
					 return false;
		  } else if (!measurementUnit.equals(other.measurementUnit))
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
