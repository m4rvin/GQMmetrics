package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;

import org.appfuse.service.GenericManager;
import org.json.JSONArray;

public interface DefaultOperationManager extends GenericManager<DefaultOperation, Long>
{
	 public JSONArray findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum type);
	 public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);
	 public List<DefaultOperation> findBySupportedMeasurementScaleOBJ(MeasurementScaleTypeEnum type);


}
