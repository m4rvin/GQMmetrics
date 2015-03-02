package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.MeasurementScaleDao;
import it.uniroma2.gqm.model.MeasurementScale;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("measurementScaleDao")
public class MeasurementScaleDaoHibernate extends GenericDaoHibernate<MeasurementScale, Long>  implements MeasurementScaleDao
{

	 public MeasurementScaleDaoHibernate()
	 {
		  super(MeasurementScale.class);
	 }

	 @Override
	 public List<MeasurementScale> findByProject(Long projectId)
	 {
		  	Query q = getSession().getNamedQuery("findMeasurementScaleByProject").setLong("project_id", projectId);
		  	return q.list();		  
	 }

	 @Override
	 public List<MeasurementScale> findByRangeOfValues(Long rangeOfValuesId)
	 {
		  Query q = getSession().getNamedQuery("findMeasurementScaleByRangeOfValues").setLong("rangeofvalues_id", rangeOfValuesId);
		  return q.list();
	 }
	 
	 

}
