package it.uniroma2.gqm.model;

import java.io.Serializable;
import java.util.ArrayList;

import org.appfuse.model.BaseObject;

public class RangeOfValues extends BaseObject implements Serializable{

	private static final long serialVersionUID = -5237393676634716606L;
	
	private long id;
	private String name;
	private MeasurementScaleTypeEnum measurementScaleType;
	
	private Class<?> valueType;
	private ArrayList<Object> values;
	private boolean isFinite;
	
	private Project project;
	
	
	public long getId(){
		return this.id;
	}
	
	public void setId(Long id){
		this.id = id;
	}
	
	public String getName(){
		return this.name;
	}
	
	public void setName(String name){
		this.name = name;
	}
	
	public Class<?> getValueType()
	{
		return valueType;
	}
	
	public ArrayList<Object> getValues()
	{
		if(this.isFinite)
			return this.values;
		return null;
	}
	
	public void addValues(Object... values)
	{
		for(Object value : values)
		{
			this.values.add(value);
		}
	}
	
	public void setValues(ArrayList<Object> values)
	{
		this.values = values;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean equals(Object o) {
		
		if(o == null)
			return false;
		if(!o.getClass().equals(this.getClass()))
			return false;
		if(this.isFinite != ((RangeOfValues)o).isFinite)
			return false;
		if(this.valueType != ((RangeOfValues)o).valueType)
			return false;
		if(!this.isFinite)
			return true;
		else
			return this.values.equals(((RangeOfValues)o).values);
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public MeasurementScaleTypeEnum getMeasurementScaleType() {
		return measurementScaleType;
	}

	public void setMeasurementScaleType(MeasurementScaleTypeEnum measurementScaleType) {
		this.measurementScaleType = measurementScaleType;
	}


}
