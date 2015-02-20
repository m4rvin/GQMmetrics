package it.uniroma2.gqm.model;


import org.appfuse.model.BaseObject;

public class RangeOfValues extends BaseObject{

	private static final long serialVersionUID = -5237393676634716606L;
	
	private long id;
	private String name;
	private boolean isDefaultRange;
	private boolean isNumeric;
	private String numberType;
	private boolean isRange;
	private String rangeValues;
	private MeasurementScaleTypeEnum measurementScaleType;

	//optional
	private boolean isFinite;
	
	//relationship with other classes
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
	
	public boolean isDefaultRange() {
		return isDefaultRange;
	}

	public void setDefaultRange(boolean isDefaultRange) {
		this.isDefaultRange = isDefaultRange;
	}

	public boolean isNumeric() {
		return isNumeric;
	}

	public void setNumeric(boolean isNumeric) {
		this.isNumeric = isNumeric;
	}

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
	}

	public boolean isRange() {
		return isRange;
	}

	public void setRange(boolean isRange) {
		this.isRange = isRange;
	}

	public String getRangeValues() {
		return rangeValues;
	}

	public void setRangeValues(String rangeValues) {
		this.rangeValues = rangeValues;
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
		if(!this.isFinite)
			return true;
		
		return false;
		//FIXME
	}

	@Override
	public int hashCode() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	

}
