package it.uniroma2.gqm.service;

import it.uniroma2.gqm.model.AbstractMetric;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;
import it.uniroma2.gqm.model.SatisfyingConditionTarget;

import java.util.List;

import org.appfuse.service.GenericManager;
import org.json.JSONArray;

public interface SatisfyingConditionManager extends GenericManager<SatisfyingCondition, Long>
{
	 public List<SatisfyingCondition> findByProject(Project project);
	 public JSONArray findTargetByMetricJSONized(AbstractMetric metric);
	 public List<String> findTargetByMetric(AbstractMetric metric);
	 public SatisfyingConditionTarget updateTargetByRepresentation(SatisfyingConditionTarget target);
}
