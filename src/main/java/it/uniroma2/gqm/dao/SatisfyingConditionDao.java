package it.uniroma2.gqm.dao;

import it.uniroma2.gqm.model.SatisfyingCondition;

import java.util.List;

import org.appfuse.dao.GenericDao;

public interface SatisfyingConditionDao extends GenericDao<SatisfyingCondition, Long>
{
	 public List<SatisfyingCondition> findSatisfyingConditionByProject(Long id);
}
