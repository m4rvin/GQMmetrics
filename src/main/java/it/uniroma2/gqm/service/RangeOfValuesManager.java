package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.service.GenericManager;
import org.json.JSONArray;

public interface RangeOfValuesManager extends GenericManager<RangeOfValues, Long>{

	public List<RangeOfValues> findByProject(Project project);
	public RangeOfValues findById(Long id);
	public RangeOfValues saveRangeOfValues(RangeOfValues rangeOfValues);
	public JSONArray findBySupportedMeasurementScaleJSONized(MeasurementScaleTypeEnum type);
	public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);
	public List<RangeOfValues> findBySupportedMeasurementScaleOBJ(MeasurementScaleTypeEnum type);
	public boolean isUsed(Long id);
	
}
