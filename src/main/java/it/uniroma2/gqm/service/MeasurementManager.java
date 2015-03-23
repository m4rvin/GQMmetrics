package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Measurement;
import it.uniroma2.gqm.model.Project;

import java.util.List;

import org.appfuse.service.GenericManager;

public interface MeasurementManager extends GenericManager<Measurement, Long> {
	public List<Measurement> findMeasuremntsByMetric(AbstractMetric metric);
	public List<Measurement> findByProject(Project project);
}
