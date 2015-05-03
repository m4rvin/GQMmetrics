package it.uniroma2.gqm.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
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
@Table(name = "range_of_values", uniqueConstraints = @UniqueConstraint(columnNames = {"number_type", "range_values" }))
@NamedQueries({ 
	@NamedQuery(name = "findRangeOfValuesByProject", query = "select r from RangeOfValues r where r.project.id= :project_id "), 
})
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

	 @Override
	 public String toString()
	 {
		  return "RangeOfValues [id=" + id + ", name=" + name + ", defaultRange=" + defaultRange + ", numeric=" + numeric + ", numberType=" + numberType + ", range=" + range + ", rangeValues=" + rangeValues + ", isFinite=" + isFinite + ", project=" + project + "]";
	 }

	 @Override
	 public int hashCode()
	 {
		  final int prime = 31;
		  int result = 1;
		  result = prime * result + (defaultRange ? 1231 : 1237);
		  result = prime * result + (int) (id ^ (id >>> 32));
		  result = prime * result + (isFinite ? 1231 : 1237);
		  result = prime * result + ((name == null) ? 0 : name.hashCode());
		  result = prime * result + ((numberType == null) ? 0 : numberType.hashCode());
		  result = prime * result + (numeric ? 1231 : 1237);
		  result = prime * result + ((project == null) ? 0 : project.hashCode());
		  result = prime * result + (range ? 1231 : 1237);
		  result = prime * result + ((rangeValues == null) ? 0 : rangeValues.hashCode());
		  return result;
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
				return true;
		  else if (!this.defaultRange)
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
						  } catch (IllegalArgumentException a) //in case of rangeEquality called on error submit with values not consistent
						  {
								return this.rangeValues.equals(((RangeOfValues)o).rangeValues);
						  }
						  catch (ArrayIndexOutOfBoundsException a) {
								return this.rangeValues.equals(((RangeOfValues)o).rangeValues);
						}
					 }
				}
		  }
		  return false;
		  // TESTME
	 }
	 
	 /**
	  * Check if value is included into the range of values, either when value is a measurement input for the metric or when it's a metric formula output
	  * @param value The value to check. It can be both a Double or a String
	  * @param asMeasurementInput
	  * @return
	  * @throws NumberFormatException When trying to convert a string into a non valid Double.
	  */
	 public boolean isIncluded(Object value, boolean asMeasurementInput) throws NumberFormatException
	 {		  			
		  if(this.defaultRange)
		  {
			   Double doubleValue;
				
				if(asMeasurementInput)
					 doubleValue = Double.valueOf((String)value);
				else
					 doubleValue = (Double) value;
				
				if(DefaultRangeOfValuesEnum.valueOf(this.numberType) == DefaultRangeOfValuesEnum.REAL_NUMBERS)
					 return true;
				else if(DefaultRangeOfValuesEnum.valueOf(this.numberType) == DefaultRangeOfValuesEnum.INTEGER_NUMBERS)
					 return doubleValue % 1 == 0;
				else // DefaultRangeOfValuesEnum.valueOf(this.numberType) == DefaultRangeOfValuesEnum.NATURAL_NUMBER
					 return doubleValue % 1 == 0 && doubleValue >= 0;
		  }
		  else
		  {
				if(!this.numeric && !asMeasurementInput) // output values of non numeric range must not be checked (always included by default)
					 return true;
				
				if(!this.numeric && asMeasurementInput) //is not numeric and implicitly not range. It check only values as input (output values are always boolean and must not be checked)
				{   
					 String stringValue = (String) value; //it is always as input, so we need only to cast.
					 List<String> rangeList = Arrays.asList(this.rangeValues.split(","));
					 return rangeList.contains(stringValue);
				}
				else //is numeric
				{
					 if(!this.range) //is not range
					 { 
						 String stringValue;
						 if(asMeasurementInput)
							 stringValue = (String) value;
						 else //the Object contains a Double object, so we need to create the String object
							 stringValue = String.valueOf(value);
						 List<String> rangeList = Arrays.asList(this.rangeValues.split(","));
						 return rangeList.contains(stringValue);
						
					 }
					 else //is range
					 {
						  Double doubleValue;
							
						  if(asMeasurementInput)
							  doubleValue = Double.valueOf((String)value);
						  else
							  doubleValue = (Double) value;
						  
						  List<String> rangeList = Arrays.asList(this.rangeValues.split(","));
						  for(String range : rangeList)
						  {
								Range r = new Range(range);
								if(r.includes(doubleValue, DefaultRangeOfValuesEnum.valueOf(this.numberType)))
									 return true;
						  }
						  return false;
					 }
				}		
		  }
	 }

	 /**
	  * Retrieve the Double value associated to the specified Object {@code value} for (NOT defaultRange && NOT numeric)
	  * @param value The value from which retrieve the associated Double
	  * @return The Double related value of the String {@code value}
	  * @throws NumberFormatException
	  */
	 public Double getStringValueAsNumberByIndex(String value)
	 {	
		 //NOT numeric. (always a String)
		 if(!this.isNumeric()){
			  String stringValue = (String) value;
			  List<String> rangeList = Arrays.asList(this.rangeValues.split(","));
			  return new Double(rangeList.indexOf(stringValue));
		 }
		 else{//numeric (or numeric and defaultrange)
			 return Double.valueOf(value);//a string representing a number
			 
		 }
	 }
	 

	 public boolean isIncluded(RangeOfValues other)
	 {
		  if (this.defaultRange && other.isDefaultRange()) //both default range. Just check the inclusion natural < integer < real
				return DefaultRangeOfValuesEnum.valueOf(this.numberType).ordinal() <= DefaultRangeOfValuesEnum.valueOf(other.getNumberType()).ordinal();
 
		  else if(this.defaultRange && !other.isDefaultRange()) //always false, a default range can't be included in a custom range (default ranges are only numeric)
				return false;
		  
		  else if(!this.defaultRange && other.isDefaultRange())
		  {
				if(!this.isNumeric()) //default range are numeric. a non numeric range can't be included in a numeric one
					 return false;
				else //both numeric, just check the number type of the custom one
					 return DefaultRangeOfValuesEnum.valueOf(this.numberType).ordinal() <= DefaultRangeOfValuesEnum.valueOf(other.getNumberType()).ordinal();
		  }
		  else // both custom range
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
				} else //numeric ranges
				{
					 if (!this.range && other.isRange()) //this range of values is not a range and other range is a range
					 {
						  String[] ranges = other.getRangeValues().split(",");
						  String[] values = this.rangeValues.split(",");
						  for (String value : values) //check if every value from this range of values list is in other range of value ranges
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

					 } else if (this.range && !other.isRange()) //this range of values is a range and other range is not a range
					 {
						  String[] ranges = this.rangeValues.split(",");

						  List<Double> otherList = RangeOfValues.getOrderedListOfDoubles(other.getRangeValues());

						  for (String range : ranges) //check if every values inside this range is in the other range list
						  {
								Range r = new Range(range);
								if (!r.isIncluded(otherList, DefaultRangeOfValuesEnum.valueOf(this.numberType)))
									 return false;
						  }
						  return true;
					 } else //this range of values is a range and other range is a range
					 {
						  String[] otherRanges = other.getRangeValues().split(",");
						  String[] myRanges = this.rangeValues.split(",");
						  for (String myRange : myRanges)
						  {
								boolean found = false;
								Range myR = new Range(myRange); 
								for (String otherRange : otherRanges) //check if every single range from this is included in other range of values
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
		  
		  if(range1.equals("") && range2.equals(""))
				return true;
		  
		  if (range1.equals("") || range2.equals(""))
				return false;
		  
		  Range firstRange = new RangeOfValues.Range(range1.split(",")[0]);
		  Range secondRange = new RangeOfValues.Range(range2.split(",")[0]);

		  if (firstRange.equals(secondRange))
				return rangeEquality(deleteFirstRange(range1), deleteFirstRange(range2), type);
/*
		  if (firstRange.equals(secondRange))
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
*/
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
	 
	 public void sortAndMerge()
	 {
		  String[] ranges = this.rangeValues.split(",");
		  List<Range> rangeList = new ArrayList<RangeOfValues.Range>();
		  
		  for(String r : ranges)
		  {
				Range range = new Range(r);
				rangeList.add(range);
		  }
		  //merge
		  Collections.sort(rangeList);
		  
		  ListIterator<Range> iterator = rangeList.listIterator();
		  
		  Range currentRange = iterator.next();
		  
		  while(iterator.hasNext())
		  {
				Range nextRange = iterator.next();
				
				Range mergedRange = currentRange.merge(nextRange, DefaultRangeOfValuesEnum.valueOf(this.numberType));
				
				if(mergedRange == null)
					 currentRange = nextRange;
				else
				{
					 iterator.remove();
					 iterator.previous();
					 iterator.set(mergedRange);
					 iterator.next();
					 currentRange = mergedRange;
				}
		  }
		  
		  StringBuilder result = new StringBuilder();
		  
		  for(int i = 0; i < rangeList.size(); i++)
		  {
				if(result.length() != 0)
					 result.append(",");
				result.append(rangeList.get(i).toString());
		  }
		  this.setRangeValues(result.toString());
	 }

	 private static class Range implements Comparable<Range>
	 {

		  private double first;
		  private double last;

		  public Range(String format)
		  {
				format = format.replaceAll("\\[", "");
				format = format.replaceAll("\\]", "");
				String[] arr = format.split(":");
				double value;
				for(int i = 0; i < arr.length; i++)
			   {
					if(arr[i].equals("-inf"))
						 value = Double.NEGATIVE_INFINITY;
					else if(arr[i].equals("inf"))
						 value = Double.POSITIVE_INFINITY;
					else
						 value = Double.parseDouble(arr[i]);
					
					if(i == 0)
						 this.first = value;
					else
						 this.last = value;
			   }
		  }
		  
		  public Range(double first, double last)
		  {
				this.first = first;
				this.last = last;
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
				String result = "[";
				result += (this.first == Double.NEGATIVE_INFINITY) ? "-inf" : this.first;
				result += ":";
				result += ((this.last == Double.POSITIVE_INFINITY)) ? "inf" : this.last;
				result += "]";
				return result;
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
		  
		  public Range merge(Range other, DefaultRangeOfValuesEnum type)
		  {
				if(this.includes(other, type, type))
					 return this;
				
				else if(other.includes(this, type, type))
					 return other;
				
				if(this.last < other.first) //ranges disjoint
					 return null;
				
				else
					 return new Range(this.first, other.last);
		  }

		  @Override
		  public int compareTo(Range o)
		  {
				if(o == null)
					 return 1;
				if(o.first == this.first)
					 return 0;
				return (this.first > o.first) ? 1 : -1;
		  }
	 }
}
