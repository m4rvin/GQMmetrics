package it.uniroma2.gqm.model;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import junit.framework.Assert;

public class RangeOfValuesEqualTest
{

	 @Autowired
	 private MeasurementScale mockMeasurementScale;
	 
	 @Test
	 public void testEqualityOfRangeOfValues()
	 {
		  this.mockMeasurementScale.toString();
		  String rov = "[1:10],[20:25],[25:30]";
		  String rov1  = "[1:3],[4:10],[20:30]";
		  boolean result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.INTEGER_NUMBERS);
		  System.out.println(result);
		  Assert.assertTrue(result);
		  result = RangeOfValues.rangeEquality(rov, rov1, DefaultRangeOfValuesEnum.REAL_NUMBERS);
		  Assert.assertFalse(result);
	 }
	 
	 
}
