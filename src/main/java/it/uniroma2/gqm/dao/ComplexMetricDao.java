package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface ComplexMetricDao extends GenericDao<AbstractMetric, Long>
{

	 // generic query
	 public List<AbstractMetric> findAllMetricByProject(Project project);

	 public List<AbstractMetric> findByMeasurementScale(Long id);
	 
	 public AbstractMetric findMetricByName(String name);
	 
	 public List<AbstractMetric> findMetricByMeasurementScaleType(MeasurementScaleTypeEnum type);

	 public List<AbstractMetric> findMetricByMeasurementScaleTypeExludingOneById(MeasurementScaleTypeEnum type, Long metricToExcludeId);

	 // SimpleMetric related query
	 public List<SimpleMetric> findSimpleMetricByProject(Project project);

	 public SimpleMetric findSimpleMetricById(Long id);

	 // CombinedMetric related query
	 public List<CombinedMetric> findCombinedMetricByProject(Project project);

	 public CombinedMetric findCombinedMetricById(Long id);
	 
	

}
