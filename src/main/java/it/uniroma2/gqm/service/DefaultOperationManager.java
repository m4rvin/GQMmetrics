package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;

import org.appfuse.service.GenericManager;
import org.json.JSONArray;

public interface DefaultOperationManager extends GenericManager<DefaultOperation, Long>
{
	 public JSONArray findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);
}
