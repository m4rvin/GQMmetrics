package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.dao.MeasurementScaleDao;
import it.uniroma2.gqm.model.MeasurementScale;
import it.uniroma2.gqm.model.Project;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("measurementScaleManager")
public class MeasurementScaleManagerImpl extends GenericManagerImpl<MeasurementScale, Long> implements MeasurementScaleManager
{

	 private MeasurementScaleDao measurementScaleDao;
	 private ComplexMetricDao metricDao;

	 @Autowired
	 public MeasurementScaleManagerImpl(MeasurementScaleDao measurementScaleDao)
	 {
		  super(measurementScaleDao);
		  this.measurementScaleDao = measurementScaleDao;
	 }

	 @Autowired
	 public void setMetricDao(ComplexMetricDao metricDao)
	 {
		  this.metricDao = metricDao;
	 }

	 @Override
	 public List<MeasurementScale> findByProject(Project project)
	 {

		  if (project != null)
		  {
				return this.measurementScaleDao.findByProject(project.getId());
		  } else
				return null;
	 }

	 @Override
	 public boolean isUsed(Long id)
	 {
		  if (id != null)
				return this.metricDao.findByMeasurementScale(id).size() != 0;
		  return true;
	 }

}
