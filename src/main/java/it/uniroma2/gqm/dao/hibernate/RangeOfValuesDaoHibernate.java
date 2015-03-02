package it.uniroma2.gqm.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import it.uniroma2.gqm.dao.RangeOfValuesDao;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

@Repository("rangeOfValuesDao")
public class RangeOfValuesDaoHibernate extends GenericDaoHibernate<RangeOfValues, Long> implements RangeOfValuesDao
{

	 public RangeOfValuesDaoHibernate()
	 {
		  super(RangeOfValues.class);
	 }

	 @Override
	public List<RangeOfValues> findByProject(Long id) {
		 try {
			  Query q =  getSession().getNamedQuery("findRangeOfValuesByProject").setLong("project_id", id);
			  List res = q.list();
			  return res;
		 }
		 catch(Exception e)
		 {
			  System.out.println(e.getMessage());
		 }
		
    	return new ArrayList<RangeOfValues>();
	}

	 @Override
	 public RangeOfValues save(RangeOfValues object)
	 {
		  getSession().saveOrUpdate(object);
		  getSession().flush();
		  return super.save(object);
	 }

	 @Override
	 public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type)
	 {
		  Query q = getSession().getNamedQuery("findRangeOfValuesBySupportedMeasurementScale").setLong("measurementScaleType", type.ordinal());
		  return q.list();
	 }

}
