package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.model.Scale;
import it.uniroma2.gqm.model.Unit;

import java.util.List;
import org.appfuse.dao.GenericDao;

public interface MetricDao extends GenericDao<SimpleMetric, Long> {
	public List<SimpleMetric> findByProject(Long id);
	public List<SimpleMetric> findByMeasurementScale(Long id);
}
