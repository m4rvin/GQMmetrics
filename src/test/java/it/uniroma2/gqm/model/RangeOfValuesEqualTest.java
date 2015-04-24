package it.uniroma2.gqm.model;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.Assert;

public class RangeOfValuesEqualTest
{

	 int recursiveFun3(int n)
	 {
	     if (n <= 0)
	         return 1;
	     else
	         return 1 + recursiveFun3(n/5);
	 }
	 
	 @Test
	 public void test()
	 {
		  int res = recursiveFun3(15);
		  System.out.println(res);
	 }
	 
	 
	 @Test
	 public void testEqualityOfRangeOfValues()
	 {
		  String rov = "[1:10],[20:25],[25:30]";
		  String rov1  = "[1:10]";
		  boolean result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.INTEGER_NUMBERS);
		  Assert.assertTrue(result);
		  result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.REAL_NUMBERS);
		  Assert.assertTrue(result);
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
