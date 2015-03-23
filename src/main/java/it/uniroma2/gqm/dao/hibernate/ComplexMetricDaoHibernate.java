package it.uniroma2.gqm.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;


@Repository("complexMetricDao")
public class ComplexMetricDaoHibernate extends GenericDaoHibernate<AbstractMetric, Long> implements ComplexMetricDao {

	public ComplexMetricDaoHibernate() {
		super(AbstractMetric.class);
	}

	@Override
	public List<AbstractMetric> findByProject(Long id) {
		Query q =  getSession().getNamedQuery("findMetricByProject").setLong("project_id", id);
    	return q.list();
	}

	@Override
	public List<AbstractMetric> findByMeasurementScale(Long id)
	{
		Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
		return q.list();
	}
	
}
