package it.uniroma2.gqm.dao.hibernate;

import java.util.List;
import java.util.Map;

import it.uniroma2.gqm.dao.DefaultOperationDao;
import it.uniroma2.gqm.model.DefaultOperation;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.RangeOfValues;

import org.appfuse.dao.SearchException;
import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


@Repository("DefaultOperationDao")
public class DefaultOperationDaoHibernate extends
		GenericDaoHibernate<DefaultOperation, Long> implements
		DefaultOperationDao {

	public DefaultOperationDaoHibernate() {
		super(DefaultOperation.class);
	}


   @Override
   public List<Object> findBySupportedMeasurementScale(MeasurementScaleTypeEnum type)
   {
   	 Query q = getSession().getNamedQuery("findRangeOfValuesBySupportedMeasurementScale").setLong("measurementScaleType", type.ordinal());
   	 return q.list();
   }
   
}
