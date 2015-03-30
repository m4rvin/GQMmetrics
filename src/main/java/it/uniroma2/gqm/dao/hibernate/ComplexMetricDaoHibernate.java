package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.ComplexMetricDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.CombinedMetric;
import it.uniroma2.gqm.model.MeasurementScaleTypeEnum;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SimpleMetric;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;

@Repository("complexMetricDao")
public class ComplexMetricDaoHibernate extends GenericDaoHibernate<AbstractMetric, Long> implements ComplexMetricDao
{

	 public ComplexMetricDaoHibernate()
	 {
		  super(AbstractMetric.class);
	 }

	 // generic queries

	 @Override
	 public List<AbstractMetric> findAllMetricByProject(Project project)
	 {
		  Query q = getSession().getNamedQuery("findMetricByProject").setLong("project_id", project.getId());
		  return q.list();
	 }

	 @Override
	 public List<AbstractMetric> findByMeasurementScale(Long id)
	 {
		  Query q = getSession().getNamedQuery("findByMeasurementScale").setLong("measurementScaleId", id);
		  return q.list();
	 }

	 @Override
	 public AbstractMetric findMetricByName(String name) throws IndexOutOfBoundsException
	 {
		  Query q = getSession().getNamedQuery("findMetricByName").setString("name", name);
		  return (AbstractMetric) q.list().get(0);
	 }
	 
	 @Override
	 public List<AbstractMetric> findMetricByMeasurementScaleType(MeasurementScaleTypeEnum type)
	 {
		 Query q = getSession().getNamedQuery("findMetricByMeasurementScaleType").setLong("type", type.ordinal());
		 return q.list();
	 }
	 
	 @Override
	 public List<AbstractMetric> findMetricByMeasurementScaleTypeExludingOneById(MeasurementScaleTypeEnum type, Long metricToExcludeId)
	 {
		 Query q = getSession().getNamedQuery("findMetricByMeasurementScaleTypeExludingOneById").setLong("type", type.ordinal()).setLong("id", metricToExcludeId);
		 return q.list();
	 }

	 // SimpleMetric related queries

	 @Override
	 public List<SimpleMetric> findSimpleMetricByProject(Project project)
	 {
		  Query q = getSession().getNamedQuery("findSimpleMetricByProject").setLong("project_id", project.getId());
		  return q.list();
	 }

	 @Override
	 public SimpleMetric findSimpleMetricById(Long id)
	 {
		  return (SimpleMetric) this.get(id);
	 }

	 // CombinedMetric related queries

	 @Override
	 public List<CombinedMetric> findCombinedMetricByProject(Project project)
	 {
		  Query q = getSession().getNamedQuery("findCombinedMetricByProject").setLong("project_id", project.getId());
		  return q.list();
	 }

	 @Override
	 public CombinedMetric findCombinedMetricById(Long id)
	 {
		  return (CombinedMetric) this.get(id);
	 }

}
