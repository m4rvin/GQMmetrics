package it.uniroma2.gqm.model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;

@Entity
@NamedQueries({
    @NamedQuery(
            name = "findMetricByProject",
            query = "select m from Metric m  where m.project.id= :project_id "
    ),
    @NamedQuery(
            name = "findMeasuredMetric",
            query = "select distinct m from Goal g inner join g.questions gq " +
            		" inner join gq.pk.question q  " +
            		" inner join q.metrics qm " +
            		" inner join qm.pk.metric m " +
            		" where g.id= :goal_id and m.satisfyingConditionValue <> null"
    )
})
public class Metric   extends BaseObject  implements Serializable {

	private static final long serialVersionUID = 7990600814484921752L;
	private Long id;
	private String code;
	private String name;
	private String hypothesis;
	private Unit unit;
	private Project project;
	private MeasurementScale measurementScale;
	private MetricTypeEnum type;
	private CollectingTypeEnum collectingType;
	private SatisfyingConditionOperationEnum satisfyingConditionOperation;
	private User metricOwner;
	private Set<QuestionMetric> questions = new HashSet<QuestionMetric>();
	private Metric metricA;
	private Metric metricB;
	private OperationEnum operation;
	private Double actualValue;
	private Double satisfyingConditionValue;
	private Set<Measurement> measurements= new HashSet<Measurement>();
	
	
	@Id
	@Column(name="metric_id")
	@GeneratedValue(strategy = GenerationType.AUTO) 
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="code", length=50,nullable=false)
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="name", length=255,nullable=false)
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="hypothesis", length=255,nullable=true)
	public String getHypothesis() {
		return hypothesis;
	}
	public void setHypothesis(String hypothesis) {
		this.hypothesis = hypothesis;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
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
		Metric other = (Metric) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "Metric [id=" + id + ", name=" + name + "]";
	}
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "unit_id", nullable = true)	
	public Unit getUnit() {
		return unit;
	}
	public void setUnit(Unit unit) {
		this.unit = unit;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "project_id", nullable = false)
	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "type", length = 50)
	public MetricTypeEnum getType() {
		return type;
	}
	public void setType(MetricTypeEnum type) {
		this.type = type;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "collecting_type", length = 50)
	public CollectingTypeEnum getCollectingType() {
		return collectingType;
	}
	public void setCollectingType(CollectingTypeEnum collectingType) {
		this.collectingType = collectingType;
	}
	
	
	@Enumerated(EnumType.STRING)
	@Column(name = "satisfying_condition_peration", length = 50)
	public SatisfyingConditionOperationEnum getSatisfyingConditionOperation() {
		return satisfyingConditionOperation;
	}
	public void setSatisfyingConditionOperation(
			SatisfyingConditionOperationEnum satisfyingConditionOperation) {
		this.satisfyingConditionOperation = satisfyingConditionOperation;
	}
	@ManyToOne
	@JoinColumn(name = "mmdmo_id", nullable = false)		
	public User getMetricOwner() {
		return metricOwner;
	}
	public void setMetricOwner(User metricOwner) {
		this.metricOwner = metricOwner;
	}
	
	
	@OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.metric", cascade=CascadeType.ALL)
	public Set<QuestionMetric> getQuestions() {
		return questions;
	}
	public void setQuestions(Set<QuestionMetric> questions) {
		this.questions = questions;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metric_a_id", nullable = true)
	public Metric getMetricA() {
		return metricA;
	}
	public void setMetricA(Metric metricA) {
		this.metricA = metricA;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "metric_b_id", nullable = true)
	public Metric getMetricB() {
		return metricB;
	}
	public void setMetricB(Metric metricB) {
		this.metricB = metricB;
	}
	
	@Enumerated(EnumType.STRING)
	@Column(name = "operation", length = 50,nullable=true)	
	public OperationEnum getOperation() {
		return operation;
	}
	public void setOperation(OperationEnum operation) {
		this.operation = operation;
	}
	
	@Column(name="actual_value")
	public Double getActualValue() {
		return actualValue;
	}
	
	public void setActualValue(Double actualValue) {
		this.actualValue = actualValue;
	}
	
	@Column(name="satisfying_condition_value")
	public Double getSatisfyingConditionValue() {
		return satisfyingConditionValue;
	}
	public void setSatisfyingConditionValue(Double satisfyingConditionValue) {
		this.satisfyingConditionValue = satisfyingConditionValue;
	}
	
	@OneToMany(mappedBy="metric")
	public Set<Measurement> getMeasurements() {
		return measurements;
	}
	public void setMeasurements(Set<Measurement> measurements) {
		this.measurements = measurements;
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "measurement_scale_id", nullable = false)
	public MeasurementScale getMeasurementScale()
   {
   	 return measurementScale;
   }
	
   public void setMeasurementScale(MeasurementScale measurementScale)
   {
   	 this.measurementScale = measurementScale;
   }
   
@Transient
	public boolean isConditionReached(){
		boolean ret = false;
		//Double value = null;
		if(this.satisfyingConditionValue != null  && this.satisfyingConditionOperation != null 
				&& satisfyingConditionOperation != SatisfyingConditionOperationEnum.NONE){
			switch (this.satisfyingConditionOperation){
				case EQUAL:
					ret = this.getMeasuredValue() == this.satisfyingConditionValue;
					break;
				case GREATER_OR_EQUAL:
					ret = this.getMeasuredValue() >= this.satisfyingConditionValue;
					break;
				case GREATHER:
					ret = this.getMeasuredValue() > this.satisfyingConditionValue;
					break;
				case LESS:
					ret = this.getMeasuredValue() < this.satisfyingConditionValue;
					break;
				case LESS_OR_EQUAL:
					ret = this.getMeasuredValue() <= this.satisfyingConditionValue;
					break;
				case NONE:
					break;
			}
		}
		return ret;
	}
	
	@Transient
	public Double getMeasuredValue(){
		Double value = Double.NaN;
		try {
			//System.out.println("Metric: " + this.getCode() + " 1" + " value= "  + value);
			if(collectingType == CollectingTypeEnum.MULTIPLE_VALUE ){
				//System.out.println("Metric: " + this.getCode() + " 2" + " value= "  + value);
				if(measurements!=null){
					//System.out.println("Metric: " + this.getCode() + " 3" + " value= "  + value);
					Iterator<Measurement> it = measurements.iterator();
					int n = 0;
					double sum = 0;
					//System.out.println("Metric: " + this.getCode() + " 4" + " value= "  + value);
					while (it.hasNext()) {
						//System.out.println("Metric: " + this.getCode() + " 5" + " value= "  + value);
						Double v = ((Measurement) it.next()).getValue();	
						//System.out.println("Metric: " + this.getCode() + " 6" + " value= "  + value);
						n++;
						sum+=v;
					}
					//System.out.println("Metric: " + this.getCode() + " 7" + " value= "  + value);
					value = sum/n;
					//System.out.println("Metric: " + this.getCode() + " 8" + " value= "  + value);
				}
			} else {
				// It's a composite metrics...
				if(metricA != null && metricA.getId() != null && 
						metricB != null && metricB.getId() != null && 
						operation != null){ 
					//System.out.println("Metric: " + this.getCode() + " 9" + " value= "  + value);
					if(operation == OperationEnum.ADDITION) {
						value = metricA.getMeasuredValue() + metricB.getMeasuredValue();
					}else if(operation == OperationEnum.DIVISION) {
						value = metricA.getMeasuredValue() / metricB.getMeasuredValue();
					}else if(operation == OperationEnum.SUBTRACTION) {
						value = metricA.getMeasuredValue() - metricB.getMeasuredValue();
					}else if(operation == OperationEnum.MULTIPLICATION) {
						value = metricA.getMeasuredValue() * metricB.getMeasuredValue();
					}
					//System.out.println("Metric: " + this.getCode() + " 10" + " value= "  + value);
				} else {
					//System.out.println("Metric: " + this.getCode() + " 11" + " value= "  + value);
					if(measurements!=null){
						//System.out.println("Metric: " + this.getCode() + " 12" + " value= "  + value);
						Iterator<Measurement> it = measurements.iterator();
						int n = 0;
						double sum = 0;
						//System.out.println("Metric: " + this.getCode() + " 13" + " value= "  + value);
						
						while (it.hasNext()) {
							//System.out.println("Metric: " + this.getCode() + " 14" + " value= "  + value);
							Double v = ((Measurement) it.next()).getValue();						
							n++;
							sum+=v;
						}
						if(n>0){
							//System.out.println("Metric: " + this.getCode() + " 15" + " value= "  + value);
							value = sum/n;
							//System.out.println("Metric: " + this.getCode() + " 16" + " value= "  + value);
						}
					}
				}				
			}						
		}catch(Exception ex){
			return  Double.NaN;
		}
		
		System.out.println("Metric: " + this.getCode() + " 17" + " value= "  + value);
		return value;
	}
	
	@Transient
	public String getFormula(){
		String formula = "None";
		if(operation == OperationEnum.ADDITION) {
			formula = metricA.getCode().concat(" ").concat("+").concat(" ").concat(metricB.getCode());
		}else if(operation == OperationEnum.DIVISION) {
			formula = metricA.getCode().concat(" ").concat("/").concat(" ").concat(metricB.getCode());
		}else if(operation == OperationEnum.SUBTRACTION) {
			formula = metricA.getCode().concat(" ").concat("-").concat(" ").concat(metricB.getCode());
		}else if(operation == OperationEnum.MULTIPLICATION) {
			formula = metricA.getCode().concat(" ").concat("*").concat(" ").concat(metricB.getCode());
		}
		return formula;
	}
}	
