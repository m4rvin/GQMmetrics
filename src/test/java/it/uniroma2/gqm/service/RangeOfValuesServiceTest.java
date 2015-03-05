package it.uniroma2.gqm.service;

import junit.framework.Assert;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.webapp.controller.BaseControllerTestCase;

import org.json.JSONArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RangeOfValuesServiceTest extends BaseControllerTestCase
{

	 	@Autowired
	 	private RangeOfValuesManager mockRangeOfValuesManager;
	 	
		@Test
	 	public void testFindBySupportedMeasurementScale()
	 	{
	 		JSONArray rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.INTERVAL);
	 		Assert.assertEquals(2, rovs.length());
	 	   rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.NOMINAL);
	 		Assert.assertEquals(4, rovs.length());
	 		rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.ORDINAL);
	 		Assert.assertEquals(3, rovs.length());
	 		rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.RATIO);
	 		Assert.assertEquals(1, rovs.length());
	 	}
}
