package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.SimpleMetric;
import it.uniroma2.gqm.model.Project;

import org.appfuse.service.GenericManager;

public interface MeasurementManager extends GenericManager<Measurement, Long> {
	public List<Measurement> findMeasuremntsByMetric(SimpleMetric metric);
	public List<Measurement> findByProject(Project project);
}
