package it.uniroma2.gqm.dao;


import it.uniroma2.gqm.model.CombinedMetric;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface CombinedMetricDao extends GenericDao<CombinedMetric, Long> {
	public List<CombinedMetric> findByProject(Long id);
	public List<CombinedMetric> findByMeasurementScale(Long id);
}
