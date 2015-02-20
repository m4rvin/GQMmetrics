package it.uniroma2.gqm.model;


import org.appfuse.model.BaseObject;

public class RangeOfValues extends BaseObject{

	private static final long serialVersionUID = -5237393676634716606L;
	
	private long id;
	private String name;
	private boolean defaultRange;
	private boolean numeric;
	private String numberType;
	private boolean range;
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

	public boolean isNumeric() {
		return numeric;
	}

	public void setNumeric(boolean numeric) {
		this.numeric = numeric;
	}

	public boolean isRange() {
		return range;
	}

	public void setRange(boolean range) {
		this.range = range;
	}

	public boolean isDefaultRange() {
		return defaultRange;
	}

	public void setDefaultRange(boolean defaultRange) {
		this.defaultRange = defaultRange;
	}	

	public String getNumberType() {
		return numberType;
	}

	public void setNumberType(String numberType) {
		this.numberType = numberType;
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
