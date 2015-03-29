package it.uniroma2.gqm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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
import javax.persistence.Transient;
import javax.persistence.UniqueConstraint;

import org.appfuse.model.BaseObject;

@Entity
@Table(name = "range_of_values", uniqueConstraints = @UniqueConstraint(columnNames = { "measurement_scale_type", "number_type", "range_values" }))
@NamedQueries({ @NamedQuery(name = "findRangeOfValuesByProject", query = "select r from RangeOfValues r where r.project.id= :project_id "), @NamedQuery(name = "findRangeOfValuesBySupportedMeasurementScale", query = "select r.id, r.name from RangeOfValues r where r.measurementScaleType >= :measurementScaleType"), @NamedQuery(name = "findRangeOfValuesOBJBySupportedMeasurementScale", query = "select r from RangeOfValues r where r.measurementScaleType >= :measurementScaleType") })
public class RangeOfValues extends BaseObject
{

	 private static final long serialVersionUID = -5237393676634716606L;

	 @Id
	 @Column(name = "rangeofvalues_id")
	 @GeneratedValue(strategy = GenerationType.AUTO)
	 private long id;

	 @Column(unique = true)
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

	 // optional
	 @Column(name = "is_finite")
	 private boolean isFinite;

	 // relationship with other classes
	 @ManyToOne(fetch = FetchType.LAZY)
	 @JoinColumn(name = "project_id", nullable = false)
	 private Project project;

	 public long getId()
	 {
		  return this.id;
	 }

	 public void setId(Long id)
	 {
		  this.id = id;
	 }

	 public String getName()
	 {
		  return this.name;
	 }

	 public void setName(String name)
	 {
		  this.name = name;
	 }

	 public boolean isNumeric()
	 {
		  return numeric;
	 }

	 public void setNumeric(boolean numeric)
	 {
		  this.numeric = numeric;
	 }

	 public boolean isRange()
	 {
		  return range;
	 }

	 public void setRange(boolean range)
	 {
		  this.range = range;
	 }

	 public boolean isDefaultRange()
	 {
		  return defaultRange;
	 }

	 public void setDefaultRange(boolean defaultRange)
	 {
		  this.defaultRange = defaultRange;
	 }

	 public String getNumberType()
	 {
		  return numberType;
	 }

	 public void setNumberType(String numberType)
	 {
		  this.numberType = numberType;
	 }

	 public String getRangeValues()
	 {
		  return rangeValues;
	 }

	 public void setRangeValues(String rangeValues)
	 {
		  this.rangeValues = rangeValues;
	 }

	 public Project getProject()
	 {
		  return project;
	 }

	 public void setProject(Project project)
	 {
		  this.project = project;
	 }

	 public MeasurementScaleTypeEnum getMeasurementScaleType()
	 {
		  return measurementScaleType;
	 }

	 public void setMeasurementScaleType(MeasurementScaleTypeEnum measurementScaleType)
	 {
		  this.measurementScaleType = measurementScaleType;
	 }

	 @Override
	 public String toString()
	 {
		  return "RangeOfValues [id=" + id + ", name=" + name + ", defaultRange=" + defaultRange + ", numeric=" + numeric + ", numberType=" + numberType + ", range=" + range + ", rangeValues=" + rangeValues + ", measurementScaleType=" + measurementScaleType + ", isFinite=" + isFinite + ", project=" + project + "]";
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

	 public boolean isIncluded(RangeOfValues other)
	 {
		  if (this.defaultRange && other.isDefaultRange()) //both default range. Just check the inclusion natural < integer < real
				return DefaultRangeOfValuesEnum.valueOf(this.rangeValues).ordinal() <= DefaultRangeOfValuesEnum.valueOf(other.getRangeValues()).ordinal();
 
		  else if(this.defaultRange && !other.isDefaultRange()) //always false, a default range can't be included in a custom range (default ranges are only numeric)
				return false;
		  
		  else if(!this.defaultRange && other.isDefaultRange())
		  {
				if(!this.isNumeric()) //default range are numeric. a non numeric range can't be included in a numeric one
					 return false;
				else //both numeric, just check the number type of the custom one
					 return DefaultRangeOfValuesEnum.valueOf(this.numberType).ordinal() <= DefaultRangeOfValuesEnum.valueOf(other.getRangeValues()).ordinal();
		  }
		  else
		  // both custom range
		  {
				if (this.numeric != other.isNumeric())
					 return false;

				if (!this.numeric || (!this.range && !other.isRange())) // non
																						  // numeric
																						  // or both
																						  // non range
				{
					 List<String> values = Arrays.asList(this.rangeValues.split(","));
					 List<String> otherValues = Arrays.asList(other.getRangeValues().split(","));
					 for (String value : values)
					 {
						  if (!otherValues.contains(value))
								return false;
					 }
					 return true;
				} else
				// numeric
				{
					 if (!this.range && other.isRange())
					 {
						  String[] ranges = other.getRangeValues().split(",");
						  String[] values = this.rangeValues.split(",");
						  for (String value : values)
						  {
								boolean found = false;
								for (String range : ranges)
								{
									 Range r = new Range(range);
									 if (r.includes(Double.valueOf(value), DefaultRangeOfValuesEnum.valueOf(other.numberType)))
									 {
										  found = true;
										  break;
									 }
								}
								if (!found)
									 return false;
						  }
						  return true;

					 } else if (this.range && !other.isRange())
					 {
						  String[] ranges = this.rangeValues.split(",");

						  List<Double> otherList = RangeOfValues.getOrderedListOfDoubles(other.getRangeValues());

						  for (String range : ranges)
						  {
								Range r = new Range(range);
								if (!r.isIncluded(otherList, DefaultRangeOfValuesEnum.valueOf(this.numberType)))
									 return false;
						  }
						  return true;
					 } else
					 {
						  String[] otherRanges = other.getRangeValues().split(",");
						  String[] myRanges = this.rangeValues.split(",");
						  for (String myRange : myRanges)
						  {
								boolean found = false;
								Range myR = new Range(myRange);
								for (String otherRange : otherRanges)
								{
									 Range otherR = new Range(otherRange);
									 if (otherR.includes(myR, DefaultRangeOfValuesEnum.valueOf(this.numberType), DefaultRangeOfValuesEnum.valueOf(other.getNumberType())))
									 {
										  found = true;
										  break;
									 }
								}
								if (!found)
									 return false;
						  }
						  return true;
					 }
				}
		  }
	 }

	 @Override
	 public boolean equals(Object o)
	 {

		  if (o == null)
				return false;
		  if (!o.getClass().equals(this.getClass()))
				return false;
		  if (this.defaultRange != ((RangeOfValues) o).defaultRange)
				return false;
		  else if (this.defaultRange && this.numberType.equals(((RangeOfValues) o).numberType))
		  {
				if (this.measurementScaleType != null)
				{
					 if (this.measurementScaleType == ((RangeOfValues) o).measurementScaleType)
						  return true;
					 else
						  return false;
				}
				return true;
		  } else if (!this.defaultRange)
		  {
				if (this.numeric != ((RangeOfValues) o).numeric)
					 return false;
				if (!this.numeric)
					 return this.rangeValues.equals(((RangeOfValues) o).rangeValues);
				else
				{
					 if (this.range != ((RangeOfValues) o).range)
						  return false;
					 if (!this.range)
						  return setEquality(this.rangeValues, ((RangeOfValues) o).rangeValues);
					 else
					 {
						  try
						  {
								return rangeEquality(this.rangeValues, ((RangeOfValues) o).rangeValues, DefaultRangeOfValuesEnum.valueOf(this.numberType));
						  } catch (IllegalArgumentException e)
						  {
								return false;
						  }
					 }
				}
		  }
		  return false;
		  // TESTME
	 }

	 /*
	  * Returns true if the sets are equivalent, i.e. if they contains the same
	  * elements
	  */

	 @Transient
	 public static boolean setEquality(String set1, String set2)
	 {
		  Set<String> hashSet1 = new HashSet<String>(Arrays.asList(set1.split(",")));
		  Set<String> hashSet2 = new HashSet<String>(Arrays.asList(set2.split(",")));
		  return hashSet1.equals(hashSet2);
	 }

	 /*
	  * Returns true if the two range are equivalent (ex. rangeEquality("[1:10]",
	  * "[1:5],[6:10]") ---> true) under the assumption that the range are well
	  * formed, i.e. in ascending order and without overlappings
	  */
	 @Transient
	 public static boolean rangeEquality(String range1, String range2, DefaultRangeOfValuesEnum type)
	 {
		  Range firstRange = new RangeOfValues.Range(range1.split(",")[0]);
		  Range secondRange = new RangeOfValues.Range(range2.split(",")[0]);

		  if (firstRange.equals(secondRange))
				return true;
		  if (range1.equals("") || range2.equals(""))
				return false;

		  if (range1.split(",")[0].equals(range2.split(",")[0]))
		  {
				try
				{
					 range1 = range1.substring(range1.indexOf(","));
				} catch (IndexOutOfBoundsException e)
				{
					 range1 = "";
				}
				try
				{
					 range2 = range2.substring(range2.indexOf(","));
				} catch (IndexOutOfBoundsException e)
				{
					 range2 = "";
				}
				rangeEquality(range1, range2, type);
		  }

		  if (firstRange.getFirst() != secondRange.getFirst())
				return false;
		  if (firstRange.getLast() > secondRange.getLast())
		  {
				switch (type)
				{
				case REAL_NUMBERS:
					 firstRange.setFirst(secondRange.getLast());
					 break;
				default:
					 firstRange.setFirst(secondRange.getLast() + 1);
					 break;
				}
				range1 = deleteFirstRange(range1);
				range2 = deleteFirstRange(range2);
				range1 = firstRange.toString() + "," + range1;
				return rangeEquality(range1, range2, type);
		  } else
		  {
				switch (type)
				{
				case REAL_NUMBERS:
					 secondRange.setFirst(firstRange.getLast());
					 break;
				default:
					 secondRange.setFirst(firstRange.getLast() + 1);
					 break;
				}
				range1 = deleteFirstRange(range1);
				range2 = deleteFirstRange(range2);
				range2 = secondRange.toString() + "," + range2;
				return rangeEquality(range1, range2, type);
		  }

	 }

	 private static List<Double> getOrderedListOfDoubles(String range)
	 {
		  String[] rangeArr = range.split(",");
		  List<Double> ret = new ArrayList<Double>();
		  for (String r : rangeArr)
		  {
				ret.add(Double.parseDouble(r));
		  }
		  Collections.sort(ret);
		  return ret;
	 }

	 /*
	  * Deletes the first occurrence of a range pattern from a valid range of
	  * values string
	  */
	 private static String deleteFirstRange(String range)
	 {
		  int commaIndex = range.indexOf(",");
		  if (range.indexOf(",") == -1)// only one range pattern
				return "";
		  return range.substring(commaIndex + 1);
	 }

	 private static class Range
	 {

		  private double first;
		  private double last;

		  public Range(String format)
		  {
				format = format.replaceAll("\\[", "");
				format = format.replaceAll("\\]", "");
				String[] arr = format.split(":");
				this.first = Double.parseDouble(arr[0]);
				this.last = Double.parseDouble(arr[1]);
		  }

		  public double getFirst()
		  {
				return first;
		  }

		  public void setFirst(double first)
		  {
				this.first = first;
		  }

		  public double getLast()
		  {
				return last;
		  }

		  @Override
		  public int hashCode()
		  {
				final int prime = 31;
				int result = 1;
				long temp;
				temp = Double.doubleToLongBits(first);
				result = prime * result + (int) (temp ^ (temp >>> 32));
				temp = Double.doubleToLongBits(last);
				result = prime * result + (int) (temp ^ (temp >>> 32));
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
				Range other = (Range) obj;
				if (Double.doubleToLongBits(first) != Double.doubleToLongBits(other.first))
					 return false;
				if (Double.doubleToLongBits(last) != Double.doubleToLongBits(other.last))
					 return false;
				return true;
		  }

		  @Override
		  public String toString()
		  {
				return "[" + this.first + ":" + this.last + "]";
		  }

		  public boolean includes(Double number, DefaultRangeOfValuesEnum type)
		  {
				if (this.getFirst() > number || this.getLast() < number)
					 return false;
				else
				{
					 if (type == DefaultRangeOfValuesEnum.INTEGER_NUMBERS || type == DefaultRangeOfValuesEnum.NATURAL_NUMBERS)
					 {
						  if (number % 1 == 0) // number is a whole
								return true;
						  else
								return false;
					 } else
						  return true;
				}
		  }

		  public boolean isIncluded(List<Double> list, DefaultRangeOfValuesEnum type)
		  {
				if (type == DefaultRangeOfValuesEnum.REAL_NUMBERS)
					 return false;
				for (double i = this.getFirst(); i < this.getLast() + 1; i++)
				{
					 if (!list.contains(i))
						  return false;
				}
				return true;
		  }

		  public boolean includes(Range other, DefaultRangeOfValuesEnum otherType, DefaultRangeOfValuesEnum myType)
		  {
				if (otherType == DefaultRangeOfValuesEnum.REAL_NUMBERS && myType != DefaultRangeOfValuesEnum.REAL_NUMBERS)
					 return false;
				if (other.getFirst() >= this.getFirst() && other.getLast() <= this.getLast())
					 return true;
				else
					 return false;
		  }
	 }
}
