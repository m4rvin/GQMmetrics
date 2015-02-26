package it.uniroma2.gqm.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "default_operation")
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
	@Override
	public String toString() {
		return "DefaultOperation [id=" + id + ", operation=" + operation
				+ ", operandsNumber=" + operandsNumber + "]";
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
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
