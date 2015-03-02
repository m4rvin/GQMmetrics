package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.MeasurementScale;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface MeasurementScaleDao extends GenericDao<MeasurementScale, Long>
{
	 public List<MeasurementScale> findByProject(Long projectId);
	 public List<MeasurementScale> findByRangeOfValues(Long rangeOfValuesId);
}
