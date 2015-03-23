package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;

import org.appfuse.service.GenericManager;

public interface ComplexMetricManager extends GenericManager<AbstractMetric, Long> {
	
	public List<AbstractMetric> findByProject(Project project);
}
