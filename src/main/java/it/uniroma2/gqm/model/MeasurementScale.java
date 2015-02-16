package it.uniroma2.gqm.model;

import org.appfuse.model.BaseObject;

public class MeasurementScale extends BaseObject {


	private static final long serialVersionUID = -4900033895686795409L;
	
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

}
