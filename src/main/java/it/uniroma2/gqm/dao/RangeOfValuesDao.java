package it.uniroma2.gqm.dao;

import java.util.List;

import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.dao.GenericDao;

public interface RangeOfValuesDao extends
		GenericDao<RangeOfValues, Long> {

	public List<RangeOfValues> findByProject(Long id);  
	public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);
	public List<RangeOfValues> findBySupportedMeasurementScaleOBJ(MeasurementScaleTypeEnum type);

}
