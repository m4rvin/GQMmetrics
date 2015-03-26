package it.uniroma2.gqm.service;

import junit.framework.Assert;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.webapp.controller.BaseControllerTestCase;

import org.json.JSONArray;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class MetricServiceTest extends BaseControllerTestCase
{

	 @Autowired
	 private ComplexMetricManager metricManager;
	 
	 @Test
	 public void testFindMetricByMeasurementScaleType()
	 {
		  JSONArray metrics = this.metricManager.findByMeasurementScaleType(MeasurementScaleTypeEnum.INTERVAL);
		  Assert.assertEquals(1, metrics.length());
		  metrics = this.metricManager.findByMeasurementScaleType(MeasurementScaleTypeEnum.RATIO);
		  Assert.assertEquals(2, metrics.length());
		  metrics = this.metricManager.findByMeasurementScaleType(MeasurementScaleTypeEnum.ORDINAL);
		  Assert.assertEquals(0, metrics.length());
		  metrics = this.metricManager.findByMeasurementScaleType(MeasurementScaleTypeEnum.NOMINAL);
		  Assert.assertEquals(2, metrics.length());
	 }
	 
}
