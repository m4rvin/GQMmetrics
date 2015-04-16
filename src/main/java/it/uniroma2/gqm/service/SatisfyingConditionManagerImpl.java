package it.uniroma2.gqm.service;

import it.uniroma2.gqm.dao.SatisfyingConditionDao;
import it.uniroma2.gqm.model.Project;
import it.uniroma2.gqm.model.SatisfyingCondition;

import java.util.List;

import org.appfuse.service.impl.GenericManagerImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service(value = "satisfyingConditionManager")
public class SatisfyingConditionManagerImpl extends GenericManagerImpl<SatisfyingCondition, Long> implements SatisfyingConditionManager
{
	 
	 @Autowired
	 SatisfyingConditionDao satisfyingConditionDao;
	 
	 @Transactional
	 @Override
	 public List<SatisfyingCondition> findByProject(Project project)
	 {
		 List<SatisfyingCondition> result = this.satisfyingConditionDao.findSatisfyingConditionByProject(project.getId());
		 return result;
	 }

}
