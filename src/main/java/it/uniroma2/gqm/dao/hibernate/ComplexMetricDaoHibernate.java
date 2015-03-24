package it.uniroma2.gqm.dao.hibernate;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SimpleMetric;


@Repository("complexMetricDao")
public class ComplexMetricDaoHibernate extends GenericDaoHibernate<AbstractMetric, Long> implements ComplexMetricDao {

	public ComplexMetricDaoHibernate() {
		super(AbstractMetric.class);
	}


	//generic queries
	
	@Override
	public List<AbstractMetric> findAllMetricByProject(Project project) {
		Query q = getSession().getNamedQuery("findMetricByProject").setLong("project_id", project.getId());
		return q.list();
	}
	
	@Override
	public List<AbstractMetric> findByMeasurementScale(Long id)
	{
		Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
		return q.list();
	}

	
	//SimpleMetric related queries
	
	@Override
	public List<SimpleMetric> findSimpleMetricByProject(Project project) {
		Query q = getSession().getNamedQuery("findSimpleMetricByProject").setLong("project_id", project.getId());
		return q.list();
	}

	@Override
	public SimpleMetric findSimpleMetricById(Long id) {
		return (SimpleMetric) this.get(id);
	}
	
	
	//CombinedMetric related queries

	@Override
	public List<CombinedMetric> findCombinedMetricByProject(Project project) {
		Query q = getSession().getNamedQuery("findCombinedMetricByProject").setLong("project_id", project.getId());
		return q.list();
	}

	@Override
	public CombinedMetric findCombinedMetricById(Long id) {
		return (CombinedMetric) this.get(id);
	}
}
