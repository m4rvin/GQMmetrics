package it.uniroma2.gqm.model;

import java.util.Set;

import org.appfuse.model.BaseObject;

public class MeasurementScale extends BaseObject {


	private static final long serialVersionUID = -4900033895686795409L;
	
	private long id;
	private String name;
	private RangeOfValues rangeOfValues;
	private Set<Operation> operations;
	private MeasurementScaleTypeEnum type;

	@Override
	public String toString() {

		return null;
	}

	@Override
	public boolean equals(Object o) {

		return false;
	}

	@Override
	public int hashCode() {
		
		return 0;
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

	public Set<Operation> getOperations() {
		return operations;
	}

	public void setOperations(Set<Operation> operations) {
		this.operations = operations;
	}
	
	

}
