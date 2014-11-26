package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.Metric;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface MeasurementDao  extends GenericDao<Measurement, Long> {
	public List<Measurement> findMeasuremntsByMetric(Metric metric);
	public List<Measurement> findByProject(Long id);
}
