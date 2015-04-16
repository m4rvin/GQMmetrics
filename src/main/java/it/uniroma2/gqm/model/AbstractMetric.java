package it.uniroma2.gqm.model;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.appfuse.model.BaseObject;
import org.appfuse.model.User;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQueries({ @NamedQuery(name = "findMetricByProject", query = "select m from AbstractMetric m  where m.project.id= :project_id "), @NamedQuery(name = "findMeasuredMetric", query = "select distinct m from Goal g inner join g.questions gq " + " inner join gq.pk.question q  " + " inner join q.metrics qm " + " inner join qm.pk.metric m " + " where g.id= :goal_id and m.satisfyingConditionValue <> null"),
		  @NamedQuery(name = "findByMeasurementScale", query = "select m from AbstractMetric m where m.measurementScale.id = :measurementScaleId"),
		  @NamedQuery(name = "findMetricByMeasurementScaleType", query = "select m from AbstractMetric m where m.measurementScale.type = :type" ),
		  @NamedQuery(name = "findMetricByMeasurementScaleTypeExludingOneById", query = "select m from AbstractMetric m where m.measurementScale.type = :type AND m.id <> :id" ),
		  @NamedQuery(name = "findMetricByName", query = "select m from AbstractMetric m where m.name = :name")})
@DiscriminatorColumn(name = "complexMetricType")
@Table(name = "AbstractMetric", uniqueConstraints = @UniqueConstraint(columnNames = { "name", "type", "measurement_scale_id", "collecting_type", "metric_formula"}))
public class AbstractMetric extends BaseObject
{

	 private static final long serialVersionUID = -4627758431767217159L;

	 protected Long id;
	 protected String code;
	 protected String name;
	 protected Project project;
	 protected MetricTypeEnum type;
	 protected CollectingTypeEnum collectingType; // va definito nella classe
																 // astratta, viene forzato ad
																 // essere single value al
																 // momento della creazione
																 // sulla view
	 protected String hypothesis;
	 protected MeasurementScale measurementScale;
	 protected SatisfyingConditionOperationEnum satisfyingConditionOperation;
	 protected User metricOwner;
	 protected Set<QuestionMetric> questions = new HashSet<QuestionMetric>();
	 protected Double actualValue = null;
	 protected Double satisfyingConditionValue;
	 protected Set<Measurement> measurements = new HashSet<Measurement>();
	 protected Set<CombinedMetric> composerFor;
	 protected String formula;
	 protected MetricOutputValueTypeEnum outputValueType;
	 
	 @Id
	 @Column(name = "metric_id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 public Long getId()
	 {
		  return id;
	 }

	 public void setId(Long id)
	 {
		  this.id = id;
	 }

	 @Column(name = "code", length = 50, nullable = false)
	 public String getCode()
	 {
		  return code;
	 }

	 public void setCode(String code)
	 {
		  this.code = code;
	 }

	 @Column(name = "name", length = 255, nullable = false, unique = true)
	 public String getName()
	 {
		  return name;
	 }

	 public void setName(String name)
	 {
		  this.name = name;
	 }

	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "project_id", nullable = false)
	 public Project getProject()
	 {
		  return project;
	 }

	 public void setProject(Project project)
	 {
		  this.project = project;
	 }

	 @Enumerated(EnumType.STRING)
	 @Column(name = "type", length = 50)
	 public MetricTypeEnum getType()
	 {
		  return type;
	 }

	 public void setType(MetricTypeEnum type)
	 {
		  this.type = type;
	 }

	 @Enumerated(EnumType.STRING)
	 @Column(name = "collecting_type", length = 50)
	 public CollectingTypeEnum getCollectingType()
	 {
		  return collectingType;
	 }

	 public void setCollectingType(CollectingTypeEnum collectingType)
	 {
		  this.collectingType = collectingType;
	 }

	 @Column(name = "hypothesis", length = 255, nullable = true)
	 public String getHypothesis()
	 {
		  return hypothesis;
	 }

	 public void setHypothesis(String hypothesis)
	 {
		  this.hypothesis = hypothesis;
	 }

	 @Enumerated(EnumType.STRING)
	 @Column(name = "satisfying_condition_peration", length = 50)
	 public SatisfyingConditionOperationEnum getSatisfyingConditionOperation()
	 {
		  return satisfyingConditionOperation;
	 }

	 public void setSatisfyingConditionOperation(SatisfyingConditionOperationEnum satisfyingConditionOperation)
	 {
		  this.satisfyingConditionOperation = satisfyingConditionOperation;
	 }

	 @ManyToOne
	 @JoinColumn(name = "mmdmo_id", nullable = false)
	 public User getMetricOwner()
	 {
		  return metricOwner;
	 }

	 public void setMetricOwner(User metricOwner)
	 {
		  this.metricOwner = metricOwner;
	 }

	 @OneToMany(fetch = FetchType.LAZY, mappedBy = "pk.metric", cascade = CascadeType.ALL)
	 public Set<QuestionMetric> getQuestions()
	 {
		  return questions;
	 }

	 public void setQuestions(Set<QuestionMetric> questions)
	 {
		  this.questions = questions;
	 }
	 
	 @Column(name = "metric_formula")
	 public String getFormula()
	 {
	 	 return formula;
	 }

	 public void setFormula(String formula)
	 {
	 	 this.formula = formula;
	 }

	 @Column(name = "actual_value")
	 public Double getActualValue()
	 {
		  return actualValue;
	 }

	 public void setActualValue(Double actualValue)
	 {
		  this.actualValue = actualValue;
	 }

	 @Column(name = "satisfying_condition_value")
	 public Double getSatisfyingConditionValue()
	 {
		  return satisfyingConditionValue;
	 }

	 public void setSatisfyingConditionValue(Double satisfyingConditionValue)
	 {
		  this.satisfyingConditionValue = satisfyingConditionValue;
	 }

	 @OneToMany(mappedBy = "metric")
	 public Set<Measurement> getMeasurements()
	 {
		  return measurements;
	 }

	 public void setMeasurements(Set<Measurement> measurements)
	 {
		  this.measurements = measurements;
	 }

	 @ManyToOne(fetch = FetchType.EAGER)
	 @JoinColumn(name = "measurement_scale_id", nullable = false)
	 public MeasurementScale getMeasurementScale()
	 {
		  return measurementScale;
	 }

	 public void setMeasurementScale(MeasurementScale measurementScale)
	 {
		  this.measurementScale = measurementScale;
	 }

	 @Column
	 @Enumerated(EnumType.STRING)
	 public MetricOutputValueTypeEnum getOutputValueType()
	 {
	 	 return outputValueType;
	 }

	 public void setOutputValueType(MetricOutputValueTypeEnum outputValueType)
	 {
	 	 this.outputValueType = outputValueType;
	 }

	 // @ManyToMany(fetch = FetchType.LAZY)
	 // @JoinTable(name = "metriccomposerfor_metriccomposedby",
	 // joinColumns = {@JoinColumn(name = "metriccomposerfor_id",
	 // referencedColumnName="metric_id")},
	 // inverseJoinColumns = {@JoinColumn(name = "metriccomposedby_id",
	 // referencedColumnName="metric_id")}
	 // )
	 @ManyToMany(mappedBy = "composedBy", fetch = FetchType.EAGER)
	 public Set<CombinedMetric> getComposerFor()
	 {
		  return composerFor;
	 }

	 public void setComposerFor(Set<CombinedMetric> composedFor)
	 {
		  this.composerFor = composedFor;
	 }

	 public void addComposerFor(CombinedMetric metric)
	 {
		  this.composerFor.add(metric);
	 }
	 
	 public void removeComposerFor(CombinedMetric metric)
	 {
		  this.composerFor.remove(metric);
	 }
	 
	 @Transient
	 public boolean isEresable()
	 {
		  return this.questions.size() == 0 && this.composerFor.size() == 0;
	 }

	 /*
	  *  va cancellata da qui la valutaizione della metrica, spostare sul manager
	  */
	 @Transient
	 public boolean isConditionReached()
	 {
		  boolean ret = false;
		  // Double value = null;
		  if (this.satisfyingConditionValue != null && this.satisfyingConditionOperation != null && satisfyingConditionOperation != SatisfyingConditionOperationEnum.NONE)
		  {
				switch (this.satisfyingConditionOperation)
				{
				case EQUAL:
					 ret = this.getMeasuredValue() == this.satisfyingConditionValue;
					 break;
				case GREATER_OR_EQUAL:
					 ret = this.getMeasuredValue() >= this.satisfyingConditionValue;
					 break;
				case GREATER:
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

/*	 @Transient
	 public Double getMeasuredValue()
	 {
		  Double value = Double.NaN;
		  try
		  {
				// System.out.println("Metric: " + this.getCode() + " 1" +
				// " value= " + value);
				if (collectingType == CollectingTypeEnum.MULTIPLE_VALUE)
				{
					 // System.out.println("Metric: " + this.getCode() + " 2" +
					 // " value= " + value);
					 if (measurements != null)
					 {
						  // System.out.println("Metric: " + this.getCode() + " 3" +
						  // " value= " + value);
						  Iterator<Measurement> it = measurements.iterator();
						  int n = 0;
						  double sum = 0;
						  // System.out.println("Metric: " + this.getCode() + " 4" +
						  // " value= " + value);
						  while (it.hasNext())
						  {
								// System.out.println("Metric: " + this.getCode() + " 5"
								// + " value= " + value);
								Double v = ((Measurement) it.next()).getValue();
								// System.out.println("Metric: " + this.getCode() + " 6"
								// + " value= " + value);
								n++;
								sum += v;
						  }
						  // System.out.println("Metric: " + this.getCode() + " 7" +
						  // " value= " + value);
						  value = sum / n;
						  // System.out.println("Metric: " + this.getCode() + " 8" +
						  // " value= " + value);
					 }
				} else
				{
					 // It's a composite metrics...
					 
					  * if(metricA != null && metricA.getId() != null && metricB !=
					  * null && metricB.getId() != null && operation != null){
					  * //System.out.println("Metric: " + this.getCode() + " 9" +
					  * " value= " + value); if(operation == OperationEnum.ADDITION)
					  * { value = metricA.getMeasuredValue() +
					  * metricB.getMeasuredValue(); }else if(operation ==
					  * OperationEnum.DIVISION) { value = metricA.getMeasuredValue()
					  * / metricB.getMeasuredValue(); }else if(operation ==
					  * OperationEnum.SUBTRACTION) { value =
					  * metricA.getMeasuredValue() - metricB.getMeasuredValue();
					  * }else if(operation == OperationEnum.MULTIPLICATION) { value =
					  * metricA.getMeasuredValue() * metricB.getMeasuredValue(); }
					  * //System.out.println("Metric: " + this.getCode() + " 10" +
					  * " value= " + value); }
					  
					 // System.out.println("Metric: " + this.getCode() + " 11" +
					 // " value= " + value);
					 if (measurements != null)
					 {
						  // System.out.println("Metric: " + this.getCode() + " 12" +
						  // " value= " + value);
						  Iterator<Measurement> it = measurements.iterator();
						  int n = 0;
						  double sum = 0;
						  // System.out.println("Metric: " + this.getCode() + " 13" +
						  // " value= " + value);

						  while (it.hasNext())
						  {
								// System.out.println("Metric: " + this.getCode() +
								// " 14" + " value= " + value);
								Double v = ((Measurement) it.next()).getValue();
								n++;
								sum += v;
						  }
						  if (n > 0)
						  {
								// System.out.println("Metric: " + this.getCode() +
								// " 15" + " value= " + value);
								value = sum / n;
								// System.out.println("Metric: " + this.getCode() +
								// " 16" + " value= " + value);
						  }
					 }

				}
		  } catch (Exception ex)
		  {
				return Double.NaN;
		  }

		  System.out.println("Metric: " + this.getCode() + " 17" + " value= " + value);
		  return value;
	 }

*/	 

	 /*
	 @Transient
	 public Double getMeasuredValue(){
		 
		 return null;
	 }*/

	 
	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + ((actualValue == null) ? 0 : actualValue.hashCode());
		  result = prime * result + ((code == null) ? 0 : code.hashCode());
		  result = prime * result + ((collectingType == null) ? 0 : collectingType.hashCode());
		  result = prime * result + ((formula == null) ? 0 : formula.hashCode());
		  result = prime * result + ((hypothesis == null) ? 0 : hypothesis.hashCode());
		  result = prime * result + ((id == null) ? 0 : id.hashCode());
		  result = prime * result + ((measurements == null) ? 0 : measurements.hashCode());
		  result = prime * result + ((metricOwner == null) ? 0 : metricOwner.hashCode());
		  result = prime * result + ((name == null) ? 0 : name.hashCode());
		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  result = prime * result + ((satisfyingConditionOperation == null) ? 0 : satisfyingConditionOperation.hashCode());
		  result = prime * result + ((satisfyingConditionValue == null) ? 0 : satisfyingConditionValue.hashCode());
		  result = prime * result + ((type == null) ? 0 : type.hashCode());
		  return result;
	 }

	 @Override
	 public boolean equals(Object obj)
	 {
		  if (this == obj)
				return true;
		  if (obj == null)
				return false;
		  if (getClass() != obj.getClass())
				return false;
		  AbstractMetric other = (AbstractMetric) obj;
		  if (actualValue == null)
		  {
				if (other.actualValue != null)
					 return false;
		  } else if (!actualValue.equals(other.actualValue))
				return false;
		  if (code == null)
		  {
				if (other.code != null)
					 return false;
		  } else if (!code.equals(other.code))
				return false;
		  if (collectingType != other.collectingType)
				return false;
		  if (composerFor == null)
		  {
				if (other.composerFor != null)
					 return false;
		  } else if (!composerFor.equals(other.composerFor))
				return false;
		  if (formula == null)
		  {
				if (other.formula != null)
					 return false;
		  } else if (!formula.equals(other.formula))
				return false;
		  if (hypothesis == null)
		  {
				if (other.hypothesis != null)
					 return false;
		  } else if (!hypothesis.equals(other.hypothesis))
				return false;
		  if (id == null)
		  {
				if (other.id != null)
					 return false;
		  } else if (!id.equals(other.id))
				return false;
		  if (measurementScale == null)
		  {
				if (other.measurementScale != null)
					 return false;
		  } else if (!measurementScale.equals(other.measurementScale))
				return false;
		  if (measurements == null)
		  {
				if (other.measurements != null)
					 return false;
		  } else if (!measurements.equals(other.measurements))
				return false;
		  if (metricOwner == null)
		  {
				if (other.metricOwner != null)
					 return false;
		  } else if (!metricOwner.equals(other.metricOwner))
				return false;
		  if (name == null)
		  {
				if (other.name != null)
					 return false;
		  } else if (!name.equals(other.name))
				return false;
		  if (project == null)
		  {
				if (other.project != null)
					 return false;
		  } else if (!project.equals(other.project))
				return false;
		  if (questions == null)
		  {
				if (other.questions != null)
					 return false;
		  } else if (!questions.equals(other.questions))
				return false;
		  if (satisfyingConditionOperation != other.satisfyingConditionOperation)
				return false;
		  if (satisfyingConditionValue == null)
		  {
				if (other.satisfyingConditionValue != null)
					 return false;
		  } else if (!satisfyingConditionValue.equals(other.satisfyingConditionValue))
				return false;
		  if (type != other.type)
				return false;
		  return true;
	 }

	 @Override
	 public String toString()
	 {
		  return "AbstractMetric [id=" + id + ", code=" + code + ", name=" + name + "]";
	 }

	 

}
