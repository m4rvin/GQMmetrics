package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.Project;

import org.appfuse.service.GenericManager;

public interface MeasurementScaleManager extends GenericManager<MeasurementScale, Long> {

	public List<MeasurementScale> findByProject(Project project);

}
