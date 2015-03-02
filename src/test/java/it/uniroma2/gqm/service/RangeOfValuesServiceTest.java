package it.uniroma2.gqm.service;

import java.util.List;

import junit.framework.Assert;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;
import it.uniroma2.gqm.webapp.controller.BaseControllerTestCase;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;


public class RangeOfValuesServiceTest extends BaseControllerTestCase
{

	 	@Autowired
	 	private RangeOfValuesManager mockRangeOfValuesManager;
	 	
		@Test
	 	public void testFindBySupportedMeasurementScale()
	 	{
	 		List<String> rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.INTERVAL);
	 		Assert.assertEquals(2, rovs.size());
	 	   rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.NOMINAL);
	 		Assert.assertEquals(4, rovs.size());
	 		rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.ORDINAL);
	 		Assert.assertEquals(3, rovs.size());
	 		rovs = this.mockRangeOfValuesManager.findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum.RATIO);
	 		Assert.assertEquals(1, rovs.size());
	 	}
}
