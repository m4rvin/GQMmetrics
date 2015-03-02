package it.uniroma2.gqm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "default_operation")
@NamedQueries({
    @NamedQuery(
   			name = "findDefaultOperationBySupportedMeasurementScale",
   			query = "select do.id, do.operation, do.operandsNumber from DefaultOperation do where do.measurementScaleType <= :measurementScaleType"
    )
})
public class DefaultOperation extends BaseObject{

	/**
	 * 
	 */
	private static final long serialVersionUID = 4180691112551983728L;
	
	
	@Id
	@Column(name = "defaultoperation_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	private String operation;
	
	@Column(name = "operands_number")
	private int operandsNumber;
	
	@Column(name = "measurement_scale_type")
	private MeasurementScaleTypeEnum measurementScaleType;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getOperation() {
		return operation;
	}
	public void setOperation(String operation) {
		this.operation = operation;
	}
	public int getOperandsNumber() {
		return operandsNumber;
	}
	public void setOperandsNumber(int operandsNumber) {
		this.operandsNumber = operandsNumber;
	}
	
	public MeasurementScaleTypeEnum getMeasurementScaleType() {
		return measurementScaleType;
	}
	public void setMeasurementScaleType(
			MeasurementScaleTypeEnum measurementScaleType) {
		this.measurementScaleType = measurementScaleType;
	}
	@Override
	public String toString() {
		return "DefaultOperation [id=" + id + ", operation=" + operation
				+ ", operandsNumber=" + operandsNumber
				+ ", measurementScaleType=" + measurementScaleType + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime
				* result
				+ ((measurementScaleType == null) ? 0 : measurementScaleType
						.hashCode());
		result = prime * result + operandsNumber;
		result = prime * result
				+ ((operation == null) ? 0 : operation.hashCode());
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
		DefaultOperation other = (DefaultOperation) obj;
		if (id != other.id)
			return false;
		if (measurementScaleType != other.measurementScaleType)
			return false;
		if (operandsNumber != other.operandsNumber)
			return false;
		if (operation == null) {
			if (other.operation != null)
				return false;
		} else if (!operation.equals(other.operation))
			return false;
		return true;
	}
	
	
}
