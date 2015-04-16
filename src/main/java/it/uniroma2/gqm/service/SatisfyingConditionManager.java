package it.uniroma2.gqm.service;

import java.util.List;

import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;

import org.appfuse.service.GenericManager;

public interface SatisfyingConditionManager extends GenericManager<SatisfyingCondition, Long>
{
	 public List<SatisfyingCondition> findByProject(Project project);
}
