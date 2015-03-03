package it.uniroma2.gqm.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "range_of_values")
@NamedQueries({
    @NamedQuery(
            name = "findRangeOfValuesByProject",
            query = "select r from RangeOfValues r where r.project.id= :project_id "
    ),
    @NamedQuery(
   			name = "findRangeOfValuesBySupportedMeasurementScale",
   			query = "select r.id, r.name from RangeOfValues r where r.measurementScaleType >= :measurementScaleType"
    ),
    @NamedQuery(
   			name = "findRangeOfValuesOBJBySupportedMeasurementScale",
   			query = "select r from RangeOfValues r where r.measurementScaleType >= :measurementScaleType"
    )
})

public class RangeOfValues extends BaseObject{

	private static final long serialVersionUID = -5237393676634716606L;
	
	@Id
	@Column(name = "rangeofvalues_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	
	private String name;
	
	@Column(name = "is_default_range")
	private boolean defaultRange;
	
	@Column(name = "is_numeric")
	private boolean numeric;
	
	@Column(name = "number_type")
	private String numberType;
	
	@Column(name = "is_range")
	private boolean range;
	
	@Column(name = "range_values")
	private String rangeValues;
	
	@Column(name = "measurement_scale_type")
	private MeasurementScaleTypeEnum measurementScaleType;

	//optional
	@Column(name = "is_finite")
	private boolean isFinite;
	
	//relationship with other classes
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
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
		return "RangeOfValues [id=" + id + ", name=" + name + ", defaultRange="
				+ defaultRange + ", numeric=" + numeric + ", numberType="
				+ numberType + ", range=" + range + ", rangeValues="
				+ rangeValues + ", measurementScaleType="
				+ measurementScaleType + ", isFinite=" + isFinite
				+ ", project=" + project + "]";
	}

   @Override
   public int hashCode()
   {
   	 final int prime = 31;
   	 int result = 1;
   	 result = prime * result + (defaultRange ? 1231 : 1237);
   	 result = prime * result + (int) (id ^ (id >>> 32));
   	 result = prime * result + (isFinite ? 1231 : 1237);
   	 result = prime * result + ((measurementScaleType == null) ? 0 : measurementScaleType.hashCode());
   	 result = prime * result + ((name == null) ? 0 : name.hashCode());
   	 result = prime * result + ((numberType == null) ? 0 : numberType.hashCode());
   	 result = prime * result + (numeric ? 1231 : 1237);
   	 result = prime * result + ((project == null) ? 0 : project.hashCode());
   	 result = prime * result + (range ? 1231 : 1237);
   	 result = prime * result + ((rangeValues == null) ? 0 : rangeValues.hashCode());
   	 return result;
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
}
