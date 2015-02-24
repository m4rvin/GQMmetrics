package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.service.GenericManager;

public interface RangeOfValuesManager extends GenericManager<RangeOfValues, Long>{

	public List<RangeOfValues> findByProject(Project project);
	public RangeOfValues findById(Long id);
	public RangeOfValues saveRangeOfValues(RangeOfValues rangeOfValues);
	public List<RangeOfValues> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);
	
}
