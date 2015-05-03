package it.uniroma2.gqm.model;
import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

public class RangeOfValuesEqualTest
{
	 
	 private RangeOfValues rov;
	 private RangeOfValues rov1;

	 @Before
	 public void initialize()
	 {
		  rov = new RangeOfValues();
		  rov1 = new RangeOfValues();
	 }
	 
	 
	 @Test
	 public void testEqualityOfRangeOfValues()
	 {
		  String rov = "[1:10],[20:25],[26:30]";
		  String rov1  = "[1:10],[20:30]";
		  boolean result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.INTEGER_NUMBERS);
		  Assert.assertTrue(result);
		  result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.REAL_NUMBERS);
		  Assert.assertFalse(result);
	 }
	 
	 @Test
	 public void testInclusion1()
	 {
		  rov.setRangeValues("[-inf:100]");
		  rov1.setRangeValues("[-1000:-500], [0:50]");
		  
		  rov.setNumberType(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  rov1.setNumberType(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  
		  rov.setNumeric(true);
		  rov1.setNumeric(true);
		  
		  rov.setRange(true);
		  rov1.setRange(true);
				
		  boolean result = rov1.isIncluded(rov);
		  
		  Assert.assertTrue(result);
		  
		  
		  rov.setRangeValues("[-inf:inf]");
		  rov1.setRangeValues("[-inf:-500], [0:inf]");
		  
		  result = rov1.isIncluded(rov);
		  
		  Assert.assertTrue(result);
	 }
	 
	 @Test
	 public void testInclusion2()
	 {
		  rov.setRangeValues("[-inf:100]");
		  rov1.setRangeValues("[-1000:-500], [0:50], [100:200]");
		  
		  rov.setNumberType(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  rov1.setNumberType(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  
		  rov.setNumeric(true);
		  rov1.setNumeric(true);
		  
		  rov.setRange(true);
		  rov1.setRange(true);
				
		  boolean result = rov1.isIncluded(rov);
		  
		  Assert.assertFalse(result);
		  
		  
		  rov.setRangeValues("[-inf:50]");
		  rov1.setRangeValues("[-inf:-500], [0:100]");
		  
		  result = rov1.isIncluded(rov);
		  
		  Assert.assertFalse(result);
	 }
	 
	 
	 @Test
	 public void testSortAndMerge1()
	 {
		  RangeOfValues rov = new RangeOfValues();
		  
		  rov.setNumeric(true);
		  rov.setRange(true);
		  
		  rov.setRangeValues("[-inf:0],[-20:20],[30:40],[32:38],[25:40],[35:50]");
		  rov.setNumberType(DefaultRangeOfValuesEnum.INTEGER_NUMBERS.toString());
		  
		  rov.sortAndMerge();
		  
		  Assert.assertEquals("[-inf:20.0],[25.0:50.0]", rov.getRangeValues());
		  
	 }
	 
	 @Test
	 public void testSortAndMerge2()
	 {
		  RangeOfValues rov = new RangeOfValues();
		  
		  rov.setNumeric(true);
		  rov.setRange(true);
		  
		  rov.setRangeValues("[-inf:inf],[10:20],[24.5:30]");
		  rov.setNumberType(DefaultRangeOfValuesEnum.REAL_NUMBERS.toString());
		  
		  rov.sortAndMerge();
		  
		  Assert.assertEquals("[-inf:inf]", rov.getRangeValues());
		  
	 }
	 
	 @Test
	 public void testSortAndMerge3()
	 {
		  RangeOfValues rov = new RangeOfValues();
		  
		  rov.setNumeric(true);
		  rov.setRange(true);
		  
		  rov.setRangeValues("[-100:2.5],[23.4:25],[-50:-40.4],[0:10]");
		  rov.setNumberType(DefaultRangeOfValuesEnum.REAL_NUMBERS.toString());
		  
		  rov.sortAndMerge();
		  
		  Assert.assertEquals("[-100.0:10.0],[23.4:25.0]", rov.getRangeValues());
		  
	 }
	 
	 
	 
	 
	 @Test
	 public void testRangeOfValuesInclusion()
	 {
		  RangeOfValues rov = new RangeOfValues();
		  RangeOfValues other = new RangeOfValues();
		  
		  rov.setNumeric(false);
		  other.setNumeric(false);
		  
		  rov.setRangeValues("S,XL,L,M,XXL");
		  other.setRangeValues("XXS,XS,S,M,L,XL,XXL");
		  Assert.assertTrue(rov.isIncluded(other));
		  
		  other.setRangeValues("XXS,XS,S,M,L,XL");
		  Assert.assertFalse(rov.isIncluded(other));
		  
		  rov.setNumeric(true);
		  other.setNumeric(true);
		  
		  rov.setRange(true);
		  other.setRange(false);
		  
		  rov.setRangeValues("[1:4],[6:8]");
		  other.setRangeValues("0,1,2,3,4,5,6,7,8,9");
		  
		  rov.setNumberType("NATURAL_NUMBERS");
		  Assert.assertTrue(rov.isIncluded(other));
		  
		  rov.setNumberType("REAL_NUMBERS");
		  Assert.assertFalse(rov.isIncluded(other));
		  
		  rov.setRange(false);
		  other.setRange(true);
		  
		  rov.setRangeValues("1.3,4,5,6,7");
		  other.setRangeValues("[1:10]");
		  
		  other.setNumberType("REAL_NUMBERS");
		  Assert.assertTrue(rov.isIncluded(other));
		  
		  other.setNumberType("NATURAL_NUMBERS");
		  Assert.assertFalse(rov.isIncluded(other));
		  
		  rov.setRange(true);
		  
		  rov.setRangeValues("[-20:20],[50:100]");
		  other.setRangeValues("[-50:1000]");
		  
		  rov.setNumberType("INTEGER_NUMBERS");
		  other.setNumberType("INTEGER_NUMBERS");
		  Assert.assertTrue(rov.isIncluded(other));
		  
		  rov.setNumberType("REAL_NUMBERS");
		  Assert.assertFalse(rov.isIncluded(other));
		  
	 }
	 
	 
}
