package it.uniroma2.gqm.dao;

import java.util.List;

import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;

import org.appfuse.dao.GenericDao;

public interface DefaultOperationDao extends GenericDao<DefaultOperation, Long> {
	
	public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type);

}
