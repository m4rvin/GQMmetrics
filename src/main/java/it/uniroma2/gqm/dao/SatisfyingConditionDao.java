package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionTarget;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface SatisfyingConditionDao extends GenericDao<SatisfyingCondition, Long>
{
	 public List<SatisfyingCondition> findSatisfyingConditionByProject(Long id);
	 public List<Object[]> findSatisfyingConditionTargetsByMetric(Long id);
	 public List<Object[]> findSatisfyingConditionTargetsByMetricWhenEditing(Long id);
	 public Object[] findSatisfyingConditionTargetByRepresentation(Long metric_id, Long question_id, Long goal_id);
}
