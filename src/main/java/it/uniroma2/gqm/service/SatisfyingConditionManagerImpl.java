package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.SatisfyingConditionDao;
import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Goal;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.Question;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionTarget;

import java.util.ArrayList;
import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "satisfyingConditionManager")
public class SatisfyingConditionManagerImpl extends GenericManagerImpl<SatisfyingCondition, Long> implements SatisfyingConditionManager
{
	 SatisfyingConditionDao satisfyingConditionDao;
	 
	 @Autowired
	 public SatisfyingConditionManagerImpl(SatisfyingConditionDao dao)
	 {
		  super(dao);
		  this.satisfyingConditionDao = dao;
	 }
	 
	 @Transactional
	 @Override
	 public List<SatisfyingCondition> findByProject(Project project)
	 {
		 List<SatisfyingCondition> result = this.satisfyingConditionDao.findSatisfyingConditionByProject(project.getId());
		 return result;
	 }

	 @Transactional
	 @Override
	 public JSONArray findTargetByMetricJSONized(AbstractMetric metric)
	 {
		  List<Object[]> result = this.satisfyingConditionDao.findSatisfyingConditionTargetsByMetric(metric.getId());
		  JSONArray ret = new JSONArray();
		  for(Object[] r : result)
		  {
				Goal g = (Goal) r[0];
				Question q = (Question) r[1];
				AbstractMetric m = (AbstractMetric) r[2];
				String target = g.getId() + "-" + q.getId() + "-" + m.getId();
				ret.put(target);
		  }
		  if(ret.length() > 0)
				return  ret;
		  return null;
	 }
	 
	 @Transactional
	 @Override
	 public List<String> findTargetByMetric(AbstractMetric metric)
	 {
		  List<Object[]> result = this.satisfyingConditionDao.findSatisfyingConditionTargetsByMetric(metric.getId());
		  List<String> ret = new ArrayList<String>();
		  for(Object[] r : result)
		  {
				Goal g = (Goal) r[0];
				Question q = (Question) r[1];
				AbstractMetric m = (AbstractMetric) r[2];
				String target = g.getId() + "-" + q.getId() + "-" + m.getId();
				ret.add(target);
		  }
		  if(ret.size() > 0)
				return  ret;
		  return null;
	 }

	 @Override
	 @Transactional
	 public SatisfyingConditionTarget updateTargetByRepresentation(SatisfyingConditionTarget target)
	 {
		  String[] ids = target.getRepresentation().split("-");
		  Long goal_id = new Long(ids[0]);
		  Long question_id = new Long(ids[1]);
		  Long metric_id = new Long(ids[2]);
		  
		  Object[] res = this.satisfyingConditionDao.findSatisfyingConditionTargetByRepresentation(metric_id, question_id, goal_id);
		  if(res != null)
		  {
				target.setGoal((Goal) res[0]);
				target.setQuestion((Question) res[1]);
				target.setMetric((AbstractMetric) res[2]);
				return target;
		  }
		  return null;
	 }

	 /**
	  * Use this when handling editing cases, it does not filter on the already existing targets
	  */
	 @Override
	 public JSONArray findTargetByMetricWhenEditingJSONized(AbstractMetric metric)
	 {
		  List<Object[]> result = this.satisfyingConditionDao.findSatisfyingConditionTargetsByMetricWhenEditing(metric.getId());
		  JSONArray ret = new JSONArray();
		  for(Object[] r : result)
		  {
				Goal g = (Goal) r[0];
				Question q = (Question) r[1];
				AbstractMetric m = (AbstractMetric) r[2];
				String target = g.getId() + "-" + q.getId() + "-" + m.getId();
				ret.put(target);
		  }
		  if(ret.length() > 0)
				return  ret;
		  return null;
	 }
	 
	 @Transactional
	 @Override
	 public List<String> findTargetByMetricWhenEditing(AbstractMetric metric)
	 {
		  List<Object[]> result = this.satisfyingConditionDao.findSatisfyingConditionTargetsByMetricWhenEditing(metric.getId());
		  List<String> ret = new ArrayList<String>();
		  for(Object[] r : result)
		  {
				Goal g = (Goal) r[0];
				Question q = (Question) r[1];
				AbstractMetric m = (AbstractMetric) r[2];
				String target = g.getId() + "-" + q.getId() + "-" + m.getId();
				ret.add(target);
		  }
		  if(ret.size() > 0)
				return  ret;
		  return null;
	 }
}
