package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.SatisfyingCondition;

import java.util.List;
import java.util.Set;

import org.appfuse.dao.GenericDao;

public interface SatisfyingConditionDao extends GenericDao<SatisfyingCondition, Long>
{
	 public List<SatisfyingCondition> findSatisfyingConditionByProject(Long id);
	 public List<Object[]> findSatisfyingConditionTargetsByMetric(Long id);
	 public List<Object[]> findSatisfyingConditionTargetsByMetricWhenEditing(Long id);
	 public Object[] findSatisfyingConditionTargetByRepresentation(Long metric_id, Long question_id, Long goal_id);
	 
	 public List<SatisfyingCondition> findByProjectGoalMetric(Long project_id, Long goal_id, Long metric_id);

}
