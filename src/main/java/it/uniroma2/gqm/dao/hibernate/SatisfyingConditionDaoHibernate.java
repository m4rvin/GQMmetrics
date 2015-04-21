package it.uniroma2.gqm.dao.hibernate;

import it.uniroma2.gqm.dao.SatisfyingConditionDao;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionTarget;

import java.util.List;

import org.appfuse.dao.hibernate.GenericDaoHibernate;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository(value = "satisfyingConditionDao")
public class SatisfyingConditionDaoHibernate extends GenericDaoHibernate<SatisfyingCondition, Long> implements SatisfyingConditionDao
{

	 public SatisfyingConditionDaoHibernate()
	 {
		  super(SatisfyingCondition.class);
	 }

	 @Override
	 public List<SatisfyingCondition> findSatisfyingConditionByProject(Long id)
	 {
		  Query q = getSession().getNamedQuery("findSatisfyingConditionByProject").setLong("project_id", id);
		  return q.list();
	 }

	 @Override
	 public List<Object[]> findSatisfyingConditionTargetsByMetric(Long id)
	 {
		  Query q = getSession().getNamedQuery("findSatisfyingConditionTargetByMetric").setLong("metric_id", id);
		  return q.list();
	 }

	 @Override
	 public Object[] findSatisfyingConditionTargetByRepresentation(Long metric_id, Long question_id, Long goal_id)
	 {
		  Query q = getSession().getNamedQuery("findSatisfyingConditionTargetByRepresentation").setLong("metric_id", metric_id).setLong("question_id", question_id).setLong("goal_id", goal_id);
		  return (Object[]) q.uniqueResult();
	 }

	 @Override
	 public List<Object[]> findSatisfyingConditionTargetsByMetricWhenEditing(Long id)
	 {
		  Query q = getSession().getNamedQuery("findSatisfyingConditionTargetByMetricEditing").setLong("metric_id", id);
		  return q.list();
	 }
}
