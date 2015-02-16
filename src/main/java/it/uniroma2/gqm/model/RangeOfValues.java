package it.uniroma2.gqm.model;

import java.util.ArrayList;

import org.appfuse.model.BaseObject;

public class RangeOfValues extends BaseObject {

	private static final long serialVersionUID = -5237393676634716606L;
	
	private Class<?> valueType;
	private ArrayList<Object> values;
	private boolean isFinite;
	
	
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

}
